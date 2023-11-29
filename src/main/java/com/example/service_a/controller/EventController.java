package com.example.service_a.controller;

import com.example.service_a.payload.request.EventRequestBody;
import com.example.service_a.payload.response.EventResponseBody;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/event")
public class EventController {

  @Produce("direct:event")
  private final ProducerTemplate producerTemplate;


  public EventController(ProducerTemplate template) {
    this.producerTemplate = template;
  }

  @PostMapping()
  @Timed(value = "controller.event.get")
  public EventResponseBody getEvent(@RequestBody EventRequestBody body) {
    EventResponseBody response = (EventResponseBody) producerTemplate.requestBody(body);
    log.debug("EventResponseBody from the camel router: " + response
        .toString());
    return response;
  }
}
