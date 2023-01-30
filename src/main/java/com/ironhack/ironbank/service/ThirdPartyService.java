package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountMapDto;
import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.dto.TransactionDto;
import com.ironhack.ironbank.dto.TransferDto;
import com.ironhack.ironbank.dto.response.ThirdPartyDtoResponse;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.exception.UserNotFoundException;
import com.ironhack.ironbank.model.entities.AccountMap;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.AccountMapRepository;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.service.utils.AccountUtils;
import com.ironhack.ironbank.service.utils.FraudDetectionUtils;
import com.ironhack.ironbank.service.utils.TransactionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThirdPartyService {

    private final UserRepository userRepository;

    private final AccountUtils accountUtils;

    private final AccountMapRepository accountMapRepository;

    private final AccountRepository accountRepository;

    private final TransactionUtils transactionUtils;

    private final FraudDetectionUtils fraudDetectionUtils;



    public ThirdPartyDtoResponse createUser(ThirdPartyDto thirdPartyDto) {

        var user = thirdPartyDto.getUsername();
        accountUtils.verifyUserExists(user);

        var thirdParty = accountUtils.createThirdParty(thirdPartyDto);
        //When thirdparty register himself, need to activate account.
        thirdParty.setIsAccountNonLocked(false);
        return ThirdPartyDtoResponse.fromThirdParty(userRepository.save(thirdParty));
    }



    public AccountMapDto registerAccount(String account, String secretkey) {
        var thirdParty = getThirdParty();
        var accountToAdd = accountRepository.findAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account doesn't exists.")
        );
        if(accountToAdd.getSecretKey().equals(secretkey)){

            var foundAccount = accountMapRepository.findAccountMapByAccountNumberAndThirdPartyUsername(account,thirdParty.getUsername());
            if(foundAccount.isPresent()) throw new EspecificException("Account is already registered");

            var accountMap = new AccountMap(account,secretkey);
            accountMap.setThirdParty((ThirdParty) thirdParty);
            accountMapRepository.save(accountMap);
            return AccountMapDto.fromAccountMap(accountMap);
        }else{
            throw new EspecificException("Account or SecretKey incorrect.");
        }
    }


    private User getThirdParty() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var thirdParty = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundException(authentication.getName()));
        return thirdParty;
    }

    public List<AccountMapDto> allAcountMap() {
        var thirdParty = getThirdParty();
        var listOfAcountMap = accountMapRepository.findAllByThirdParty(thirdParty);
        var listOfAccountMapDto = new ArrayList<AccountMapDto>();
        for(AccountMap accountMap : listOfAcountMap){
            listOfAccountMapDto.add(AccountMapDto.fromAccountMap(accountMap));
        }
        return listOfAccountMapDto;
    }


    public TransactionDto chargeService(String company, String account, String secretKey, BigDecimal amount) {

        var accountToCharge = accountRepository.findAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account doesn't exists."));
        if (accountToCharge.getSecretKey().equals(secretKey)){
            // TODO : Validar que no quede en negativo + el fraud detection
            accountUtils.cehckFinalBalance(accountToCharge,amount);
            // TODO fraudDetectionUtils
            accountToCharge.getBalance().decreaseAmount(amount);
            accountRepository.save(accountToCharge);

            return TransactionDto.fromTransaction(transactionUtils.registerChargeService(accountToCharge,amount,company));
        }else {
            throw new EspecificException("Secret Key invalid.");
        }
    }

    public TransactionDto transferToAccount(TransferDto transferDto, String bankName, String name) {

        var accountToCredit = accountRepository.findAccountByNumber(transferDto.getToAccount()).orElseThrow(
                ()-> new EspecificException("Account doesn't exists."));
        accountToCredit.getBalance().increaseAmount(transferDto.getAmount());
        accountRepository.save(accountToCredit);
        return TransactionDto.fromTransaction(transactionUtils.registerTransferFromThirdParty(accountToCredit,transferDto.getAmount(),bankName,name));
    }
}
