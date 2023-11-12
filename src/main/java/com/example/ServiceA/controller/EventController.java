package com.example.ServiceA.controller;

import com.example.ServiceA.payload.request.EventRequestBody;
import com.example.ServiceA.payload.response.EventResponseBody;
import com.example.ServiceA.util.ColorLog;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final MeterRegistry meterRegistry;

    public EventController(ProducerTemplate template, MeterRegistry meterRegistry) {
        this.producerTemplate = template;
        this.meterRegistry = meterRegistry;
    }

    @Timed(value = "controller.event.get.item")
    @PostMapping(value = "/item")
    public EventResponseBody getEvent(@RequestBody EventRequestBody body) {

        EventResponseBody response = (EventResponseBody) producerTemplate.requestBody(body);
        log.debug(ColorLog.getLog("EventResponseBody from the camel router: " + response
                .toString()));
        return response;
    }

    @Timed(value = "event.all")
    @PostMapping(value = "/all")
    public EventResponseBody all(@RequestBody EventRequestBody body) {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            ColorLog.printStackTrace(e);
        }

        EventResponseBody response = (EventResponseBody) producerTemplate.requestBody(body);
        log.debug(ColorLog.getLog("EventResponseBody from the camel router: " + response
                .toString()));
        return response;
    }

    @GetMapping(value = "/demo")
    public ResponseEntity<String> demo() {
        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            ColorLog.printStackTrace(e);
        }

        timer.stop(
                Timer.builder("demo")
                        .tag("demo_tag_key", "demo_tag_value")
                        .description("Demo without using Timed Annotation")
                        .register(meterRegistry)
        );

        return ResponseEntity.ok().body("Demo without using Timed Annotation");
    }
}
