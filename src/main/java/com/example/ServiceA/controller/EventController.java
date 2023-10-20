package com.example.ServiceA.controller;

import com.example.ServiceA.payload.request.EventRequestBody;
import com.example.ServiceA.payload.response.EventResponseBody;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventController {
  @Produce("direct:event")
  private final ProducerTemplate producerTemplate;

  public EventController(ProducerTemplate template) {
    this.producerTemplate = template;
  }

  @PostMapping(value = "/event")
  public EventResponseBody getEvent(@RequestBody EventRequestBody body) {
    EventResponseBody response = (EventResponseBody) producerTemplate.requestBody(body);
    System.out.println(response.toString());
    return response;
  }
}
