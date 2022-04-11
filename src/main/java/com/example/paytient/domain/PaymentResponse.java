package com.example.paytient.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PaymentResponse {

    private String updatedBalance;
    private LocalDate dueDate;
    private String notes;
    
}
