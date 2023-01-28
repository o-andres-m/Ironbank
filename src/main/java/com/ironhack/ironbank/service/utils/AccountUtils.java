package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.dto.AdminDto;
import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CheckingAccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.setting.Settings;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountUtils {

    private final CheckingAccountRepository checkingAccountRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    /**
     * Return automatic Account Number Generated.
     */
    public static String generateAccountNumber(AccountHolder accountHolder){
        var dNow = new Date();
        var ft = new SimpleDateFormat("yyMMddhhmmss");

        return Settings.getBANK_ACCOUNT_START().toUpperCase()+
                accountHolder.getDateOfBirth().toString().substring(2,4)+
                ft.format(dNow);
    }

    public void verifyUserExists(String user) {
        var findUserInDb = userRepository.findByUsername(user);
        if (findUserInDb.isPresent()) throw new EspecificException("Username already exists. Please change username.");
    }


    public void findCheckingAccountByAccountHolder(AccountHolder accountHolder) {
        var checkingAccount = checkingAccountRepository.findCheckingAccountByPrimaryOwner(accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Checking Account."));
    }

    public AccountHolder createAccountHolder(AccountHolderDto accountHolderDto, String user) {
        var accountHolder = new AccountHolder();
        accountHolder.setUsername(user);
        accountHolder.setPassword(passwordEncoder.encode(accountHolderDto.getPassword()));
        accountHolder.setNif(accountHolderDto.getNif());
        accountHolder.setRoles("ROLE_ACCOUNTHOLDER");
        accountHolder.setFirstName(accountHolderDto.getFirstName());
        accountHolder.setLastName(accountHolderDto.getLastName());
        accountHolder.setDateOfBirth(accountHolderDto.getDateOfBirth());
        accountHolder.setAddress(new Address(accountHolderDto.getAddress(), accountHolderDto.getEmail(), accountHolderDto.getPhone()));
        return accountHolder;
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

    public  Admin createAdmin(AdminDto adminDto) {
        var admin = new Admin();
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        admin.setUsername(adminDto.getUsername());
        admin.setEmail(adminDto.getEmail());
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setRoles("ROLE_ADMIN");
        return admin;
    }





}
