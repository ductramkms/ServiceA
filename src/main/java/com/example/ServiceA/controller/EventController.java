package com.example.ServiceA.controller;

import com.example.ServiceA.payload.request.EventRequestBody;
import com.example.ServiceA.payload.response.EventResponseBody;
import com.example.ServiceA.util.ColorLog;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EventController {

    @Produce("direct:event")
    private final ProducerTemplate producerTemplate;

    public EventController(ProducerTemplate template) {
        this.producerTemplate = template;
    }

    @PostMapping(value = "/event")
    public EventResponseBody getEvent(@RequestBody EventRequestBody body) {
        EventResponseBody response = (EventResponseBody) producerTemplate.requestBody(body);
        log.debug(ColorLog.getLog("EventResponseBody from the camel router: " + response
                .toString()));
        return response;
    }
}
