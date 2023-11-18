package com.example.ServiceA.controller;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import com.example.ServiceA.config.MyAuthorizedClient;
import com.example.ServiceA.constant.Constant;
import com.example.ServiceA.payload.request.KafkaRequestBody;
import com.example.ServiceA.payload.response.CamelResponseBody;
import com.example.ServiceA.util.ColorLog;
import com.example.ServiceA.util.Helper;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MyAuthorizedClient authorizedClient;

    @Autowired
    private WebClient webClient;

    @Value("${app.service_b.base_url}")
    private String baseUrl;

    @Produce
    private final ProducerTemplate producerTemplate;

    private final MeterRegistry meterRegistry;

    public EmployeeController(ProducerTemplate template, MeterRegistry meterRegistry) {
        this.producerTemplate = template;
        this.meterRegistry = meterRegistry;
    }

    /**
     * This function return the common type of result of each API.
     *
     * @param body common type of content body
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

        log.info(ColorLog.getLog("Get all employees with time request = " + timer.max(
                TimeUnit.MILLISECONDS)));

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

        log.info(ColorLog.getLog("Get employee by id = " + id + " & request time = " + timer.max(
                TimeUnit.MILLISECONDS)));
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

        log.info(ColorLog.getLog("Create new employee & request time = " + timer.max(
                TimeUnit.MILLISECONDS)));
        return body;
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PutMapping
    @Counted(value = "kafka.producer.employee.update", description = "Kafka send message to update employee ")
    public ResponseEntity<CamelResponseBody> update(@RequestBody String value) {
        Timer timer = Timer.builder("controller.employee.update").register(meterRegistry);

        timer.record(() -> {
            KafkaRequestBody message = new KafkaRequestBody(Constant.UPDATE_EMPLOYEE, value);
            kafkaTemplate.send(Constant.TOPIC_1, Helper.jsonSerialize(message));
        });

        log.info(ColorLog.getLog("Update new employee & request time = " + timer.max(
                TimeUnit.MILLISECONDS)));

        return ResponseEntity.accepted().body(CamelResponseBody.builder().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CamelResponseBody> delete(@PathVariable Integer id) {
        Timer timer = Timer.builder("controller.employee.delete").register(meterRegistry);

        timer.record(() -> {
            KafkaRequestBody message = new KafkaRequestBody(Constant.DELETE_EMPLOYEE, "" + id);
            kafkaTemplate.send(Constant.TOPIC_1, Helper.jsonSerialize(message));
        });

        log.info(ColorLog.getLog("Delete employee " + id + " & request time = " + timer.max(
                TimeUnit.MILLISECONDS)));
        return ResponseEntity.accepted().body(CamelResponseBody.builder().build());
    }
}
