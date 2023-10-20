package com.example.ServiceA.controller;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Produce
    private final ProducerTemplate producerTemplate;

    public EmployeeController(ProducerTemplate template) {
        this.producerTemplate = template;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String all() {
        String result = (String) producerTemplate.requestBody("direct:employee", "get-all");
        return result;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getById(@PathVariable Integer id) {
        String result = (String) producerTemplate.requestBodyAndHeader("direct:employee",
                "get-by-id", "id", id);

        return result;
    }
}
