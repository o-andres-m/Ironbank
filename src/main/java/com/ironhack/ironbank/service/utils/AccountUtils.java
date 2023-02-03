package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.users.AdminDto;
import com.ironhack.ironbank.dto.users.ThirdPartyDto;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CheckingAccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.setting.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountUtils {

    private final CheckingAccountRepository checkingAccountRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final AccountHolderRepository accountHolderRepository;


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

    //TODO MOVER A USER UTILS
    public void verifyUserExists(String user) {
        var findUserInDb = userRepository.findByUsername(user);
        if (findUserInDb.isPresent()) throw new EspecificException("Username already exists. Please change username.");
    }
    //TODO MOVER A USER UTILS

    public void verifyNifExists(String user) {
        var findUserInDb = accountHolderRepository.findAccountHolderByNif(user);
        if (findUserInDb.isPresent()) throw new EspecificException("Nif is registered.");
    }


    public CheckingAccount findCheckingAccountByAccountHolder(AccountHolder accountHolder) {
        return checkingAccountRepository.findCheckingAccountByPrimaryOwner(accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Checking Account."));
    }
    //TODO MOVER A USER UTILS

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
    //TODO MOVER A USER UTILS

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
    //TODO MOVER A USER UTILS

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


    public void checkFinalBalance(Account accountToCharge, BigDecimal amount) {
        if (accountToCharge.getBalance().getAmount().subtract(amount).signum() < 0){
            throw new EspecificException("Error. Account doesn't have enough founds.");
        }
    }

    public Account getAndVerifyAccount(String account, User accountHolder) {
        var accountFound = accountRepository.findAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account not found."));
        if(!accountFound.getPrimaryOwner().equals(accountHolder)){
            throw new EspecificException("Account is not yours!");
        }
        return accountFound;
    }

    //TODO MOVER ESTA A USER UTILS
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new EspecificException("User with ID "+ id +" not found."));
    }

    public Account getAccountByNumber(String account) {
        var accountFound = accountRepository.findAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account not found."));
        return accountFound;
    }


    public void checkMinimumBalance(Account account, BigDecimal amount) {
        if (account.getBalance().getAmount().subtract(amount).subtract(account.getPenaltyFee().getPenaltyAmount()).doubleValue() <= 0 &&
                account.getBalance().getAmount().subtract(amount).subtract(account.getPenaltyFee().getPenaltyAmount()).doubleValue() != account.getPenaltyFee().getPenaltyAmount().negate().doubleValue()){
            throw new EspecificException("Error. You are trying to let less the amount of " +
                    "penalty fee, please withdraw less amount or make a total withdraw.");
        }

    }

    public boolean checkFinalBalanceZero(Account account, BigDecimal amount) {
        return account.getBalance().getAmount().subtract(amount).doubleValue() == 0;
    }

    public void checkAccountNotFreezed(Account account) {
        if(account.getStatus().equals(Status.FREEZE)) throw new EspecificException("Account Freezed. Please contact Bank.");
    }

    public CheckingAccount checkUserHaveCheckingAccount(User accountHolder) {
        return checkingAccountRepository.findCheckingAccountByPrimaryOwner((AccountHolder) accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Checking Account."));
    }
}
