package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.exception.UserNotFoundException;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.AccountMap;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.AccountMapRepository;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.service.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThirdPartyService {

    private final UserRepository userRepository;

    private final AccountUtils accountUtils;
    private final AccountMapRepository accountMapRepository;



    public ThirdPartyDto createUser(ThirdPartyDto thirdPartyDto) {

        var user = thirdPartyDto.getUsername();
        accountUtils.verifyUserExists(user);

        var thirdParty = accountUtils.createThirdParty(thirdPartyDto);
        //When thirdparty register himself, need to activate account.
        thirdParty.setIsAccountNonLocked(false);
        return ThirdPartyDto.fromThirdParty(userRepository.save(thirdParty));
    }



    public List<AccountMap> registerAccount(String account, String secretkey) {
        var thirdParty = getThirdParty();

        // Faltaria verificar que la cuenta existe y la secretKey es correcta.

        var listOfAccountMap = accountMapRepository.findAllByThirdParty((ThirdParty) thirdParty);

        var flag = false;
        for(AccountMap accountMap : listOfAccountMap){
            if (accountMap.getAccountNumber().equals(account)) {
                flag = true;
                break;
            }
        }
        if (!flag){
            var accountMap = new AccountMap(account,secretkey);
            listOfAccountMap.add(accountMap);
        }
        return accountMapRepository.saveAll(listOfAccountMap);
    }

    private User getThirdParty() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var thirdParty = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundException(authentication.getName()));
        return thirdParty;
    }
}
