package com.example.service_a.controller;

import com.example.service_a.ServiceAApplication;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
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
  private MockMvc mockMvc;
  private ClientAndServer mockServer;
  private MockServerClient mockServerClient;

  @BeforeEach
  public void setup() {
    mockServer = ClientAndServer.startClientAndServer(8081);
    mockServerClient = new MockServerClient("127.0.0.1", 8081);
  }

  @AfterEach
  public void tearDown() {
    mockServer.stop();
    mockServerClient.stop();
  }

  @Test
  void testAll() throws Exception {
    String resBody = "{\"status\":\"OK\",\"message\":\"Get list employees success!\",\"data\":{\"total\":10,\"employees\":[{\"empId\":1,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0},{\"empId\":11,\"name\":\"Nguyen Van B\",\"department\":\"QC Engineer\",\"salary\":5.0},{\"empId\":12,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0},{\"empId\":1302227978,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0},{\"empId\":1465734802,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0},{\"empId\":-1,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0},{\"empId\":2065949153,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0},{\"empId\":789696773,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0},{\"empId\":1429094995,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0},{\"empId\":91,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0}]}}";

    mockServerClient.when(HttpRequest.request()
                .withMethod("GET")
                .withPath("/employees"),
            Times.exactly(1))
        .respond(
            HttpResponse.response()
                .withStatusCode(HttpStatus.SC_OK)
                .withHeaders(
                    new Header("Content-Type",
                        "application/json; charset=utf-8"))
                .withBody(resBody)
                .withDelay(TimeUnit.SECONDS, 1));

    mockMvc.perform(MockMvcRequestBuilders.get("/employees").contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content()
                .contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON));
  }

  @Test
  void getById_ExistedId_OK() throws Exception {
    int id = 1;

    String resBody = "{\"status\":\"OK\",\"message\":\"Get employee 1 success!\",\"data\":{\"empId\":1,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0}}";

    mockServerClient.when(HttpRequest.request()
                .withMethod("GET")
                .withPath("/employees/" + id),
            Times.exactly(1))
        .respond(
            HttpResponse.response()
                .withStatusCode(HttpStatus.SC_OK)
                .withHeaders(
                    new Header("Content-Type",
                        "application/json; charset=utf-8"))
                .withBody(resBody)
                .withDelay(TimeUnit.SECONDS, 1));

    mockMvc.perform(MockMvcRequestBuilders.get("/employees/" + id).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content()
                .contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON));
  }

  @Test
  void getById_UnExistedId_NotFound() throws Exception {
    int id = 100;
    String resBody = "{\"status\":\"NOT_FOUND\",\"message\":\"Can't find the employee with id = "
        + id + "\",\"data\":null}";

    mockServerClient.when(
            HttpRequest.request()
                .withMethod("GET")
                .withPath("/employees/" + id)
        )
        .respond(
            HttpResponse.response()
                .withStatusCode(HttpStatus.SC_NOT_FOUND)
                .withHeaders(
                    new Header("Content-Type",
                        "application/json; charset=utf-8")
                )
                .withBody(resBody)
                .withDelay(TimeUnit.SECONDS, 1)
        );

    mockMvc.perform(MockMvcRequestBuilders.get("/employees/" + id).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            MockMvcResultMatchers.content()
                .contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON));
  }

  @Test
  void create_ValidData_Created() throws Exception {
    String reqBody = "{\"empId\":12,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0}";

    String resBody = "{\"status\":\"CREATED\",\"message\":\"Create employee success!\",\"data\":{\"empId\":12,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0}}";

    mockServerClient.when(HttpRequest.request()
                .withMethod("POST")
                .withPath("/employees")
                .withHeader("Content-type", "application/json")
                .withBody(reqBody),
            Times.exactly(1))
        .respond(
            HttpResponse.response()
                .withStatusCode(HttpStatus.SC_CREATED)
                .withHeaders(
                    new Header("Content-Type",
                        "application/json; charset=utf-8"))
                .withBody(resBody)
                .withDelay(TimeUnit.SECONDS, 1));

    mockMvc.perform(MockMvcRequestBuilders.post("/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(reqBody)
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.content()
                .contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON));
  }

  @Test
  @Order(2)
  public void create_ExistedEmployee_Created() throws Exception {
    String reqBody = "{\"empId\":12,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0}";

    String resBody = "{\"status\":\"CONFLICT\",\"message\":\"The employee with id = 12 has already existed\",\"data\":null}";

    mockServerClient.when(HttpRequest.request()
                .withMethod("POST")
                .withPath("/employees")
                .withHeader("Content-type", "application/json")
                .withBody(reqBody),
            Times.exactly(1))
        .respond(
            HttpResponse.response()
                .withStatusCode(HttpStatus.SC_CONFLICT)
                .withHeaders(
                    new Header("Content-Type",
                        "application/json; charset=utf-8"))
                .withBody(resBody)
                .withDelay(TimeUnit.SECONDS, 1));

    mockMvc.perform(MockMvcRequestBuilders.post("/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(reqBody)
        )
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(
            MockMvcResultMatchers.content()
                .contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON));
  }

  @Test
  @Order(1)
  void create_InvalidData_BadRequest() throws Exception {
    String reqBody = "{\"empId\":-12,\"name\":\"Duy\",\"department\":\"Software\",\"salary\":3.0}";
    String resBody = "{\"status\":\"BAD_REQUEST\",\"message\":\"The id of employee cannot be negative, but we got id = -12\",\"data\":null}";

    mockServerClient.when(HttpRequest.request()
                .withMethod("POST")
                .withPath("/employees")
                .withHeader("Content-type", "application/json")
                .withBody(reqBody),
            Times.exactly(1))
        .respond(
            HttpResponse.response()
                .withStatusCode(HttpStatus.SC_BAD_REQUEST)
                .withHeaders(
                    new Header("Content-Type",
                        "application/json; charset=utf-8"))
                .withBody(resBody)
                .withDelay(TimeUnit.SECONDS, 1));

    mockMvc.perform(MockMvcRequestBuilders.post("/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(reqBody)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.content()
                .contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON));
  }
}
