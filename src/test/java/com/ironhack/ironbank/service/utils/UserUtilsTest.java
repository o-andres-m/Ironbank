package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.dto.users.AdminDto;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserUtilsTest {

    @Autowired
    private UserUtils userUtils;
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void getUserById() {
        var userAdmin = userUtils.getUserById(1L);
        assertEquals(userAdmin.getUsername(),"admin");
        assertThrows(EspecificException.class, ()->userUtils.getUserById(999L));
    }

    @Test
    void verifyNifExists() {
        assertThrows(EspecificException.class, ()-> userUtils.verifyNifExists("Y3434343Y"));
        assertDoesNotThrow(()-> userUtils.verifyNifExists("AAAAAAA"));
    }

    @Test
    void verifyUserExists() {
        assertThrows(EspecificException.class, ()-> userUtils.verifyUserExists("user"));
        assertDoesNotThrow(()-> userUtils.verifyUserExists("AAAAAAA"));
    }

    @Test
    void createAdmin() {
        var adminDto = new AdminDto();
        adminDto.setPassword("pass");
        adminDto.setUsername("username");
        adminDto.setEmail("email@email.com");
        adminDto.setFirstName("FirstName");
        adminDto.setLastName("LastName");

        var admin = userUtils.createAdmin(adminDto);

        assertEquals(admin.getUsername(),adminDto.getUsername());
        assertEquals(admin.getEmail(),adminDto.getEmail());
        assertEquals(admin.getUsername(),adminDto.getUsername());
    }

    @Test
    void getAccountHolder() {
        var accountHolder = userUtils.getAccountHolder(2L);
        assertEquals(accountHolder.getId(),2L);
        assertThrows(EspecificException.class, ()->userUtils.getAccountHolder(999L));
    }

    @Test
    void getUserByNif() {

        assertEquals(2L, (long) userUtils.getUserByNif("Y3434343Y").getId());
    }
}