package com.example.ServiceA.controller;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ServiceA.constant.Constant;
import com.example.ServiceA.payload.response.CamelResponseBody;

@RestController
@RequestMapping(path = "employee", produces = MediaType.APPLICATION_JSON_VALUE)
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
        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employee", null,
                Constant.REQ_TYPE, Constant.GET_EMPLOYEES);

        return result(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamelResponseBody> getById(@PathVariable Integer id) {

        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employee",
                id, Constant.REQ_TYPE, Constant.GET_EMPLOYEE_BY_ID);

        return result(body);
    }

    @PostMapping
    public ResponseEntity<CamelResponseBody> create(@RequestBody String value) {
        CamelResponseBody body = (CamelResponseBody) producerTemplate.requestBodyAndHeader(
                "direct:employee",
                value, Constant.REQ_TYPE, Constant.CREATE_EMPLOYEE);

        return result(body);
    }
}
