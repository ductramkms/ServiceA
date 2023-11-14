package com.example.ServiceA.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SampleObject {

  private int id;
  private String name;
  private Integer old;
  private Double salary;
}
