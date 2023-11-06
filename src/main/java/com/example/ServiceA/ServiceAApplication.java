package com.example.ServiceA;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceAApplication implements CommandLineRunner {
  public static void main(String[] args) {
    SpringApplication.run(ServiceAApplication.class, args);
  }


  @Override
  public void run(String... args) throws Exception {
    DefaultCamelContext context = new DefaultCamelContext();
  }
}
