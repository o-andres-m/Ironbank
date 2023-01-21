package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDto;
import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.exception.UserNotFoundException;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.accounts.SavingAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HoldersService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;



    public AccountHolderDto register(AccountHolderDto accountHolderDto) {
        var accountHolder = new AccountHolder();
        accountHolder.setUsername(accountHolderDto.getUsername());
        accountHolder.setPassword(passwordEncoder.encode(accountHolderDto.getPassword()));
        accountHolder.setNif(accountHolderDto.getNif());
        accountHolder.setRoles("ROLE_ACCOUNTHOLDER");
        accountHolder.setFirstName(accountHolderDto.getFirstName());
        accountHolder.setLastName(accountHolderDto.getLastName());
        accountHolder.setDateOfBirth(accountHolderDto.getDateOfBirth());
        accountHolder.setAddress(new Address(accountHolderDto.getAddress(),accountHolderDto.getAddress()));

        if (Utils.calculateAge(accountHolder.getDateOfBirth())<18){
            throw new EspecificException("Clients must have +18 to register. Please go to the Office of the Bank.");
        }else {
            return AccountHolderDto.fromAccountHolder(userRepository.save(accountHolder));
        }
    }

    public AccountDto createAccount(String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var accountHolder = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundException(authentication.getName()));

        switch (type) {
            case "checking":{
                var accountCreated = new CheckingAccount((AccountHolder) accountHolder);
                accountRepository.save(accountCreated);
                return AccountDto.fromAccount(accountCreated);
            }
            case "saving":{
                var accountCreated = new SavingAccount((AccountHolder) accountHolder);
                accountRepository.save(accountCreated);
                return AccountDto.fromAccount(accountCreated);
            }
            default:{
                throw new EspecificException("Account type not found.");
            }
        }
    }

}
