package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.account.AccountAdminViewDto;
import com.ironhack.ironbank.dto.account.AccountDto;
import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.users.AdminDto;
import com.ironhack.ironbank.dto.users.ThirdPartyDto;
import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.dto.users.AdminDtoResponse;
import com.ironhack.ironbank.dto.users.ThirdPartyDtoResponse;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.accounts.CreditCardAccount;
import com.ironhack.ironbank.model.entities.accounts.SavingAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.service.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final AccountUtils accountUtils;

    private final AccountRepository accountRepository;


    /**
     * Rgister Methods
     */
    public AccountHolderDtoResponse registerAH(AccountHolderDto accountHolderDto) {
        var user = accountHolderDto.getUsername();
        accountUtils.verifyUserExists(accountHolderDto.getUsername());
        accountUtils.verifyNifExists(accountHolderDto.getNif());

        var accountHolder = accountUtils.createAccountHolder(accountHolderDto);
        return AccountHolderDtoResponse.fromAccountHolder(userRepository.save(accountHolder));
    }

    public ThirdPartyDtoResponse registerTP(ThirdPartyDto thirdPartyDto) {
        var user = thirdPartyDto.getUsername();
        accountUtils.verifyUserExists(user);

        var thirdParty = accountUtils.createThirdParty(thirdPartyDto);
        return ThirdPartyDtoResponse.fromThirdParty(userRepository.save(thirdParty));

    }

    public AdminDtoResponse registerAdmin(AdminDto adminDto) {
        var user = adminDto.getUsername();
        accountUtils.verifyUserExists(user);

        var admin = accountUtils.createAdmin(adminDto);
        return AdminDtoResponse.fromAdmin(userRepository.save(admin));

    }

    /**
     * Search Methods
     */
    public ArrayList<AccountHolderDtoResponse> searchAH(Optional<String> username, Optional<String> firstName, Optional<String> lastName, Optional<String> nif, Optional<String> phone) {

        var listOfAH2 = userRepository.searchAccountHolders(username.orElse(""),
                firstName.orElse(""),lastName.orElse(""),
                nif.orElse(""),phone.orElse(""));

        var listOfAHDto = new ArrayList<AccountHolderDtoResponse>();
        for (User user : listOfAH2){
            listOfAHDto.add(AccountHolderDtoResponse.fromAccountHolder((AccountHolder) user));
        }
        return listOfAHDto;
    }


    public ArrayList<ThirdPartyDtoResponse> searchTP(Optional<String> username, Optional<String> companyName, Optional<String> nif) {

        var listOfTP = userRepository.searchThirdParty(username.orElse(""),
                companyName.orElse(""), nif.orElse(""));

        var listOfTPDto = new ArrayList<ThirdPartyDtoResponse>();
        for (User user : listOfTP){
            listOfTPDto.add(ThirdPartyDtoResponse.fromThirdParty((ThirdParty) user));
        }
        return listOfTPDto;
    }

    /**
     * Modify Methods
     */
    public AccountHolderDtoResponse updateAH(Long id, Optional<String> username, Optional<String> firstName, Optional<String> lastName, Optional<String> nif, Optional<String> phone, Optional<String> email, Optional<LocalDate> dateOfBirth, Optional<String> address) {

        User foundUser = accountUtils.getUserById(id);
        username.ifPresent(accountUtils::verifyUserExists);

        if(foundUser.getRoles().equals("ROLE_ACCOUNTHOLDER")) {
            var user =  (AccountHolder) foundUser;
            username.ifPresent(user::setUsername);
            firstName.ifPresent(user::setFirstName);
            lastName.ifPresent(user::setLastName);
            nif.ifPresent(user::setNif);
            dateOfBirth.ifPresent(user::setDateOfBirth);
            address.ifPresent(s -> user.getAddress().setAddress(s));
            phone.ifPresent(s -> user.getAddress().setPhone(s));
            email.ifPresent(s -> user.getAddress().setEmail(s));

            return AccountHolderDtoResponse.fromAccountHolder(userRepository.save(user));
        }else{
            throw new EspecificException("ID "+id+" is not AccountHolder");
        }
    }

    public ThirdPartyDtoResponse updateTP(Long id, Optional<String> username, Optional<String> companyName, Optional<String> nif, Optional<String> phone, Optional<String> email, Optional<String> address) {

        User foundUser = accountUtils.getUserById(id);
        username.ifPresent(accountUtils::verifyUserExists);

        if(foundUser.getRoles().equals("ROLE_TIRDPARTY")) {
            var user =  (ThirdParty) foundUser;
            username.ifPresent(user::setUsername);
            companyName.ifPresent(user::setCompanyName);
            nif.ifPresent(user::setNif);
            address.ifPresent(s -> user.getAddress().setAddress(s));
            phone.ifPresent(s -> user.getAddress().setPhone(s));
            email.ifPresent(s -> user.getAddress().setEmail(s));

            return ThirdPartyDtoResponse.fromThirdParty(userRepository.save(user));
        }else{
            throw new EspecificException("ID "+id+" is not ThirdParty");
        }
    }

    public AdminDtoResponse updateAdmin(Long id, Optional<String> username, Optional<String> firstName, Optional<String> lastName, Optional<String> email) {

        User foundUser = accountUtils.getUserById(id);
        username.ifPresent(accountUtils::verifyUserExists);

        if(foundUser.getRoles().equals("ROLE_ADMIN")) {
            var user =  (Admin) foundUser;
            username.ifPresent(user::setUsername);
            firstName.ifPresent(user::setFirstName);
            lastName.ifPresent(user::setLastName);
            email.ifPresent(user::setEmail);

            return AdminDtoResponse.fromAdmin(userRepository.save(user));
        }else{
            throw new EspecificException("ID "+id+" is not Admin");
        }
    }



    /**
     * Accounts Methods
     */
    public List<AccountDto> allAccounts(Optional<String> username) {
        var accountList = new ArrayList<Account>();
        if (username.isEmpty()){
            accountList = (ArrayList<Account>) accountRepository.findAll();
        }else{
            accountList = (ArrayList<Account>) accountRepository.findAccountByPrimaryOwner_Username(username.get());
        }
        var accountListDto = new ArrayList<AccountDto>();

        for(Account account : accountList){
            accountListDto.add(AccountDto.fromAccount(account));
        }
        return accountListDto;
    }

    public AccountAdminViewDto viewAccount(String account) {
        var accountFound = accountRepository.findAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account not found."));
        return AccountAdminViewDto.fromAccount(accountFound);
    }

    public AccountAdminViewDto updateAccount(String account, Optional<Long> secondaryOwnerId, Optional<BigDecimal> minimumBalance, Optional<BigDecimal> creditLimit, Optional<Double> interests) {
        var accountFound = accountRepository.findAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account not found."));
        if (secondaryOwnerId.isPresent() && secondaryOwnerId.get() == 0L){
            accountFound.setSecondaryOwner(null);
        }else {
            secondaryOwnerId.ifPresent(aLong -> accountFound.setSecondaryOwner((AccountHolder) userRepository.findById(aLong).orElseThrow(
                    () -> new EspecificException("Secondary Owner Id must be an Id of User List"))));
        }
        if (minimumBalance.isPresent()) {
            if(accountFound instanceof CheckingAccount){
                ((CheckingAccount) accountFound).setMinimumBalance(minimumBalance.get());
            }
            if(accountFound instanceof SavingAccount){
                ((SavingAccount) accountFound).setMinimumBalance(minimumBalance.get());
            }
        }
        creditLimit.ifPresent(bigDecimal -> ((CreditCardAccount) accountFound).setCreditLimit(bigDecimal));
        if (interests.isPresent()) {
            if(accountFound instanceof CreditCardAccount){
                ((CreditCardAccount) accountFound).setInterests(interests.get());
            }
            if(accountFound instanceof SavingAccount){
                ((SavingAccount) accountFound).setInterests(interests.get());
            }
        }
        return AccountAdminViewDto.fromAccount(accountRepository.save(accountFound));
    }

    public String activateUser(Long id) {
        User foundUser = accountUtils.getUserById(id);
        foundUser.setIsAccountNonLocked(true);
        userRepository.save(foundUser);
        return "User id: "+id+", username: "+foundUser.getUsername()+", is now ACTIVATED";
    }

    public String desactivateUser(Long id) {
        User foundUser = accountUtils.getUserById(id);
        foundUser.setIsAccountNonLocked(false);
        userRepository.save(foundUser);
        return "User id: "+id+", username: "+foundUser.getUsername()+", is now DESACTIVATED";
    }

    public AccountDto freezeAccount(String account, Integer action) {
        if(action != 0 ||  action != 1){
            throw new EspecificException("Please put 'action' param. (0 = FREEZE, 1 = ACTIVE)");
        }
        var accountFound = accountRepository.findAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account not found."));
        if(action == 1){
            accountFound.setStatus(Status.ACTIVE);
        }
        if(action == 0){
            accountFound.setStatus(Status.FREEZE);
        }
        return AccountDto.fromAccount(accountRepository.save(accountFound));
    }
}

