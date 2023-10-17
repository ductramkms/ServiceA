package com.example.ServiceA.controller;

import com.example.ServiceA.payload.request.EventRequestBody;
import com.example.ServiceA.payload.response.EventResponseBody;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    @GetMapping(value = "/get-name")
    public  String getName() {
        return "MY NAME IS DUC";
    }

    @Produce("direct:event")
    private  final ProducerTemplate producerTemplate;

    public HomeController(ProducerTemplate template){
        this.producerTemplate = template;
    }

    @PostMapping(value = "/event")
    public EventResponseBody getEvent(@RequestBody EventRequestBody body) {
        EventResponseBody response = (EventResponseBody) producerTemplate.requestBody(body);
        System.out.println(response.toString());
        return response;
    }
}
