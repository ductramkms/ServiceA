package com.example.ServiceA.controller;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import com.example.ServiceA.config.MyAuthorizedClient;
import com.example.ServiceA.util.ColorLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class HomeController {

    @Autowired
    private WebClient webClient;

    @Autowired
    private MyAuthorizedClient myAuthorizedClient;

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
    ColorLog.log("@ACCESS TOKEN: " + accessToken);

    return this.webClient
        .get()
        .uri("http://127.0.0.1:8081")
        .attributes(oauth2AuthorizedClient(authorizedClient))
        .retrieve()
        .bodyToMono(Object.class)
        .block();
  }
}
