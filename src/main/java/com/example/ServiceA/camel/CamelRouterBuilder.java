package com.example.ServiceA.camel;

import com.example.ServiceA.constant.Constant;
import com.example.ServiceA.processor.employee.EmployeeProcessor;
import com.example.ServiceA.processor.event.EventProcessor;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class CamelRouterBuilder extends RouteBuilder {

    @Value("${app.service_b.host}")
    private String host;
    @Value("${app.service_b.port}")
    private String port;

    @Override
    public void configure() throws Exception {
        restConfiguration().host(host).port(port).component("http")
                .endpointProperty("bridgeEndpoint", "true")
                .endpointProperty("throwExceptionOnFailure", "false"); // Add this line

        // Handle for employee
        from("direct:employees")
                .errorHandler(
                        deadLetterChannel("direct:error")
                                .maximumRedeliveries(3)
                                .redeliveryDelay(500)
                                .retryAttemptedLogLevel(LoggingLevel.WARN)
                ).choice()
                // GET ALL EMPLOYEES
                .when(header(Constant.REQ_TYPE).isEqualTo(Constant.GET_EMPLOYEES))
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
                .to("rest:get:employees")
                .log("log:${body}")
                .process(new EmployeeProcessor())
                // GET ALL EMPLOYEE BY ID
                .when(header(Constant.REQ_TYPE).isEqualTo(Constant.GET_EMPLOYEE_BY_ID))
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
                .log("@D_LOG REQUEST: ${headers}")
                .toD("rest:get:employees/${body}")
                .log("@D_LOG: RESPONSE: ${headers}")
                .log("@D_LOG OF GET BY ID: ${body}")
                .process(new EmployeeProcessor())
                // CREATE EMPLOYEE
                .when(header(Constant.REQ_TYPE).isEqualTo(Constant.CREATE_EMPLOYEE))
                .setBody().simple("${body}")
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
                .to("rest:post:employees")
                .log("@D_LOG: ${body}")
                .process(new EmployeeProcessor())
                // UPDATE EMPLOYEE
                .when(header(Constant.REQ_TYPE).isEqualTo(Constant.UPDATE_EMPLOYEE))
                .setBody().simple("${body}")
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
                .to("rest:put:employees")
                .log("@D_LOG: ${body}")
                .process(new EmployeeProcessor());

        // Handle for event router
        from("direct:event")
                .log("Sending message with body and header")
                .to("log:output")
                .process(new EventProcessor());
    }
}
