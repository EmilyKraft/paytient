package com.example.paytient.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import org.springframework.stereotype.Component;

@Component
public class PaymentUtils {

    public double computeMatchAmount(double paymentAmount) {
        if (paymentAmount < 10) {
            return paymentAmount * .01;
        } else if (paymentAmount < 50) {
            return paymentAmount * .03;
        } else
            return paymentAmount * .05;
    }

    public LocalDate computeDueDate() {
        LocalDate date = LocalDate.now();
        LocalDate dueDate = date.plusDays(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        System.out.println(dueDate.format(formatter));
        if (DayOfWeek.SUNDAY.equals(dueDate.getDayOfWeek()) || DayOfWeek.SATURDAY.equals(dueDate.getDayOfWeek())) {
            LocalDate followingMonday = dueDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            System.out.println(followingMonday.format(formatter));
            return followingMonday;
        } else
            return dueDate;
    }
}
