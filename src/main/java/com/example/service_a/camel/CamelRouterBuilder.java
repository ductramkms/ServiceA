package com.example.service_a.camel;

import com.example.service_a.constant.Constant;
import com.example.service_a.processor.employee.EmployeeProcessor;
import com.example.service_a.processor.event.EventProcessor;
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
  public void configure() {
    restConfiguration()
        .host(host)
        .port(port)
        .component("http")
        .endpointProperty("bridgeEndpoint", "true")
        .endpointProperty("throwExceptionOnFailure", "false");

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
        .process(new EmployeeProcessor())
        // GET ALL EMPLOYEE BY ID
        .when(header(Constant.REQ_TYPE).isEqualTo(Constant.GET_EMPLOYEE_BY_ID))
        .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
        .toD("rest:get:employees/${body}")
        .process(new EmployeeProcessor())
        // CREATE EMPLOYEE
        .when(header(Constant.REQ_TYPE).isEqualTo(Constant.CREATE_EMPLOYEE))
        .setBody()
        .simple("${body}")
        .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
        .setHeader("", constant(""))
        .to("rest:post:employees")
        .process(new EmployeeProcessor())
        // UPDATE EMPLOYEE
        .when(header(Constant.REQ_TYPE).isEqualTo(Constant.UPDATE_EMPLOYEE))
        .setBody()
        .simple("${body}")
        .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
        .to("rest:put:employees")
        .process(new EmployeeProcessor());

    // Handle for event router
    from("direct:event")
        .log("Sending message with body and header")
        .to("log:output")
        .process(new EventProcessor());
  }
}
