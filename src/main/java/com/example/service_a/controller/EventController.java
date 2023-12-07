package com.example.service_a.controller;

import com.example.service_a.constant.Constant;
import com.example.service_a.payload.request.EventRequestBody;
import com.example.service_a.payload.response.EventResponseBody;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/event")
public class EventController {

    @Produce("direct:event")
    private final ProducerTemplate producerTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public EventController(ProducerTemplate template, KafkaTemplate<String, String> kafkaTemplate) {
        this.producerTemplate = template;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    @Timed(value = "controller.event.get")
    public EventResponseBody getEvent(@RequestBody EventRequestBody body) {
        EventResponseBody response = (EventResponseBody) producerTemplate.requestBody(body);
        log.debug("EventResponseBody from the camel router: " + response
                .toString());
        return response;
    }

    @PostMapping("/publish-message")
    public String publishMessage(@RequestParam(required = true) Integer number) {
        for (int i = 0; i < number; ++i) {
            kafkaTemplate.send(Constant.TOPIC_3, "number " + i);
        }
        return "OK";
    }
}
