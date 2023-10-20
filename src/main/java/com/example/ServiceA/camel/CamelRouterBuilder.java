package com.example.ServiceA.camel;

import com.example.ServiceA.processor.EventProcessor;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class CamelRouterBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    restConfiguration().host("localhost").port(8081).component("http")
        .endpointProperty("bridgeEndpoint", "true")
        .endpointProperty("throwExceptionOnFailure", "false"); // Add this line

    onException(Exception.class).to("log:ERRROROOROOOOROORORO");

    from("direct:employee").choice()
        .when(body().isEqualTo("get-all"))
        .to("rest:get:employee")
        .log("log:${body}")
        .process(exchange -> {
          String result = exchange.getIn().getBody(String.class);
          exchange.getMessage().setBody(result);
        })
        .when(body().isEqualTo("get-by-id"))
        .log("@D_LOG Header: ${headers.id}")
        .toD("rest:get:employee/${headers.id}")
        .log("@D_LOG: ${body}")
        .log("@D_LOG ID: ${headers.id}")
        .process(exchange -> {
          // Map<String, Object> header = exchange.getIn().getHeaders();

          // for (Entry<String, Object> entry : header.entrySet()) {
          //   System.out.println("@D_LOG: " + entry.getKey() + " ~ " + entry.getValue());
          // }

          String result = exchange.getIn().getBody(String.class);
          System.out.println("@D_LOG: " + result);
          exchange.getMessage().setBody(result);

          Map<String, Object> header = exchange.getMessage().getHeaders();

          for (Entry<String, Object> entry : header.entrySet()) {
            System.out.println("@D_LOG: " + entry.getKey() + " ~ " + entry.getValue());
          }
        });

    from("direct:event")
        .log("Sending message with body and header")
        .to("log:output")
        .process(new EventProcessor());
  }
}
