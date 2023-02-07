package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.account.AccountDto;
import com.ironhack.ironbank.dto.users.*;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.service.AdminService;
import com.ironhack.ironbank.service.ThirdPartyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @MockBean
    AdminService adminService;
    @Test
    void registerAdmin() throws Exception {

        var admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin");

        var adminDto = AdminDto.fromAdmin(admin);
        var adminDtoResponse = AdminDtoResponse.fromAdmin(admin);

        when(adminService.registerAdmin(adminDto)).thenReturn(adminDtoResponse);

        mockMvc.perform(post("/admin/register/ADMIN")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(adminDto))
                        .with(httpBasic("admin","admin")))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void searchAH() throws Exception {

        var accountHolderDto1 = new AccountHolderDtoResponse();
        var accountHolderDto2 = new AccountHolderDtoResponse();
        var accountHolderDto3 = new AccountHolderDtoResponse();

        var listOfAHDTO = new ArrayList<AccountHolderDtoResponse>(List.of(
                accountHolderDto1,accountHolderDto2,accountHolderDto3));


        when(adminService.searchAH(Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                        Optional.empty(),
                                    Optional.empty()))
                                    .thenReturn(listOfAHDTO);

        mockMvc.perform(get("/admin/AH")
                        .with(httpBasic("admin","admin")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*",hasSize(3)));
    }

    @Test
    void searchAHWithParam() throws Exception {

        var accountHolderDto1 = new AccountHolderDtoResponse();
        accountHolderDto1.setUsername("username1");
        var accountHolderDto2 = new AccountHolderDtoResponse();
        accountHolderDto2.setUsername("username2");
        var accountHolderDto3 = new AccountHolderDtoResponse();
        accountHolderDto3.setUsername("username3");

        var listOfAHDTO = new ArrayList<AccountHolderDtoResponse>(List.of(
                accountHolderDto1,accountHolderDto2,accountHolderDto3));


        when(adminService.searchAH(Optional.of("user"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()))
                .thenReturn(listOfAHDTO);

        mockMvc.perform(get("/admin/AH")
                        .param("username","user")
                        .with(httpBasic("admin","admin")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*",hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].username").value("username3"));
    }

    @Test
    void updateAH() throws Exception {

        var accountHolderDto = new AccountHolderDtoResponse();
        accountHolderDto.setId(22L);
        accountHolderDto.setUsername("username");
        accountHolderDto.setEmail("email@mail.com");
        accountHolderDto.setNif("A1234");

        var accountHolderDtoUpdated = new AccountHolderDtoResponse();
        accountHolderDtoUpdated.setId(22L);
        accountHolderDtoUpdated.setUsername("newUsername");
        accountHolderDtoUpdated.setEmail("newemail@email.com");
        accountHolderDtoUpdated.setNif("A1234");



        when(adminService.updateAH(22L,
                Optional.of("newUsername"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of("newemail@email.com"),
                Optional.empty(),
                Optional.empty()))
                .thenReturn(accountHolderDtoUpdated);

        mockMvc.perform(patch("/admin/AH/{id}","22")
                        .param("username","newUsername")
                        .param("email","newemail@email.com")
                        .with(httpBasic("admin","admin")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("newUsername"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("newemail@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nif").value("A1234"));

    }

    @Test
    void activateUser() throws Exception {


        when(adminService.activateUser(22L))
                .thenReturn("User id: 22, username: username, is now ACTIVATED");

        mockMvc.perform(patch("/admin/activate/{id}","22")
                        .with(httpBasic("admin","admin")))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void deleteUser() throws Exception {

        when(adminService.deleteUser(22L))
                .thenReturn("User Nº 22 deleted. Their accounts have been deleted as well");

        mockMvc.perform(delete("/admin/user/{id}","22")
                        .with(httpBasic("admin","admin")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("User Nº 22 deleted. Their accounts have been deleted as well"));
    }

    @Test
    void deleteUserNotFound() throws Exception {

        when(adminService.deleteUser(22L))
                .thenThrow(new EspecificException("User Not Found"));

        mockMvc.perform(delete("/admin/user/{id}","22")
                        .with(httpBasic("admin","admin")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Not Found"));
    }

    @Test
    void freezeAccount() throws Exception {

            var account = new CheckingAccount();
            account.setNumber("ES000000000001");
            account.setStatus(Status.ACTIVE);

        var accountDto = new AccountDto();
        accountDto.setNumber("ES000000000001");
        accountDto.setStatus(Status.FREEZE);



        when(adminService.freezeAccount("ES000000000001",0))
                    .thenReturn(accountDto);

            mockMvc.perform(patch("/admin/accounts/freeze/{account}","ES000000000001")
                            .param("action","0")
                            .with(httpBasic("admin","admin")))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("FREEZE"));
    }
}