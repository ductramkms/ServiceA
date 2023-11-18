package com.example.ServiceA.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.ServiceA.util.ColorLog;

@RestController
@RequestMapping(path = "/rest-template-test", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestTemplateController {

    @GetMapping("/get")
    public String get() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://172.22.1.3:8081/actuator/prometheus";

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        ColorLog.log("@DUKE: " + responseEntity);
        return "GET";
    }
}
