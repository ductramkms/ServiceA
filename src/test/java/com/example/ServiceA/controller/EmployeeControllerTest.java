package com.example.ServiceA.controller;

import com.example.ServiceA.ServiceAApplication;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ServiceAApplication.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void testAll() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/employee").contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON));
  }

  @Test
  void getById_ExistedId_OK() throws Exception {
    int id = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/employee/" + id).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON));
  }

  @Test
  void getById_UnExistedId_NotFound() throws Exception {
    int id = 100;
    mockMvc.perform(MockMvcRequestBuilders.get("/employee/" + id).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON));
  }

  @Test
  void create_ValidData_Created() throws Exception {
    Random random = new Random();
    int randomId = Math.abs(random.nextInt());

    String body = "{\n"
        + "    \"empId\": " + randomId + ",\n"
        + "    \"name\": \"Duy\",\n"
        + "    \"department\": \"Software\",\n"
        + "    \"salary\": 3.0\n"
        + "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/employee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  void create_ExistedEmployee_Created() throws Exception {
    String body = "{\n"
        + "    \"empId\": 1,\n"
        + "    \"name\": \"Duy\",\n"
        + "    \"department\": \"Software\",\n"
        + "    \"salary\": 3.0\n"
        + "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/employee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  void create_InvalidData_BadRequest() throws Exception {
    String body = "{\n"
        + "    \"empId\": -1,\n"
        + "    \"name\": \"Duy\",\n"
        + "    \"department\": \"Software\",\n"
        + "    \"salary\": 3.0\n"
        + "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/employee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  void testGetById() {

  }
}
