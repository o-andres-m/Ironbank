package com.ironhack.ironbank.demo;


import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

        log.info("Final Loading Data...");

    }

}
