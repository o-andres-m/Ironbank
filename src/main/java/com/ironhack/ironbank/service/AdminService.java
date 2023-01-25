package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.dto.AdminDto;
import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.service.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final AccountUtils accountUtils;

    private final PasswordEncoder passwordEncoder;


    /**
     * Rgister Methods
     */
    public AccountHolderDto registerAH(AccountHolderDto accountHolderDto) {
        var user = accountHolderDto.getUsername();
        accountUtils.verifyUserExists(user);

        var accountHolder = accountUtils.createAccountHolder(accountHolderDto, user);
        return AccountHolderDto.fromAccountHolder(userRepository.save(accountHolder));
    }

    public ThirdPartyDto registerTP(ThirdPartyDto thirdPartyDto) {
        var user = thirdPartyDto.getUsername();
        accountUtils.verifyUserExists(user);

        var thirdParty = accountUtils.createThirdParty(thirdPartyDto);
        return ThirdPartyDto.fromThirdParty(userRepository.save(thirdParty));

    }

    public AdminDto registerAdmin(AdminDto adminDto) {
        var user = adminDto.getUsername();
        accountUtils.verifyUserExists(user);

        var admin = accountUtils.createAdmin(adminDto);
        return AdminDto.fromAdmin(userRepository.save(admin));

    }

    /**
     * Search Methods
     */
    public List<AccountHolderDto> searchAH(Optional<String> username, Optional<String> firstName, Optional<String> lastName, Optional<String> nif, Optional<String> phone) {

        var listOfAH2 = userRepository.searchAccountHolders(username.orElse(""),
                firstName.orElse(""),lastName.orElse(""),
                nif.orElse(""),phone.orElse(""));

        var listOfAHDto = new ArrayList<AccountHolderDto>();
        for (User user : listOfAH2){
            listOfAHDto.add(AccountHolderDto.fromAccountHolder((AccountHolder) user));
        }
        return listOfAHDto;
    }

    public List<ThirdPartyDto> searchTP(Optional<String> username, Optional<String> companyName, Optional<String> nif) {

        var listOfTP = userRepository.searchThirdParty(username.orElse(""),
                companyName.orElse(""), nif.orElse(""));

        var listOfTPDto = new ArrayList<ThirdPartyDto>();
        for (User user : listOfTP){
            listOfTPDto.add(ThirdPartyDto.fromThirdParty((ThirdParty) user));
        }
        return listOfTPDto;
    }

    /**
     * Modify Methods
     */


}

