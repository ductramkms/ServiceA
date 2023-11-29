package com.example.service_a.config;

import lombok.Data;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Component;

@Data
@Component
public class MyAuthorizedClient {

  private OAuth2AuthorizedClient authorizedClient;
}
