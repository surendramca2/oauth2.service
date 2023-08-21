package com.surendra.oauth.server.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentCircuitBreakerController {

    @GetMapping("/payment")
    public String doPayment(){

  return "paymentController";
    }
}
