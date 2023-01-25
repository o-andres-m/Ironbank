package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDto;
import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.exception.UserNotFoundException;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.accounts.*;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CheckingAccountRepository;
import com.ironhack.ironbank.repository.CreditCardAccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.service.utils.AccountUtils;
import com.ironhack.ironbank.service.utils.TransactionUtils;
import com.ironhack.ironbank.service.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HoldersService {

    private final UserRepository userRepository;

    private final AccountUtils accountUtils;

    private final AccountRepository accountRepository;

    private final CheckingAccountRepository checkingAccountRepository;

    private final CreditCardAccountRepository creditCardAccountRepository;

    private final TransactionUtils transactionUtils;



    public AccountHolderDto register(AccountHolderDto accountHolderDto) {

        var user = accountHolderDto.getUsername();
        accountUtils.verifyUserExists(user);

        if (Utils.calculateAge(accountHolderDto.getDateOfBirth())<18) {
            throw new EspecificException("You must have 18 years for register. Please go to the Bank.");
        }else{
            var accountHolder = accountUtils.createAccountHolder(accountHolderDto, user);
            return AccountHolderDto.fromAccountHolder(userRepository.save(accountHolder));
            }
        }



    /*
    Method to get the account Holder
     */

    private User getAccountHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var accountHolder = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundException(authentication.getName()));
        return accountHolder;
    }

    public AccountDto createCheckingAccount() {
        User accountHolder = getAccountHolder();

        var checkingAccount = checkingAccountRepository.findCheckingAccountByPrimaryOwner((AccountHolder) accountHolder);
        if (checkingAccount.isPresent()){
            throw new EspecificException("Have already one Checking Account");
        }

        if (Utils.isOver24(Utils.calculateAge(((AccountHolder) accountHolder).getDateOfBirth()))) {
            var accountCreated = new CheckingAccount((AccountHolder) accountHolder);
            accountRepository.save(accountCreated);
            return AccountDto.fromAccount(accountCreated);
        } else {
            var accountCreated = new StudentAccount((AccountHolder) accountHolder);
            accountRepository.save(accountCreated);
            return AccountDto.fromAccount(accountCreated);
        }
    }


    public AccountDto createSavingAccount(BigDecimal amount) {
        if (amount.intValueExact()<100) throw new EspecificException("Minimum amount: 100");
        var accountHolder = getAccountHolder();
        var checkingAccount = checkingAccountRepository.findCheckingAccountByPrimaryOwner((AccountHolder) accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Checking Account."));
        var balance = checkingAccount.getBalance().getAmount().doubleValue();
        if(balance<amount.doubleValue()){
            throw new EspecificException("Account doesn't have founds.");
        }else{
                Account accountCreated = new SavingAccount((AccountHolder) accountHolder);
                accountCreated.getBalance().increaseAmount(amount);
                accountRepository.save(accountCreated);
                // Register new SavingAccount
                transactionUtils.registerNewSavingAccount(accountCreated,amount);
                // Register less balance in CheckingAccount
                transactionUtils.fromCheckingtoSaving(checkingAccount,amount,accountCreated);
                // Update new balance in checkingAccount
                checkingAccount.getBalance().decreaseAmount(amount);
                accountRepository.save(checkingAccount);
                return AccountDto.fromAccount(accountCreated);
        }
    }

    public AccountDto createCreditAccount() {
        User accountHolder = getAccountHolder();
        var checkingAccount = checkingAccountRepository.findCheckingAccountByPrimaryOwner((AccountHolder) accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Checking Account."));
        var accountCreated = new CreditCardAccount((AccountHolder) accountHolder);
        accountRepository.save(accountCreated);
        return AccountDto.fromAccount(accountCreated);
    }


    public AccountDto depositInCheckingAccount(BigDecimal amount) {
        User accountHolder = getAccountHolder();
        var checkingAccount = checkingAccountRepository.findCheckingAccountByPrimaryOwner((AccountHolder) accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Checking Account."));
        checkingAccount.getBalance().increaseAmount(amount);
        checkingAccountRepository.save(checkingAccount);
        transactionUtils.registerDeposit(checkingAccount,amount);
        return AccountDto.fromAccount(checkingAccount);
    }

    public AccountDto buyWithCredit(BigDecimal amount,String store) {
        User accountHolder = getAccountHolder();
        var creditAccount = creditCardAccountRepository.findCreditCardAccountByPrimaryOwner((AccountHolder) accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Credit Card."));
        var limit = creditAccount.getCreditLimit().doubleValue();
        var creditAmount = creditAccount.getBalance().getAmount().doubleValue();

        if(limit-creditAmount-amount.doubleValue()>0) {
            creditAccount.getBalance().increaseAmount(amount);
            creditCardAccountRepository.save(creditAccount);
            transactionUtils.registerCreditBuy(creditAccount,amount,store);
        }else {
            throw new EspecificException("Credit Card doesn't have credit limit.");
        }
        return AccountDto.fromAccount(creditAccount);
    }

    public AccountDto withdraw(BigDecimal amount) {
        User accountHolder = getAccountHolder();
        var checkingAccount = checkingAccountRepository.findCheckingAccountByPrimaryOwner((AccountHolder) accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Checking Account."));
        var availableAmount = checkingAccount.getBalance().getAmount().doubleValue();

        if(availableAmount>amount.doubleValue()){
            checkingAccount.getBalance().decreaseAmount(amount);
            transactionUtils.registerWithdraw(checkingAccount,amount);
        }else {
            throw new EspecificException("You don't have founds.");
        }
        return AccountDto.fromAccount(checkingAccount);
    }
}
