package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.account.AccountMapDto;
import com.ironhack.ironbank.dto.users.ThirdPartyDto;
import com.ironhack.ironbank.dto.transaction.TransactionDto;
import com.ironhack.ironbank.dto.transaction.TransferDto;
import com.ironhack.ironbank.dto.users.ThirdPartyDtoResponse;
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
import com.ironhack.ironbank.service.utils.UserUtils;
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

    private final UserUtils userUtils;

    private final AccountUtils accountUtils;

    private final AccountMapRepository accountMapRepository;

    private final AccountRepository accountRepository;

    private final TransactionUtils transactionUtils;

    private final FraudDetectionUtils fraudDetectionUtils;

    /**
     * START ACCOUNTHOLDERS LOGIC FOR ENDPOINTS
     */
    public ThirdPartyDtoResponse createUser(ThirdPartyDto thirdPartyDto) {
        userUtils.verifyUserExists(thirdPartyDto.getUsername());
        userUtils.verifyNifExists(thirdPartyDto.getNif());
        var thirdParty = userUtils.createThirdParty(thirdPartyDto);
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
        var accountToCharge = accountUtils.getAccountByNumber(account);

        accountUtils.verifyAccountAndSecretKey(accountToCharge,secretKey);
        accountUtils.checkAccountNotFreezed(accountToCharge);
        accountUtils.checkFinalBalance(accountToCharge,amount);

        accountToCharge.getBalance().decreaseAmount(amount);
        accountRepository.save(accountToCharge);
        return TransactionDto.fromTransaction(transactionUtils.registerChargeService(accountToCharge,amount,company));
    }

    public TransactionDto transferToAccount(TransferDto transferDto, String bankName, String name) {
        var accountToCredit = accountUtils.getAccountByNumber(transferDto.getToAccount());
        accountUtils.checkAccountNotFreezed(accountToCredit);

        accountToCredit.getBalance().increaseAmount(transferDto.getAmount());
        accountRepository.save(accountToCredit);
        return TransactionDto.fromTransaction(transactionUtils.registerTransferFromThirdParty(accountToCredit,transferDto.getAmount(),bankName,name));
    }


    public TransactionDto debitService(String account, BigDecimal amount) {
        var thirdParty = (ThirdParty) getThirdParty();

        var accountToCharge = accountUtils.getAccountByNumber(account);
        accountMapRepository.findAccountMapByAccountNumber(account).orElseThrow(
                ()-> new EspecificException("ThirdParty doesn't have registered the Account."));

        accountUtils.checkFinalBalance(accountToCharge,amount);
        accountUtils.checkAccountNotFreezed(accountToCharge);

        accountToCharge.getBalance().decreaseAmount(amount);
        accountRepository.save(accountToCharge);

        return TransactionDto.fromTransaction(transactionUtils.registerChargeService(accountToCharge,amount,thirdParty.getCompanyName()));
    }
}
