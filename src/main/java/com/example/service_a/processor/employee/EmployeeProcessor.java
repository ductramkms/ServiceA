package com.example.service_a.processor.employee;

import com.example.service_a.payload.response.CamelResponseBody;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class EmployeeProcessor implements Processor {

  @Timed(value = "processor.employee", description = "Processor Of Employee")
  @Override
  public void process(Exchange exchange) {
    log.info("Employee Processor: exchange data");
    String result = exchange.getIn().getBody(String.class);
    CamelResponseBody body = CamelResponseBody.fromJson(result);
    exchange.getMessage().setBody(body);
  }
}
