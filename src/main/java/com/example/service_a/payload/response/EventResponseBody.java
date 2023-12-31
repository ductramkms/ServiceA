package com.example.service_a.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseBody {

  private String id;
  private String message;
  private String exchangeId;
}
