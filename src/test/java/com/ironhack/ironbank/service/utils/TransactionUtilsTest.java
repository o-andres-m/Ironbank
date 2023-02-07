package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.model.defaults.PenaltyFee;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.accounts.CreditCardAccount;
import com.ironhack.ironbank.model.enums.TransactionType;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class TransactionUtilsTest {

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    @AfterEach
    void tearDown(){
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void registerDeposit() {
        var account = new CheckingAccount();
        account.setNumber("ES000001");
        accountRepository.save(account);

        var transaction = transactionUtils.registerDeposit(account, BigDecimal.valueOf(100));
        assertEquals(String.valueOf(transaction.getAmount()),"€ 100.00");
        assertEquals(transaction.getAccount(),account);
        assertEquals(transaction.getObservations(), "Owner Deposit");
    }

    @Test
    void registerAdminDeposit() {
        var account = new CheckingAccount();
        account.setNumber("ES000001");
        accountRepository.save(account);

        var transaction = transactionUtils.registerAdminDeposit(account, BigDecimal.valueOf(100),"admin");

        assertEquals(transaction.getTransactionType(), TransactionType.DEPOSIT);
        assertEquals(transaction.getObservations(), "Admin balance adjust(DEPOSIT). User: admin");
    }

    @Test
    void registerWithdraw() {
        var account = new CheckingAccount();
        account.setNumber("ES000001");
        accountRepository.save(account);

        var transaction = transactionUtils.registerWithdraw(account, BigDecimal.valueOf(100));

        assertEquals(transaction.getTransactionType(), TransactionType.WITHDRAW);
        assertEquals(String.valueOf(transaction.getAmount()),"€ -100.00");
    }

    @Test
    void registerCreditBuy() {
        var account = new CreditCardAccount();
        account.setNumber("ES0000011");
        accountRepository.save(account);

        var transaction = transactionUtils.registerCreditBuy(account, BigDecimal.valueOf(100),"Supermarket");

        assertEquals(transaction.getTransactionType(), TransactionType.PAY_WITH_CREDIT_CARD);
        assertEquals(transaction.getObservations(),"Payment at Supermarket");
    }

    @Test
    void registerTransferToAnotherAccount() {

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        accountRepository.save(account);

        var transaction = transactionUtils.registerTransferToAnotherAccount(account, BigDecimal.valueOf(100),"AR000001");

        assertEquals(transaction.getTransactionType(), TransactionType.TRANSFER);
        assertEquals(transaction.getObservations(), "Transfer to AR000001");
        assertEquals(transaction.getAmount().getAmount().doubleValue(), -100.0);
    }

    @Test
    void registerPenalty() {

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setPenaltyFee(new PenaltyFee(BigDecimal.valueOf(33)));
        accountRepository.save(account);

        var transaction = transactionUtils.registerPenalty(account, "AUTO");

        assertEquals(transaction.getTransactionType(), TransactionType.PENALTY);
        assertEquals(transaction.getObservations(), "PenaltyFee applied by AUTO");
        assertEquals(transaction.getAmount().getAmount().doubleValue(), -33.0);
    }

    @Test
    void registerMonthlyMaintenance() {

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setMontlyMaintenance(BigDecimal.valueOf(12));
        accountRepository.save(account);

        var transaction = transactionUtils.registerMonthlyMaintenance(account, account.getMontlyMaintenance());

        assertEquals(transaction.getTransactionType(), TransactionType.MONTHLY_MAINTENANCE);
        assertEquals(transaction.getAmount().getAmount().doubleValue(), -12.0);
    }

}