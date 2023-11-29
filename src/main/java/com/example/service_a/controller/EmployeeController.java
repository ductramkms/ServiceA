package com.example.service_a.controller;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import com.example.service_a.config.MyAuthorizedClient;
import com.example.service_a.constant.Constant;
import com.example.service_a.payload.request.KafkaRequestBody;
import com.example.service_a.payload.response.CamelResponseBody;
import com.example.service_a.util.Helper;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RestController
@RequestMapping(path = "employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

  private final MyAuthorizedClient authorizedClient;

  private final WebClient webClient;

  @Value("${app.service_b.base_url}")
  private String baseUrl;

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final MeterRegistry meterRegistry;

  public EmployeeController(MyAuthorizedClient authorizedClient, WebClient webClient,
      ProducerTemplate producerTemplate, KafkaTemplate<String, String> kafkaTemplate,
      MeterRegistry meterRegistry) {
    this.authorizedClient = authorizedClient;
    this.webClient = webClient;
    this.kafkaTemplate = kafkaTemplate;
    this.meterRegistry = meterRegistry;
  }

  /**
   * This function return the common type of result of each API.
   *
   * @return response entity
   */
  private ResponseEntity<CamelResponseBody> result(String data) {
    CamelResponseBody body = Helper.jsonDeserialize(data, CamelResponseBody.class);
    return ResponseEntity.status(HttpStatus.valueOf(body.getStatus())).body(body);
  }

  @GetMapping
  @Timed(value = "controller.employee.get.all")
  public ResponseEntity<CamelResponseBody> all() {
    authorizedClient.getAuthorizedClient().getAccessToken().toString();
    Timer timer = Timer.builder("controller.employee.get.all").register(meterRegistry);

    ResponseEntity<CamelResponseBody> body = timer.record(
        () -> {
          String data = this.webClient
              .get()
              .uri(baseUrl + "/employees")
              .attributes(oauth2AuthorizedClient(authorizedClient
                  .getAuthorizedClient()))
              .retrieve()
              .bodyToMono(String.class)
              .block();
          return result(data);
        });

    log.info("Get all employees with time request = " + timer.max(
        TimeUnit.MILLISECONDS));

    return body;
  }

  @GetMapping("/{id}")
  @Timed(value = "controller.employee.get.by.id")
  public ResponseEntity<CamelResponseBody> getById(@PathVariable Integer id) {
    Timer timer = Timer.builder("controller.employee.get.by.id").register(meterRegistry);

    ResponseEntity<CamelResponseBody> body = timer.record(
        () -> {
          String data = this.webClient
              .get()
              .uri(baseUrl + "/employees/" + id)
              .attributes(oauth2AuthorizedClient(authorizedClient
                  .getAuthorizedClient()))
              .retrieve()
              .bodyToMono(String.class)
              .block();

          return result(data);
        });

    log.info("Get employee by id = " + id + " & request time = " + timer.max(
        TimeUnit.MILLISECONDS));
    return body;
  }

  @PostMapping
  @Timed(value = "controller.employee.create")
  public ResponseEntity<CamelResponseBody> create(@RequestBody String value) {
    Timer timer = Timer.builder("controller.employee.create").register(meterRegistry);

    ResponseEntity<CamelResponseBody> body = timer.record(
        () -> {
          String data = this.webClient
              .post()
              .uri(baseUrl + "/employees/")
              .attributes(oauth2AuthorizedClient(authorizedClient
                  .getAuthorizedClient())).contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .bodyValue(value)
              .retrieve()
              .bodyToMono(String.class)
              .block();
          return result(data);
        });

    log.info("Create new employee & request time = " + timer.max(
        TimeUnit.MILLISECONDS));
    return body;
  }

  @PutMapping
  @Counted(value = "kafka.producer.employee.update", description = "Kafka send message to update employee ")
  public ResponseEntity<CamelResponseBody> update(@RequestBody String value) {
    Timer timer = Timer.builder("controller.employee.update").register(meterRegistry);

    timer.record(() -> {
      KafkaRequestBody message = new KafkaRequestBody(Constant.UPDATE_EMPLOYEE, value);
      kafkaTemplate.send(Constant.TOPIC_1, Helper.jsonSerialize(message));
    });

    log.info("Update new employee & request time = " + timer.max(
        TimeUnit.MILLISECONDS));

    return ResponseEntity.accepted().body(CamelResponseBody.builder().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<CamelResponseBody> delete(@PathVariable Integer id) {
    Timer timer = Timer.builder("controller.employee.delete").register(meterRegistry);

    timer.record(() -> {
      KafkaRequestBody message = new KafkaRequestBody(Constant.DELETE_EMPLOYEE, "" + id);
      kafkaTemplate.send(Constant.TOPIC_1, Helper.jsonSerialize(message));
    });

    log.info("Delete employee " + id + " & request time = " + timer.max(
        TimeUnit.MILLISECONDS));
    return ResponseEntity.accepted().body(CamelResponseBody.builder().build());
  }
}
