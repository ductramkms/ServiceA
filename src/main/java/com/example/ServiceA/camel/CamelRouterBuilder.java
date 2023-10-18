package com.example.ServiceA.camel;

import com.example.ServiceA.processor.EventProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelRouterBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    restConfiguration().host("localhost").port(8081);

    from("direct:event")
        .log("Sending message with body and header")
        .to("log:output")
        .process(new EventProcessor());

    from("direct:employee")
        .to("rest:get:employee")
        .log("${body}")
        .process(exchange -> {
          String result = exchange.getIn().getBody(String.class);
          System.out.println("@D_LOG: " + result);
          exchange.getMessage().setBody(result);
        });
  }
}
