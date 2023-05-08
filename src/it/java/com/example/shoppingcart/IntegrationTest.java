package com.example.shoppingcart;

import com.example.shoppingcart.Main;
import kalix.springsdk.testkit.KalixIntegrationTestKitSupport;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * This is a skeleton for implementing integration tests for a Kalix application built with the Spring SDK.
 *
 * This test will initiate a Kalix Proxy using testcontainers and therefore it's required to have Docker installed
 * on your machine. This test will also start your Spring Boot application.
 *
 * Since this is an integration tests, it interacts with the application using a WebClient
 * (already configured and provided automatically through injection).
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class IntegrationTest extends KalixIntegrationTestKitSupport {

  @Autowired
  private WebClient webClient;

  private Duration timeout = Duration.of(5, ChronoUnit.SECONDS);

  @Test
  public void happyPath() throws Exception {
    var cartId = UUID.randomUUID().toString();
    var item = new Model.Item("prodId1", 1);

    var res = webClient.post()
                    .uri("/shoppingcart/%s/add-item".formatted(cartId))
                    .bodyValue(new Model.AddItemRequest(item))
                    .retrieve()
                    .toEntity(String.class)
                    .block(timeout);

    assertEquals(HttpStatus.OK,res.getStatusCode());

    res = webClient.post()
                    .uri("/shoppingcart/%s/checkout".formatted(cartId))
                    .retrieve()
                    .toEntity(String.class)
                    .block(timeout);

    var get = webClient.get()
                .uri("/shoppingcart/%s".formatted(cartId))
                .retrieve()
                .bodyToMono(Model.Cart.class)
                .block(timeout);
    assertTrue(get.checkedOut());

  }
}