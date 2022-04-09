package com.example.paytient.controllers;

import com.example.paytient.domain.PaymentResponse;
import com.example.paytient.services.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/")
    public String index() {
        return "Welcome to Paytient";
    }

    @PostMapping(value = "/one-time-payment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity oneTimePayment(@RequestBody double paymentAmount) {
        if (paymentAmount < 0) {
            return new ResponseEntity("Payment must be greater than $0.00", HttpStatus.BAD_REQUEST);
        }
        try {
            PaymentResponse response = paymentService.performOneTimePayment(paymentAmount);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}