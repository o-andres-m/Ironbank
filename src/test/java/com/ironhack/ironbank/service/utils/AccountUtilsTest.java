package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.defaults.PenaltyFee;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountUtilsTest {

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;



    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        accountRepository.deleteAll();
    }


    @Test
    void findCheckingAccountByAccountHolder() {
        var user = new AccountHolder();
        user = userRepository.save(user);

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setStatus(Status.ACTIVE);
        account.setPrimaryOwner(user);
        accountRepository.save(account);

        var user2 = new AccountHolder();
        user2 = userRepository.save(user2);

        var accountFound = accountUtils.findCheckingAccountByAccountHolder(user);

        assertEquals("ES000001", accountFound.getNumber());

        AccountHolder finalUser = user2;
        assertThrows(EspecificException.class, ()-> accountUtils.findCheckingAccountByAccountHolder(finalUser));
    }

    @Test
    void checkFinalBalance() {

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setStatus(Status.ACTIVE);
        account.setBalance(new Money(BigDecimal.valueOf(2000)));
        accountRepository.save(account);

        assertThrows(EspecificException.class, ()-> accountUtils.checkFinalBalance(account,BigDecimal.valueOf(3000)));
    }

    @Test
    void getAndVerifyAccount() {

        var user = new AccountHolder();
        user = userRepository.save(user);

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setStatus(Status.ACTIVE);
        account.setPrimaryOwner(user);
        account.setBalance(new Money(BigDecimal.valueOf(2000)));
        accountRepository.save(account);

        var user2 = new AccountHolder();
        user2 = userRepository.save(user2);

        AccountHolder finalUser = user2;
        assertThrows(EspecificException.class, ()->accountUtils.getAndVerifyAccount("ES000001", finalUser));
    }

    @Test
    void getAccountByNumber() {

        var user = new AccountHolder();
        user = userRepository.save(user);

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setStatus(Status.ACTIVE);
        account.setPrimaryOwner(user);
        accountRepository.save(account);

        assertEquals("ES000001", accountUtils.getAccountByNumber("ES000001").getNumber());
        assertThrows(EspecificException.class, ()-> accountUtils.getAccountByNumber("AAA"));
    }

    @Test
    void checkMinimumBalance() {
        var user = new AccountHolder();
        user = userRepository.save(user);

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setStatus(Status.ACTIVE);
        account.setPrimaryOwner(user);
        account.setBalance(new Money(BigDecimal.valueOf(2000)));
        account.setPenaltyFee(new PenaltyFee(BigDecimal.valueOf(50)));
        accountRepository.save(account);

        assertThrows(EspecificException.class, ()-> accountUtils.checkMinimumBalance(account,BigDecimal.valueOf(1990)));
    }

    @Test
    void checkFinalBalanceZero() {

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setBalance(new Money(BigDecimal.valueOf(2000)));
        accountRepository.save(account);

        assertTrue(accountUtils.checkFinalBalanceZero(account,BigDecimal.valueOf(2000)));
        assertFalse(accountUtils.checkFinalBalanceZero(account,BigDecimal.valueOf(1600)));
    }

    @Test
    void checkAccountNotFreezed() {

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setStatus(Status.FREEZE);
        account.setBalance(new Money(BigDecimal.valueOf(2000)));
        accountRepository.save(account);

        assertThrows(EspecificException.class, ()-> accountUtils.checkAccountNotFreezed(account));
    }


    @Test
    void verifyAccountAndSecretKey() {

        var account = new CheckingAccount();
        account.setNumber("ES000001");
        account.setStatus(Status.FREEZE);
        account.setSecretKey("ABC123-ABC123-ABC123");
        accountRepository.save(account);

        assertThrows(EspecificException.class, ()-> accountUtils.verifyAccountAndSecretKey(account, "AAA BBB CCC"));
    }

}