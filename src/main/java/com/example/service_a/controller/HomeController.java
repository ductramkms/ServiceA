package com.example.service_a.controller;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import com.example.service_a.config.MyAuthorizedClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@Slf4j
public class HomeController {

  private final WebClient webClient;
  private final MyAuthorizedClient myAuthorizedClient;

  public HomeController(WebClient webClient, MyAuthorizedClient myAuthorizedClient) {
    this.webClient = webClient;
    this.myAuthorizedClient = myAuthorizedClient;
  }

  @Value("${app.service_b.base_url}")
  private String baseUrl;

  @ModelAttribute("authorizedClient")
  public OAuth2AuthorizedClient getAuthorizedClient(
      @RegisteredOAuth2AuthorizedClient("service-a-client-authorization-code") OAuth2AuthorizedClient authorizedClient) {
    return authorizedClient;
  }

  @ResponseBody
  @GetMapping
  public Object home(
      @ModelAttribute("authorizedClient") OAuth2AuthorizedClient authorizedClient) {

    myAuthorizedClient.setAuthorizedClient(authorizedClient);

    String accessToken = authorizedClient.getAccessToken().getTokenValue();

    authorizedClient.getPrincipalName();
    Object object = this.webClient
        .get()
        .uri(baseUrl)
        .attributes(oauth2AuthorizedClient(authorizedClient))
        .retrieve()
        .bodyToMono(Object.class)
        .block();

    log.info("@RETURN OBJECT: " + object);
    log.info("@ACCESS TOKEN: " + accessToken);
    return accessToken;
  }
}
