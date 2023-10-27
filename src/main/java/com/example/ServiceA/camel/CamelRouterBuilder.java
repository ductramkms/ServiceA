package com.example.ServiceA.camel;

import com.example.ServiceA.constant.Constant;
import com.example.ServiceA.payload.response.CamelResponseBody;
import com.example.ServiceA.processor.EventProcessor;
import org.apache.camel.Exchange;
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
    from("direct:employee").choice()
        // GET ALL EMPLOYEES
        .when(header(Constant.REQ_TYPE).isEqualTo(Constant.GET_EMPLOYEES))
        .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
        .to("rest:get:employee")
        .log("log:${body}")
        .process(exchange -> {
          String result = exchange.getIn().getBody(String.class);
          CamelResponseBody body = CamelResponseBody.fromJson(result);
          exchange.getMessage().setBody(body);
        })
        // GET ALL EMPLOYEE BY ID
        .when(header(Constant.REQ_TYPE).isEqualTo(Constant.GET_EMPLOYEE_BY_ID))
        .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
        .log("@D_LOG REQUEST: ${headers}")
        .toD("rest:get:employee/${body}")
        .log("@D_LOG: RESPONSE: ${headers}")
        .log("@D_LOG OF GET BY ID: ${body}")
        .process(exchange -> {
          String result = exchange.getIn().getBody(String.class);
          CamelResponseBody body = CamelResponseBody.fromJson(result);
          exchange.getMessage().setBody(body);
        })
        // CREATE EMPLOYEE
        .when(header(Constant.REQ_TYPE).isEqualTo(Constant.CREATE_EMPLOYEE))
        .setBody().simple("${body}")
        .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
        .to("rest:post:employee")
        .log("@D_LOG: ${body}")
        .process(exchange -> {
          String result = exchange.getIn().getBody(String.class);
          CamelResponseBody body = CamelResponseBody.fromJson(result);
          exchange.getMessage().setBody(body);
        });

    // Handle for event router
    from("direct:event")
        .log("Sending message with body and header")
        .to("log:output")
        .process(new EventProcessor());
  }
}
