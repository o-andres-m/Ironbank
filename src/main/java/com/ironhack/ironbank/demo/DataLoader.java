package com.ironhack.ironbank.demo;


import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("load-data")
@RequiredArgsConstructor
@Log
public class DataLoader {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @EventListener(ApplicationReadyEvent.class)
    public void loadData(){

        log.info("Loading Data to Database....");

        // Create Admin
        var admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles("ROLE_ADMIN");
        admin.setFirstName("AdminName");
        admin.setLastName("AdminLastName");
        admin.setEmail("admin@ironbank.com");
        userRepository.save(admin);

        var accountHolder = new AccountHolder();
        accountHolder.setUsername("user");
        accountHolder.setPassword(passwordEncoder.encode("user"));
        accountHolder.setRoles("ROLE_ACCOUNTHOLDER");
        accountHolder.setFirstName("UserName");
        accountHolder.setLastName("UserLastName");
        accountHolder.setNif("Y3434343Y");
        accountHolder.setDateOfBirth(LocalDate.parse("1990-02-02"));
        accountHolder.setAddress(new Address("Street 1, City","mail@accountholder.com", "+34343434"));
        userRepository.save(accountHolder);


        var thirdParty = new ThirdParty();
        thirdParty.setUsername("third");
        thirdParty.setPassword(passwordEncoder.encode("third"));
        thirdParty.setRoles("ROLE_THIRDPARTY");
        thirdParty.setCompanyName("CompanyName");
        thirdParty.setNif("S22222222");
        thirdParty.setAddress(new Address("Industry 1, Factory sector","contac@companyname.com","+3411333113"));
        userRepository.save(thirdParty);

        log.info("Final Loading Data...");

    }

}
