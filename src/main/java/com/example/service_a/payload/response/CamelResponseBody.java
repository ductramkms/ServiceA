package com.example.service_a.payload.response;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CamelResponseBody {

  private String status;
  private String message;
  private Object data;

  public static CamelResponseBody fromJson(String src) {
    Gson gson = new Gson();
    return gson.fromJson(src, CamelResponseBody.class);
  }
}
