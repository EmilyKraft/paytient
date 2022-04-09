package com.example.paytient.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PaymentResponse {

    private double updatedBalance;
    private LocalDate dueDate;
    
}
