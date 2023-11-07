package com.example.ServiceA.controller;

import com.example.ServiceA.constant.Constant;
import com.example.ServiceA.payload.request.KafkaRequestBody;
import com.example.ServiceA.payload.response.CamelResponseBody;
import com.example.ServiceA.util.ColorLog;
import com.example.ServiceA.util.Helper;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        log.info(ColorLog.getLog("Get all employees"));
        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employees", null,
                Constant.REQ_TYPE, Constant.GET_EMPLOYEES);

        return result(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamelResponseBody> getById(@PathVariable Integer id) {
        log.info(ColorLog.getLog("Get employee by id = " + id));
        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employees",
                id, Constant.REQ_TYPE, Constant.GET_EMPLOYEE_BY_ID);

        return result(body);
    }

    @PostMapping
    public ResponseEntity<CamelResponseBody> create(@RequestBody String value) {
        log.info(ColorLog.getLog("Create new employee"));

        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employees",
                value, Constant.REQ_TYPE, Constant.CREATE_EMPLOYEE);

        return result(body);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PutMapping
    public ResponseEntity<CamelResponseBody> update(@RequestBody String value) {
        log.info(ColorLog.getLog("Update new employee"));

        KafkaRequestBody message = new KafkaRequestBody(Constant.UPDATE_EMPLOYEE, value);
        kafkaTemplate.send(Constant.TOPIC_1, Helper.jsonSerialize(message));

        return ResponseEntity.accepted().body(CamelResponseBody.builder().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CamelResponseBody> delete(@PathVariable Integer id) {
        log.info(ColorLog.getLog("Delete employee " + id));

        KafkaRequestBody message = new KafkaRequestBody(Constant.DELETE_EMPLOYEE, "" + id);
        kafkaTemplate.send(Constant.TOPIC_1, Helper.jsonSerialize(message));

        return ResponseEntity.accepted().body(CamelResponseBody.builder().build());
    }
}
