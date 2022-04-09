package com.example.paytient.services;

import com.example.paytient.domain.PaymentResponse;
import com.example.paytient.utils.PaymentUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentUtils paymentUtils;

    @Value("${features.payment-matching}")
    private boolean paymentFlag;

    @Override
    public PaymentResponse performOneTimePayment(double paymentAmount) {
        PaymentResponse response = new PaymentResponse();
        response.setDueDate(paymentUtils.computeDueDate());
        double customerPaidAmount = 100 - paymentAmount;
        if (paymentFlag) {
            customerPaidAmount -= paymentUtils.computeMatchAmount(paymentAmount);
        }
        response.setUpdatedBalance(customerPaidAmount);
        return response;
    }
}
