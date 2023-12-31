package com.example.service_a.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .authorizeRequests().mvcMatchers("/rest-template-test/**").permitAll()
        .and()
        .authorizeRequests().mvcMatchers("/actuator/**").permitAll();

    http
        .authorizeRequests(authorizeRequests -> authorizeRequests
            .anyRequest()
            .authenticated())
        .oauth2Login(oauth2Login -> oauth2Login.loginPage(
            "/oauth2/authorization/service-a-client-oidc"))
        .oauth2Client(withDefaults());

    return http.build();
  }
}
