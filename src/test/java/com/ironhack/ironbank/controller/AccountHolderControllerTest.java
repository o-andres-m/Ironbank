package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.info.AccountHolderInfoDto;
import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.service.HoldersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        accountHolder.setUsername("username");
        accountHolder.setPassword("password");
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("1990-01-01"));
    }

    @Test
    void viewPersonalInfo() throws Exception {
        var accountHolder = new AccountHolder();
        accountHolder.setUsername("username");
        accountHolder.setPassword("password");
        accountHolder.setNif("A12121212");
        accountHolder.setFirstName("FirstName");
        accountHolder.setLastName("LastName");
        accountHolder.setDateOfBirth(LocalDate.parse("1990-01-01"));
        accountHolder.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));

        // TODO: Account list

        var accountHolderInfo = AccountHolderInfoDto.fromAccountHolder(accountHolder);

        when(holdersService.viewPersonalInfo()).thenReturn(accountHolderInfo);

        mockMvc.perform(post("/holders/register")
                        .with(httpBasic("username","password")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"));

    }

    @Test
    void updateInfo() throws Exception {

        var accountHolder = new AccountHolder();
        accountHolder.setUsername("username");
        accountHolder.setPassword("password");
        accountHolder.setNif("A12121212");
        accountHolder.setFirstName("FirstName");
        accountHolder.setLastName("LastName");
        accountHolder.setDateOfBirth(LocalDate.parse("1990-01-01"));
        accountHolder.setAddress(new Address("Addres 1, City", "mail@mail.com", "+3412121212"));

        var accountHolderDto = AccountHolderDto.fromAccountHolder(accountHolder);

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
        accountHolder.setUsername("username");
        accountHolder.setPassword("password");
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
    void setSecondaryOwner() {
    }

    @Test
    void createCheckingAccount() {
    }

    @Test
    void testCreateCheckingAccount() {
    }

    @Test
    void createCreditAccount() {
    }


    @Test
    void depositInCheckingAccount() {
    }

    @Test
    void depositSavingAccount() {
    }

    @Test
    void depositSavingAccountFromChecking() {
    }

    @Test
    void withdraw() {
    }

    @Test
    void withdrawSavingAccount() {
    }

    @Test
    void buyWithCredit() {
    }

    @Test
    void allAccounts() {
    }

    @Test
    void viewAccount() {
    }

    @Test
    void viewTransactions() {
    }

    @Test
    void trasnferToAccount() {
    }
}