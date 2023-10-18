package com.example.ServiceA.controller;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    @Produce("direct:employee")
    private final ProducerTemplate producerTemplate;

    public EmployeeController(ProducerTemplate template) {
        this.producerTemplate = template;
    }

    @GetMapping
    public String allEvents() {
        String result = (String) producerTemplate.requestBody(null);
        return result;
    }
}
