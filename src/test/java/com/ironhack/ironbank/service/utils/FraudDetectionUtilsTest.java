package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.entities.Transaction;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FraudDetectionUtilsTest {

    @Autowired
    private FraudDetectionUtils fraudDetectionUtils;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    void verifyRecurrentOperations() {
        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setStatus(Status.ACTIVE);
        accountRepository.save(account);

        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setDate(Instant.parse("2018-11-30T18:35:24.00Z"));
        transactionRepository.save(transaction);

        assertThrows(EspecificException.class, ()->fraudDetectionUtils.verifyRecurrentOperations(account));

        var accountUpdate = accountRepository.findAccountByNumber("ES000001");
        assertEquals(accountUpdate.get().getStatus(), Status.FREEZE);
    }

    @Test
    void verifyExpensiveOperation() {
    }
}