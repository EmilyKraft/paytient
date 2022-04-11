package com.example.paytient.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PaymentUtilsTest {

    @Autowired
    private PaymentUtils paymentUtils;

    @Test
    public void computeDueDate_Success() {
        LocalDate now = LocalDate.now();
        LocalDate response = paymentUtils.computeDueDate();

        Assertions.assertEquals(response.minusDays(15), now);
    }

    @Test
    public void computeMatchAmount_TierOne() {
        double response = paymentUtils.computeMatchAmount(9);

        Assertions.assertEquals(.09, response);
    }

    @Test
    public void computeMatchAmount_TierTwo() {
        double response = paymentUtils.computeMatchAmount(10);

        Assertions.assertEquals(.30, response);
    }

    @Test
    public void computeMatchAmount_TierThree() {
        double response = paymentUtils.computeMatchAmount(50);

        Assertions.assertEquals(2.50, response);
    }
}
