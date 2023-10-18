package com.example.ServiceA.processor;

import com.example.ServiceA.payload.request.EventRequestBody;
import com.example.ServiceA.payload.response.EventResponseBody;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import java.util.UUID;

public class EventProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    EventRequestBody event = exchange.getIn().getBody(EventRequestBody.class);
    EventResponseBody responseEvent = new EventResponseBody(UUID.randomUUID().toString(),
        event.getMessage().toUpperCase(), exchange.getExchangeId());
    exchange.getIn().setBody(responseEvent);
  }
}
