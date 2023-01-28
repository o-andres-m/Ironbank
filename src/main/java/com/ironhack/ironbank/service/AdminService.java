package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDto;
import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.dto.AdminDto;
import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.dto.response.AccountHolderDtoResponse;
import com.ironhack.ironbank.dto.response.AdminDtoResponse;
import com.ironhack.ironbank.dto.response.ThirdPartyDtoResponse;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.service.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        accountUtils.verifyUserExists(user);

        var accountHolder = accountUtils.createAccountHolder(accountHolderDto, user);
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

        var foundUser = userRepository.findById(id).orElseThrow(()-> new EspecificException("User with ID "+id+" not found."));
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

        var foundUser = userRepository.findById(id).orElseThrow(()-> new EspecificException("User with ID "+id+" not found."));
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

        var foundUser = userRepository.findById(id).orElseThrow(()-> new EspecificException("User with ID "+id+" not found."));
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
}

