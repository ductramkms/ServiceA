package com.example.ServiceA.processor.employee;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.example.ServiceA.payload.response.CamelResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Employee Processor: exchange data");
        String result = exchange.getIn().getBody(String.class);
        CamelResponseBody body = CamelResponseBody.fromJson(result);
        exchange.getMessage().setBody(body);
    }

}
