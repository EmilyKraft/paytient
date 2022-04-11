package com.example.paytient.services;

import com.example.paytient.domain.PaymentRequest;
import com.example.paytient.domain.PaymentResponse;

public interface PaymentService {

    public PaymentResponse performOneTimePayment(PaymentRequest paymentAmount);
    
}
