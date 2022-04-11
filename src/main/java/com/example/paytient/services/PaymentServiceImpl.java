package com.example.paytient.services;

import com.example.paytient.domain.Account;
import com.example.paytient.domain.PaymentRequest;
import com.example.paytient.domain.PaymentResponse;
import com.example.paytient.repositories.AccountRepository;
import com.example.paytient.utils.PaymentUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentUtils paymentUtils;

    @Autowired
    private AccountRepository accountRepository;

    @Value("${features.payment-matching}")
    private boolean paymentFlag;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    // throwaway accounts for mock data
    private Account account1 = new Account("1", 100, LocalDate.now());
    private Account account2 = new Account("2", 500, LocalDate.now());

    // throwaway function to reset account for testing purposes
    public Account getAccount1() {
        return account1;
    }

    // throwaway function to mock repository
    Account getAccount(String id) {
        if (id.equals("1")) {
            return account1;
        } else if (id.equals("2")) {
            return account2;
        }
        else return new Account();
    }

    @Override
    public PaymentResponse performOneTimePayment(PaymentRequest request) {
        /* if the database were set up, we'd use something like this to grab the account
        accountRepository.findAccountById(request.getUserId());
         */
        Account account = getAccount(request.getUserId());
        PaymentResponse response = new PaymentResponse();
        LocalDate newDueDate = paymentUtils.computeDueDate();
        response.setDueDate(newDueDate);
        account.setDueDate(newDueDate);
        double balanceAfterPayment = account.getBalance() - request.getPaymentAmount();
        if (balanceAfterPayment < 0) {
            // would need to business to decide how this should be handled. for now, will just set balance to 0 and set note.
            response.setNotes("Payment exceeded balance due by: $" + df.format(balanceAfterPayment * -1));
            balanceAfterPayment = 0;
        }
        if (paymentFlag && balanceAfterPayment > 0) {
            balanceAfterPayment -= paymentUtils.computeMatchAmount(request.getPaymentAmount());
        }
        response.setUpdatedBalance(df.format(balanceAfterPayment));
        account.setBalance(balanceAfterPayment);
        /* if the database were set up, you'd update it here
        accountRepository.save(account);
         */
        return response;
    }
}
