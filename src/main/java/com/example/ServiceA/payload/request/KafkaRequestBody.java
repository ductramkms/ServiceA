package com.example.ServiceA.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class KafkaRequestBody {

    String requestType;
    String data;
}
