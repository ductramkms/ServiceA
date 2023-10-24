package com.example.ServiceA.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.ServiceA.ServiceAApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = ServiceAApplication.class)
@AutoConfigureMockMvc
class EventControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Test
  void getEvent() throws Exception{
    String body = "{\n"
        + "    \"message\": \"Hello World!\"\n"
        + "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(
        MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }
}