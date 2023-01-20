package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.AccountHolder;
import com.ironhack.ironbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HoldersService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;



    public AccountHolderDto register(AccountHolderDto accountHolderDto) {
        var accountHolder = new AccountHolder();
        accountHolder.setUsername(accountHolderDto.getUsername());
        accountHolder.setPassword(passwordEncoder.encode(accountHolderDto.getPassword()));
        accountHolder.setNif(accountHolderDto.getNif());
        accountHolder.setRoles("ROLE_ACCOUNTHOLDER");
        accountHolder.setFirstName(accountHolderDto.getFirstName());
        accountHolder.setLastName(accountHolderDto.getLastName());
        accountHolder.setDateOfBirth(accountHolderDto.getDateOfBirth());
        accountHolder.setAddress(new Address(accountHolderDto.getAddress().getAddress(),accountHolderDto.getAddress().getEmail()));

        if (Utils.calculateAge(accountHolder.getDateOfBirth())<18){
            throw new EspecificException("Clients must have +18 to register. Please go to the Bank.");
        }else {
            return AccountHolderDto.fromAccountHolder(userRepository.save(accountHolder));
        }
    }
}
