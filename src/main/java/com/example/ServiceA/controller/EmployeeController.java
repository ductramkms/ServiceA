package com.example.ServiceA.controller;

import com.example.ServiceA.constant.Constant;
import com.example.ServiceA.payload.response.CamelResponseBody;
import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "employees", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EmployeeController {

    @Produce
    private final ProducerTemplate producerTemplate;

    public EmployeeController(ProducerTemplate template) {
        this.producerTemplate = template;
    }

    /**
     * This function return the common type of result of each APIs.
     *
     * @param body common type of content body
     * @return response entity
     */
    private ResponseEntity<CamelResponseBody> result(CamelResponseBody body) {
        return ResponseEntity.status(HttpStatus.valueOf(body.getStatus())).body(body);
    }

    @GetMapping
    public ResponseEntity<CamelResponseBody> all() {
        log.info("Get all employees");
        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employees", null,
                Constant.REQ_TYPE, Constant.GET_EMPLOYEES);

        return result(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamelResponseBody> getById(@PathVariable Integer id) {
        log.info("Get employee by id = " + id);
        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employees",
                id, Constant.REQ_TYPE, Constant.GET_EMPLOYEE_BY_ID);

        return result(body);
    }

    @PostMapping
    public ResponseEntity<CamelResponseBody> create(@RequestBody String value) {
        log.info("Create new employee");

        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employees",
                value, Constant.REQ_TYPE, Constant.CREATE_EMPLOYEE);

        return result(body);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PutMapping
    public ResponseEntity<CamelResponseBody> update(@RequestBody String value) {
        log.info("Create new employee");

        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employees",
                value, Constant.REQ_TYPE, Constant.UPDATE_EMPLOYEE);

        sendMessage("Hello server a");
        return result(body);
    }

    private void sendMessage(String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate
                .send(Constant.TOPIC_1, message);

        try {
            SendResult<String, String> result = future.completable().get();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // future.addCallback(arg0 -> {
        //     return null;
        // });

        // future.whenComplete((result, ex) -> {
        //     if (ex == null) {
        //         System.out.println("Sent message=[" + message +
        //                 "] with offset=[" + result.getRecordMetadata().offset() + "]");
        //     } else {
        //         System.out.println("Unable to send message=[" +
        //                 message + "] due to : " + ex.getMessage());
        //     }
        // });
    }
}
