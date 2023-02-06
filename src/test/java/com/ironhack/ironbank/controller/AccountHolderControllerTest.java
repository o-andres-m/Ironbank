package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.account.AccountDto;
import com.ironhack.ironbank.dto.info.AccountHolderInfoDto;
import com.ironhack.ironbank.dto.transaction.TransactionDto;
import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.entities.Transaction;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.accounts.CreditCardAccount;
import com.ironhack.ironbank.model.entities.accounts.SavingAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.model.enums.TransactionType;
import com.ironhack.ironbank.service.HoldersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AccountHolderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @MockBean
    HoldersService holdersService;


    @Test
    void register() throws Exception {
        var accountHolder = new AccountHolder();
        accountHolder.setUsername("user");
        accountHolder.setPassword("user");
        accountHolder.setNif("A12121212");
        accountHolder.setFirstName("FirstName");
        accountHolder.setLastName("LastName");
        accountHolder.setDateOfBirth(LocalDate.parse("1990-01-01"));
        accountHolder.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));

        var accountHolderDto = AccountHolderDto.fromAccountHolder(accountHolder);
        var accountHolderDtoResponse = AccountHolderDtoResponse.fromAccountHolder(accountHolder);

        when(holdersService.register(accountHolderDto)).thenReturn(accountHolderDtoResponse);

        mockMvc.perform(post("/holders/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(accountHolderDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("1990-01-01"));
    }

    @Test
    void viewPersonalInfo() throws Exception {
        var accountHolder = new AccountHolder();
        accountHolder.setUsername("user");
        accountHolder.setPassword("user");
        accountHolder.setNif("A12121212");
        accountHolder.setFirstName("FirstName");
        accountHolder.setLastName("LastName");
        accountHolder.setDateOfBirth(LocalDate.parse("1990-01-01"));
        accountHolder.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));

        var listOfAccounts = new ArrayList<Account>();
        accountHolder.setAccountList(listOfAccounts);

        var accountHolderInfo = AccountHolderInfoDto.fromAccountHolder(accountHolder);

        when(holdersService.viewPersonalInfo()).thenReturn(accountHolderInfo);

        mockMvc.perform(get("/holders/info")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("mail@mail.com"));
    }

    @Test
    void updateInfo() throws Exception {

        var accountHolder = new AccountHolder();
        accountHolder.setUsername("user");
        accountHolder.setPassword("user");
        accountHolder.setNif("A12121212");
        accountHolder.setFirstName("FirstName");
        accountHolder.setLastName("LastName");
        accountHolder.setDateOfBirth(LocalDate.parse("1990-01-01"));
        accountHolder.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));

        var newPhone = "+34555555";
        var accountHolderDtoResponse = AccountHolderDtoResponse.fromAccountHolder(accountHolder);
        accountHolderDtoResponse.setPhone(newPhone);

        when(holdersService.update(Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                            Optional.of(newPhone),
                                        Optional.empty()))
                            .thenReturn(accountHolderDtoResponse);

        mockMvc.perform(patch("/holders/update")
                        .param("phone",newPhone)
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(newPhone));
    }

    @Test
    void forgotPassword() throws Exception {

        var accountHolder = new AccountHolder();
        accountHolder.setUsername("user");
        accountHolder.setPassword("user");
        accountHolder.setNif("A12121212");
        accountHolder.setFirstName("FirstName");
        accountHolder.setLastName("LastName");
        accountHolder.setDateOfBirth(LocalDate.parse("1990-01-01"));
        accountHolder.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));

        var response = "An email has been sent to mail@mail.com, please follow instructions to restore your password.";

        when(holdersService.forgotPassword("A12121212","mail@mail.com"))
                .thenReturn(response);

        mockMvc.perform(post("/holders/forgotpassword")
                        .param("nif","A12121212")
                        .param("email","mail@mail.com")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(response)));

    }

    @Test
    void setSecondaryOwner() throws Exception {


        var accountHolder1 = new AccountHolder();
        accountHolder1.setUsername("user");
        accountHolder1.setPassword("user");
        accountHolder1.setNif("A12121212");
        accountHolder1.setFirstName("FirstName");
        accountHolder1.setLastName("LastName");
        accountHolder1.setDateOfBirth(LocalDate.parse("1990-01-01"));
        accountHolder1.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));

        var secondaryOwner = new AccountHolder();
        secondaryOwner.setId(10L);
        secondaryOwner.setUsername("user2");
        secondaryOwner.setPassword("user2");
        secondaryOwner.setNif("B4545454545");
        secondaryOwner.setFirstName("SecondaryOwnerName");
        secondaryOwner.setLastName("SecondaryOwnerLastName");
        secondaryOwner.setDateOfBirth(LocalDate.parse("1990-01-01"));
        secondaryOwner.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));

        var checkingAccount = new CheckingAccount();
        checkingAccount.setPrimaryOwner(accountHolder1);
        checkingAccount.setNumber("ES0000000001");
        checkingAccount.setSecondaryOwner(secondaryOwner);

        when(holdersService.setSecondaryOwner("ES0000000001","B4545454545"))
                .thenReturn(AccountDto.fromAccount(checkingAccount));

        mockMvc.perform(put("/holders/setSecondaryOwner/{account}","ES0000000001")
                        .param("secondaryOwnerNif","B4545454545")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("ES0000000001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.secondaryOwner.firstName").value("SecondaryOwnerName"));

    }



    @Test
    void createSavingAccount() throws Exception {

        var savingAccount = new CheckingAccount();
        savingAccount.setNumber("ES0000000001");

        var transaction = new Transaction();
        transaction.setAccount(savingAccount);
        transaction.setAmount(new Money(BigDecimal.valueOf(1200)));
        transaction.setTransactionType(TransactionType.CREATE_SAVING_ACCOUNT);

        when(holdersService.createSavingAccount(BigDecimal.valueOf(1200)))
                .thenReturn(TransactionDto.fromTransaction(transaction));

        mockMvc.perform(post("/holders/create/saving")
                        .param("amount", "1200")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value("ES0000000001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value("CREATE_SAVING_ACCOUNT"));
    }

    @Test
    void depositInCheckingAccount() throws Exception {
        var checkingAccount = new CheckingAccount();
        checkingAccount.setNumber("ES0000000001");

        var transaction = new Transaction();
        transaction.setAccount(checkingAccount);
        transaction.setAmount(new Money(BigDecimal.valueOf(100)));
        transaction.setTransactionType(TransactionType.DEPOSIT);

        when(holdersService.depositInCheckingAccount(BigDecimal.valueOf(100)))
                .thenReturn(TransactionDto.fromTransaction(transaction));

        mockMvc.perform(put("/holders/deposit")
                        .param("amount", "100")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value("ES0000000001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value("DEPOSIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value("100.0"));
    }

    @Test
    void depositSavingAccount() throws Exception {

        var savingAccount = new SavingAccount();
        savingAccount.setNumber("ES0000000001");

        var transaction = new Transaction();
        transaction.setAccount(savingAccount);
        transaction.setAmount(new Money(BigDecimal.valueOf(3000)));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setObservations("Deposit to Saving Account");


        when(holdersService.depositSavingAccount("ES0000000001",BigDecimal.valueOf(3000)))
                .thenReturn(TransactionDto.fromTransaction(transaction));

        mockMvc.perform(put("/holders/deposit/saving/{account}","ES0000000001")
                        .param("amount", "3000")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value("ES0000000001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value("DEPOSIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.observations").value("Deposit to Saving Account"));
    }

    @Test
    void withdraw() throws Exception {

        var checkingAccount = new CheckingAccount();
        checkingAccount.setNumber("ES0000000001");
        checkingAccount.setStatus(Status.ACTIVE);
        checkingAccount.setBalance(new Money(BigDecimal.valueOf(3000)));


        var transaction = new Transaction();
        transaction.setAccount(checkingAccount);
        transaction.setAmount(new Money(BigDecimal.valueOf(250)));
        transaction.setTransactionType(TransactionType.WITHDRAW);


        when(holdersService.withdraw(BigDecimal.valueOf(250)))
                .thenReturn(TransactionDto.fromTransaction(transaction));

        mockMvc.perform(get("/holders/withdraw")
                        .param("amount", "250")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value("ES0000000001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value("WITHDRAW"));
    }

    @Test
    void withdrawSavingAccount() throws Exception {

        var savingAccount = new SavingAccount();
        savingAccount.setNumber("ES0000000001");
        savingAccount.setStatus(Status.ACTIVE);
        savingAccount.setBalance(new Money(BigDecimal.valueOf(3000)));


        var transaction = new Transaction();
        transaction.setAccount(savingAccount);
        transaction.setAmount(new Money(BigDecimal.valueOf(250)));
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setObservations("Owner Withdraw");



        when(holdersService.withdrawSavingAccount("ES0000000001",BigDecimal.valueOf(250)))
                .thenReturn(TransactionDto.fromTransaction(transaction));

        mockMvc.perform(get("/holders/withdraw/saving/{account}","ES0000000001")
                        .param("amount", "250")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.observations").value("Owner Withdraw"));
    }


    @Test
    void buyWithCredit() throws Exception {

        var creditAccount = new CreditCardAccount();
        creditAccount.setNumber("ES0000000001");
        creditAccount.setCreditLimit(BigDecimal.valueOf(1000));
        creditAccount.setStatus(Status.ACTIVE);

        var transaction = new Transaction();
        transaction.setAccount(creditAccount);
        transaction.setAmount(new Money(BigDecimal.valueOf(100)));
        transaction.setTransactionType(TransactionType.PAY_WITH_CREDIT_CARD);
        transaction.setObservations("Payment at StoreName");

        when(holdersService.buyWithCredit(BigDecimal.valueOf(100), "StoreName"))
                .thenReturn(TransactionDto.fromTransaction(transaction));

        mockMvc.perform(put("/holders/buywithcredit")
                        .param("amount", "100")
                        .header("store", "StoreName")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.observations").value("Payment at StoreName"));
    }


    @Test
    void allAccounts() throws Exception {
        var accountHolder = new AccountHolder();
        accountHolder.setId(10L);
        accountHolder.setUsername("user");
        accountHolder.setPassword("user");
        accountHolder.setNif("B4545454545");
        accountHolder.setFirstName("FirstOwner");
        accountHolder.setLastName("LastName");
        accountHolder.setDateOfBirth(LocalDate.parse("1990-01-01"));
        accountHolder.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));


        var checkingAccount = new CheckingAccount();
        checkingAccount.setNumber("ES0000000001");
        checkingAccount.setPrimaryOwner(accountHolder);

        var savingAccount = new SavingAccount();
        savingAccount.setNumber("ES0000000002");
        savingAccount.setPrimaryOwner(accountHolder);

        var creditAccount = new CreditCardAccount();
        creditAccount.setNumber("ES0000000003");
        creditAccount.setPrimaryOwner(accountHolder);


        var accountDtoList = new ArrayList<AccountDto>(List.of(
                AccountDto.fromAccount(checkingAccount),
                AccountDto.fromAccount(savingAccount),
                AccountDto.fromAccount(creditAccount)));

        when(holdersService.allAccounts())
                .thenReturn(accountDtoList);

        mockMvc.perform(get("/holders/all")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*",hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].primaryOwner.nif").value("B4545454545"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value("ES0000000001"));
    }

    @Test
    void viewAccount() throws Exception {
        var accountHolder = new AccountHolder();
        accountHolder.setId(10L);
        accountHolder.setUsername("user");
        accountHolder.setPassword("user");
        accountHolder.setNif("B4545454545");
        accountHolder.setFirstName("FirstOwner");
        accountHolder.setLastName("LastName");
        accountHolder.setDateOfBirth(LocalDate.parse("1990-01-01"));
        accountHolder.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));


        var checkingAccount = new CheckingAccount();
        checkingAccount.setNumber("ES0000000001");
        checkingAccount.setPrimaryOwner(accountHolder);


        when(holdersService.viewAccount("ES0000000001"))
                .thenReturn(AccountDto.fromAccount(checkingAccount));

        mockMvc.perform(get("/holders/account")
                        .param("account","ES0000000001")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.primaryOwner.nif").value("B4545454545"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("ES0000000001"));
    }

    @Test
    void viewTransactions() throws Exception {
        var checkingAccount = new CheckingAccount();
        checkingAccount.setNumber("ES0000000001");
        checkingAccount.setBalance(new Money(BigDecimal.valueOf(2000)));

        var transaction1 = new Transaction();
        transaction1.setAccount(checkingAccount);
        transaction1.setAmount(new Money(BigDecimal.valueOf(100).negate()));
        transaction1.setTransactionType(TransactionType.WITHDRAW);
        checkingAccount.getBalance().decreaseAmount(BigDecimal.valueOf(100));

        var transaction2 = new Transaction();
        transaction2.setAccount(checkingAccount);
        transaction2.setAmount(new Money(BigDecimal.valueOf(20).negate()));
        transaction2.setTransactionType(TransactionType.WITHDRAW);
        checkingAccount.getBalance().decreaseAmount(BigDecimal.valueOf(20));


        var transaction3 = new Transaction();
        transaction3.setAccount(checkingAccount);
        transaction3.setAmount(new Money(BigDecimal.valueOf(200)));
        transaction3.setTransactionType(TransactionType.DEPOSIT);
        checkingAccount.getBalance().increaseAmount(BigDecimal.valueOf(200));

        var transactionDtoList = new ArrayList<>(List.of(
                TransactionDto.fromTransaction(transaction1),
                TransactionDto.fromTransaction(transaction2),
                TransactionDto.fromTransaction(transaction3)));

        when(holdersService.viewTransactions("ES0000000001"))
                .thenReturn(transactionDtoList);


        mockMvc.perform(get("/holders/transactions")
                        .param("account","ES0000000001")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*",hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionType").value("WITHDRAW"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value("-100.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].transactionType").value("DEPOSIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].amount").value("200.0"));
    }

    @Test
    void transferToAccount() throws Exception {

        var checkingAccount = new CheckingAccount();
        checkingAccount.setNumber("ES0000000001");
        checkingAccount.setBalance(new Money(BigDecimal.valueOf(100)));

        var transaction = new Transaction();
        transaction.setAccount(checkingAccount);
        transaction.setAmount(new Money(BigDecimal.valueOf(20).negate()));
        transaction.setTransactionType(TransactionType.TRANSFER);

        when(holdersService.transferToAccount("ES0000000002",BigDecimal.valueOf(20)))
                .thenReturn(TransactionDto.fromTransaction(transaction));

        mockMvc.perform(put("/holders/transfer/{account}","ES0000000002")
                        .param("amount","20")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value("TRANSFER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value("-20.0"));
    }

    @Test
    void transferToAccount2() throws Exception {

        var checkingAccount = new CheckingAccount();
        checkingAccount.setNumber("ES0000000001");
        checkingAccount.setBalance(new Money(BigDecimal.valueOf(100)));

        when(holdersService.transferToAccount("ES0000000002",BigDecimal.valueOf(2000)))
                .thenThrow(new EspecificException("Error. Account doesn't have enough founds."));

        mockMvc.perform(put("/holders/transfer/{account}","ES0000000002")
                        .param("amount","2000")
                        .with(httpBasic("user","user")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error. Account doesn't have enough founds."));
    }
}