package com.example.ServiceA.camel;

import com.example.ServiceA.processor.EventProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelRouterBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration().host("localhost").port(8080);

        from("direct:event")
                .log("Sending message with body and header")
                .to("log:output")
                .process(new EventProcessor());
    }
}
