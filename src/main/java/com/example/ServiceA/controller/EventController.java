package com.example.ServiceA.controller;

import com.example.ServiceA.payload.request.EventRequestBody;
import com.example.ServiceA.payload.response.EventResponseBody;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

  private static final Logger logger = LogManager.getLogger(EventController.class);

  @Produce("direct:event")
  private final ProducerTemplate producerTemplate;

  public EventController(ProducerTemplate template) {
    this.producerTemplate = template;
  }

  @PostMapping(value = "/event")
  public EventResponseBody getEvent(@RequestBody EventRequestBody body) {
    EventResponseBody response = (EventResponseBody) producerTemplate.requestBody(body);
    logger.debug("EventResponseBody from the camel router: " + response.toString());
    return response;
  }
}
