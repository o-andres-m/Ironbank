package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.transaction.TransactionDto;
import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.entities.Transaction;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.accounts.CreditCardAccount;
import com.ironhack.ironbank.model.entities.accounts.SavingAccount;
import com.ironhack.ironbank.model.enums.TransactionType;
import com.ironhack.ironbank.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AutomaticControllersTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminService adminService;


    @Test
    void applyInterestsCredit() throws Exception {

        var creditCardAccount = new CreditCardAccount();
        creditCardAccount.setNumber("ES0000000001");

        var transaction1 = new Transaction();
        transaction1.setAccount(creditCardAccount);
        transaction1.setTransactionType(TransactionType.INTERESTS);
        transaction1.setAmount(new Money(BigDecimal.valueOf(1)));
        transaction1.setObservations("Interests Applied");

        var transactionDtoList = new ArrayList<TransactionDto>();
        transactionDtoList.add(TransactionDto.fromTransaction(transaction1));

        when(adminService.applyInterestsCredit()).thenReturn(transactionDtoList);

        mockMvc.perform(put("/auto/interests/credit"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void applyInterestsSaving() throws Exception {

        var savingAccount = new SavingAccount();
        savingAccount.setNumber("ES0000000001");


        var transaction1 = new Transaction();
        transaction1.setAccount(savingAccount);
        transaction1.setTransactionType(TransactionType.INTERESTS);
        transaction1.setAmount(new Money(BigDecimal.valueOf(1)));
        transaction1.setObservations("Interests Applied");


        var transactionDtoList = new ArrayList<TransactionDto>();
        transactionDtoList.add(TransactionDto.fromTransaction(transaction1));

        when(adminService.applyInterestsCredit()).thenReturn(transactionDtoList);

        mockMvc.perform(put("/auto/interests/saving"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void applyMaintenance() throws Exception {

        var checkingAccount = new CheckingAccount();
        checkingAccount.setNumber("ES0000000001");


        var transaction1 = new Transaction();
        transaction1.setAccount(checkingAccount);
        transaction1.setTransactionType(TransactionType.MONTHLY_MAINTENANCE);
        transaction1.setAmount(new Money(BigDecimal.valueOf(1)));
        transaction1.setObservations("Interests Applied");


        var transactionDtoList = new ArrayList<TransactionDto>();
        transactionDtoList.add(TransactionDto.fromTransaction(transaction1));

        when(adminService.applyInterestsCredit()).thenReturn(transactionDtoList);

        mockMvc.perform(put("/auto/maintenance"))
                .andDo(print())
                .andExpect(status().isOk());

    }
}