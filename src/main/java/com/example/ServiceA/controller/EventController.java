package com.example.ServiceA.controller;

import com.example.ServiceA.payload.request.EventRequestBody;
import com.example.ServiceA.payload.response.EventResponseBody;
import com.example.ServiceA.util.ColorLog;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.TimeUnit;
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

  @PostMapping(value = "/item")
  @Timed(value = "controller.event.get.item")
  public EventResponseBody getEvent(@RequestBody EventRequestBody body) {
    EventResponseBody response = (EventResponseBody) producerTemplate.requestBody(body);
    log.debug(ColorLog.getLog("EventResponseBody from the camel router: " + response
        .toString()));
    return response;
  }

  @PostMapping(value = "/all")
  @Timed(value = "controller.event.all", description = "Controller Get All Events", histogram = true)
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

  @GetMapping(value = "/measure-with-timer-sample")
  public ResponseEntity<String> measureWithTimerSample() {
    Timer timer = Timer.builder("measure.with.timer.sample")
        .tag("demo_tag_key", "demo_tag_value")
        .description("Demo without using Timed Annotation")
        .register(meterRegistry);

    timer.record(() -> {
      try {
        Thread.sleep(1500);
      } catch (Exception e) {
        ColorLog.printStackTrace(e);
      }
    });

    ColorLog.log("Request time of event/measure-with-timer-sample: " + timer.max(
        TimeUnit.MILLISECONDS));

    return ResponseEntity.ok().body("Measure With Timer Sample; " + timer.max(
        TimeUnit.MILLISECONDS));
  }
}
