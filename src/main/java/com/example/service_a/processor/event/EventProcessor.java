package com.example.service_a.processor.event;

import com.example.service_a.payload.request.EventRequestBody;
import com.example.service_a.payload.response.EventResponseBody;
import io.micrometer.core.annotation.Timed;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class EventProcessor implements Processor {

  @Override
  @Timed(value = "processor.event", description = "Processor of Event")
  public void process(Exchange exchange) {
    log.info("Event Processor: exchange data");
    EventRequestBody event = exchange.getIn().getBody(EventRequestBody.class);

    EventResponseBody responseEvent = new EventResponseBody(UUID.randomUUID().toString(),
        event.getMessage().toUpperCase(), exchange.getExchangeId());
    exchange.getIn().setBody(responseEvent);
  }
}
