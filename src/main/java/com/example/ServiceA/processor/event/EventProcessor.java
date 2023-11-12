package com.example.ServiceA.processor.event;

import com.example.ServiceA.payload.request.EventRequestBody;
import com.example.ServiceA.payload.response.EventResponseBody;
import com.example.ServiceA.util.ColorLog;

import io.micrometer.core.annotation.Timed;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class EventProcessor implements Processor {

    @Timed(value = "processor.event", description = "Processor of Event")
    @Override
    public void process(Exchange exchange) throws Exception {
        log.info(ColorLog.getLog("Event Processor: exchange data"));
        EventRequestBody event = exchange.getIn().getBody(EventRequestBody.class);

        EventResponseBody responseEvent = new EventResponseBody(UUID.randomUUID().toString(),
                event.getMessage().toUpperCase(), exchange.getExchangeId());
        exchange.getIn().setBody(responseEvent);
    }
}
