package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.users.AdminDto;
import com.ironhack.ironbank.dto.users.ThirdPartyDto;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.exception.UserNotFoundException;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import com.ironhack.ironbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountHolderRepository accountHolderRepository;


    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new EspecificException("User with ID "+ id +" not found."));
    }

    public void verifyNifExists(String user) {
        var findUserInDb = accountHolderRepository.findAccountHolderByNif(user);
        if (findUserInDb.isPresent()) throw new EspecificException("Nif is registered.");
    }

    public void verifyUserExists(String user) {
        var findUserInDb = userRepository.findByUsername(user);
        if (findUserInDb.isPresent()) throw new EspecificException("Username already exists. Please change username.");
    }

    public Admin createAdmin(AdminDto adminDto) {
        var admin = new Admin();
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        admin.setUsername(adminDto.getUsername());
        admin.setEmail(adminDto.getEmail());
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setRoles("ROLE_ADMIN");
        return admin;
    }

    public ThirdParty createThirdParty(ThirdPartyDto thirdPartyDto) {
        var thirdParty = new ThirdParty();
        thirdParty.setUsername(thirdPartyDto.getUsername());
        thirdParty.setPassword(passwordEncoder.encode(thirdPartyDto.getPassword()));
        thirdParty.setNif(thirdPartyDto.getNif());
        thirdParty.setCompanyName(thirdPartyDto.getCompanyName());
        thirdParty.setRoles("ROLE_THIRDPARTY");
        thirdParty.setAddress(new Address(thirdPartyDto.getAddress(), thirdPartyDto.getEmail(), thirdPartyDto.getPhone()));
        return thirdParty;
    }

    public AccountHolder createAccountHolder(AccountHolderDto accountHolderDto) {
        var accountHolder = new AccountHolder();
        accountHolder.setUsername(accountHolderDto.getUsername());
        accountHolder.setPassword(passwordEncoder.encode(accountHolderDto.getPassword()));
        accountHolder.setNif(accountHolderDto.getNif());
        accountHolder.setRoles("ROLE_ACCOUNTHOLDER");
        accountHolder.setFirstName(accountHolderDto.getFirstName());
        accountHolder.setLastName(accountHolderDto.getLastName());
        accountHolder.setDateOfBirth(accountHolderDto.getDateOfBirth());
        accountHolder.setAddress(new Address(accountHolderDto.getAddress(), accountHolderDto.getEmail(), accountHolderDto.getPhone()));
        return accountHolder;
    }

    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundException(authentication.getName()));
    }

    public AccountHolder getAccountHolder(Long id) {
        return (AccountHolder) userRepository.findById(id).orElseThrow(
                () -> new EspecificException("User Not Found"));
    }

    public AccountHolder getUserByNif(String nif) {
        return userRepository.findAccountHolderByNif(nif).orElseThrow(()-> new EspecificException("User with NIF "+ nif +" not found."));
    }
}
