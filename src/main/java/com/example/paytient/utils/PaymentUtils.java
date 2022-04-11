package com.example.paytient.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.springframework.stereotype.Component;

@Component
public class PaymentUtils {

    public double computeMatchAmount(double paymentAmount) {
        double matchAmount;
        if (paymentAmount < 10) {
            matchAmount = paymentAmount * .01;
        } else if (paymentAmount < 50) {
            matchAmount = paymentAmount * .03;
        } else {
            matchAmount = paymentAmount * .05;
        }
        double roundedMatch = Math.round(matchAmount * 100.0) / 100.0;
        return roundedMatch;
    }

    public LocalDate computeDueDate() {
        LocalDate date = LocalDate.now();
        LocalDate dueDate = date.plusDays(15);
        if (DayOfWeek.SUNDAY.equals(dueDate.getDayOfWeek()) || DayOfWeek.SATURDAY.equals(dueDate.getDayOfWeek())) {
            LocalDate followingMonday = dueDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            return followingMonday;
        } else
            return dueDate;
    }
}
