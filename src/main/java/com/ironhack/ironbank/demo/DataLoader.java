package com.ironhack.ironbank.demo;


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
        var user1 = new User("admin",passwordEncoder.encode("admin"),"ROLE_ADMIN");
        userRepository.save(user1);
        log.info("Final Loading Data...");

    }

}
