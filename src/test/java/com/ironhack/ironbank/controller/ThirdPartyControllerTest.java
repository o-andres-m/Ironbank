package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.account.AccountMapDto;
import com.ironhack.ironbank.dto.transaction.TransactionDto;
import com.ironhack.ironbank.dto.transaction.TransferDto;
import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.dto.users.ThirdPartyDto;
import com.ironhack.ironbank.dto.users.ThirdPartyDtoResponse;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.AccountMap;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.enums.TransactionType;
import com.ironhack.ironbank.service.HoldersService;
import com.ironhack.ironbank.service.ThirdPartyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ThirdPartyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @MockBean
    ThirdPartyService thirdPartyService;

    @Test
    void createUser() throws Exception {

        var thirdParty = new ThirdParty();
        thirdParty.setUsername("third");
        thirdParty.setPassword("third");
        thirdParty.setNif("F767676767");
        thirdParty.setCompanyName("CompanyName");
        thirdParty.setAddress(new Address("Addres 1 Company, City", "mail@company.com", "+34127676"));

        var thirdPartyDto = ThirdPartyDto.fromThirdParty(thirdParty);
        var thirdPartyDtoResponse = ThirdPartyDtoResponse.fromThirdParty(thirdParty);

        when(thirdPartyService.createUser(thirdPartyDto)).thenReturn(thirdPartyDtoResponse);

        mockMvc.perform(post("/thirdparty/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(thirdPartyDto)))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    void registerAccount() throws Exception {

        var accountMap = new AccountMap();
        accountMap.setAccountNumber("ES0000000001");
        accountMap.setAccountKey("ABC123-ABC1123-ABC123-ABC123");

        when(thirdPartyService.registerAccount("ES0000000001","ABC123-ABC1123-ABC123-ABC123"))
                .thenReturn(AccountMapDto.fromAccountMap(accountMap));

        mockMvc.perform(post("/thirdparty/account")
                        .param("account","ES0000000001")
                        .param("secretKey","ABC123-ABC1123-ABC123-ABC123")
                        .with(httpBasic("third","third")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value("ES0000000001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountKey").value("ABC123-ABC1123-ABC123-ABC123"));
    }

    @Test
    void allAccountMap() throws Exception {

        var accountMapList = new ArrayList<AccountMapDto>(List.of(
                new AccountMapDto(),
                new AccountMapDto(),
                new AccountMapDto(),
                new AccountMapDto()));

        when(thirdPartyService.allAcountMap())
                .thenReturn(accountMapList);

        mockMvc.perform(get("/thirdparty/account")
                        .with(httpBasic("third","third")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*",hasSize(4)));
    }

    @Test
    void allAccountMap_WithoutAuth() throws Exception {

        mockMvc.perform(get("/thirdparty/account"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void debitservice() throws Exception {

        var account = new CheckingAccount();
        account.setNumber("ES00000000001");

        var transactionDto = new TransactionDto();
        transactionDto.setAccount("ES00000000001");
        transactionDto.setTransactionType(TransactionType.PAY_SERVICE);
        transactionDto.setAmount(BigDecimal.valueOf(100));

        when(thirdPartyService.debitService("ES00000000001", BigDecimal.valueOf(100)))
                .thenReturn(transactionDto);


        mockMvc.perform(patch("/thirdparty/debitservice")
                        .param("amount","100")
                        .header("account","ES00000000001")
                        .with(httpBasic("third","third")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value("PAY_SERVICE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value("100"));
    }


    @Test
    void debitserviceWithoutHeaders() throws Exception {

        var account = new CheckingAccount();
        account.setNumber("ES00000000001");

        var transactionDto = new TransactionDto();
        transactionDto.setAccount("ES00000000001");
        transactionDto.setTransactionType(TransactionType.PAY_SERVICE);
        transactionDto.setAmount(BigDecimal.valueOf(100));

        when(thirdPartyService.debitService("ES00000000001", BigDecimal.valueOf(100)))
                .thenReturn(transactionDto);


        mockMvc.perform(patch("/thirdparty/debitservice")
                        .param("amount","100")
                        .with(httpBasic("third","third")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Required request header 'account' for method parameter type String is not present"));
    }

    @Test
    void chargeService() throws Exception {

        var transactionDto = new TransactionDto();
        transactionDto.setAccount("ES00000000001");
        transactionDto.setTransactionType(TransactionType.PAY_SERVICE);
        transactionDto.setAmount(BigDecimal.valueOf(10));

        when(thirdPartyService.chargeService("CompanyName","ES00000000001","ABC123-ABC1123-ABC123-ABC123", BigDecimal.valueOf(10) ))
                .thenReturn(transactionDto);

        mockMvc.perform(patch("/thirdparty/chargeservice")
                        .param("amount","100")
                        .header("company","CompanyName")
                        .header("account","ES00000000001")
                        .header("secretKey","ABC123-ABC1123-ABC123-ABC123"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void chargeServiceWithoutHeaders() throws Exception {

        var transactionDto = new TransactionDto();
        transactionDto.setAccount("ES00000000001");
        transactionDto.setTransactionType(TransactionType.PAY_SERVICE);
        transactionDto.setAmount(BigDecimal.valueOf(10));

        when(thirdPartyService.chargeService("CompanyName","ES00000000001","ABC123-ABC1123-ABC123-ABC123", BigDecimal.valueOf(10) ))
                .thenReturn(transactionDto);

        mockMvc.perform(patch("/thirdparty/chargeservice")
                        .param("amount","100")
                        .header("company","CompanyName")
                        .header("account","ES00000000001"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Required request header 'secretKey' for method parameter type String is not present"));
    }

    @Test
    void transferToAccount() throws Exception {

        var transactionDto = new TransactionDto();
        transactionDto.setAccount("ES00000000001");
        transactionDto.setTransactionType(TransactionType.TRANSFER);
        transactionDto.setAmount(BigDecimal.valueOf(34));
        transactionDto.setObservations("Transfer from ThirdParty Name User (Bank: Another Bank)");

        var transferDto = new TransferDto();
        transferDto.setToAccount("ES00000000001");
        transferDto.setAmount(BigDecimal.valueOf(34));


        when(thirdPartyService.transferToAccount(transferDto,"Another Bank","ThirdParty Name User"))
                .thenReturn(transactionDto);

        mockMvc.perform(post("/thirdparty/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(transferDto))
                        .header("bank","Another Bank")
                        .header("thirdClient","ThirdParty Name User"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value("TRANSFER"));
    }
}