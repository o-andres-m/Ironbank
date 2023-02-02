package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class FraudDetectionUtils {

    private final TransactionRepository transactionRepository;


    public boolean verifyRecurrentOperations(Account account) {
        var lastTransaction = transactionRepository.findLastTransactionByAccountId(account.getId());
        var duration = Duration.between(lastTransaction.getDate(),Instant.now());
        return duration.getSeconds() < 1;
    }

    public void verifyExpensiveOperation(CheckingAccount checkingAccount, BigDecimal amount) {

    }
}
