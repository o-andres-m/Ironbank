package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.account.AccountDto;
import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.transaction.TransactionDto;
import com.ironhack.ironbank.dto.info.AccountHolderInfoDto;
import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.exception.UserNotFoundException;
import com.ironhack.ironbank.model.entities.Transaction;
import com.ironhack.ironbank.model.entities.accounts.*;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.repository.*;
import com.ironhack.ironbank.service.utils.*;
import com.ironhack.ironbank.setting.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HoldersService {

    private final AccountHolderRepository accountHolderRepository;

    private final UserRepository userRepository;

    private final UserUtils userUtils;

    private final AccountRepository accountRepository;

    private final CheckingAccountRepository checkingAccountRepository;

    private final CreditCardAccountRepository creditCardAccountRepository;

    private final SavingAccountRepository savingAccountRepository;

    private final AccountUtils accountUtils;

    private final FraudDetectionUtils fraudDetectionUtils;

    private final TransactionRepository transactionRepository;

    private final TransactionUtils transactionUtils;

    private final PasswordEncoder passwordEncoder;


    /*
    Method to get the account Holder
     */
    private User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var accountHolder = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundException(authentication.getName()));
        return accountHolder;
    }

    /**
     * START ACCOUNTHOLDERS LOGIC FOR ENDPOINTS
     */

    public AccountHolderDtoResponse register(AccountHolderDto accountHolderDto) {
        accountUtils.verifyUserExists(accountHolderDto.getUsername());
        accountUtils.verifyNifExists(accountHolderDto.getNif());

        if (Utils.calculateAge(accountHolderDto.getDateOfBirth())<18) {
            throw new EspecificException("You must have 18 years for register. Please go to the Bank.");
        }else{
            var accountHolder = accountUtils.createAccountHolder(accountHolderDto);
            return AccountHolderDtoResponse.fromAccountHolder(userRepository.save(accountHolder));
            }
        }

    public AccountHolderInfoDto viewPersonalInfo() {
        User accountHolder = getLoginUser();
        return AccountHolderInfoDto.fromAccountHolder((AccountHolder) userRepository.findByUsername(accountHolder.getUsername()).get());
    }

    public AccountHolderDtoResponse update(Optional<String> username, Optional<String> password, Optional<String> address, Optional<String> phone, Optional<String> email) {
        username.ifPresent(accountUtils::verifyUserExists);
        var accountHolder = (AccountHolder) getLoginUser();
        username.ifPresent(accountHolder::setUsername);
        password.ifPresent(s -> accountHolder.setPassword(passwordEncoder.encode(s)));
        var accountHolderAddress = accountHolder.getAddress();
        address.ifPresent(accountHolderAddress::setAddress);
        phone.ifPresent(accountHolderAddress::setPhone);
        email.ifPresent(accountHolderAddress::setEmail);
        accountHolder.setAddress(accountHolderAddress);
        return AccountHolderDtoResponse.fromAccountHolder(userRepository.save(accountHolder));
    }

    public String forgotPassword(String nif, String email) {
        var accountHolder = accountHolderRepository.findAccountHolderByNif(nif).orElseThrow(
                ()-> new EspecificException("Nif not registered."));
        if(accountHolder.getAddress().getEmail().equals(email)){
            return "An email has been sent to "+email+", please follow instructions to restore your password.";
        }else{
            return "Nif and Email doesn't match, please go to the Bank to reset your password.";
        }
    }

    public AccountDto setSecondaryOwner(String account, String secondaryOwnerNif) {
        User accountHolder = getLoginUser();
        var accountToUpdate= accountUtils.getAndVerifyAccount(account, accountHolder);
        if (secondaryOwnerNif.equals("0")){
            accountToUpdate.setSecondaryOwner(null);
        }else {
            var secondaryOwner = userUtils.getUserByNif(secondaryOwnerNif);
            accountToUpdate.setSecondaryOwner(secondaryOwner);
        }
        return AccountDto.fromAccount(accountRepository.save(accountToUpdate));
    }

    /**
     * ACCOUNTS
     */

    public AccountDto createCheckingAccount() {
        User accountHolder = getLoginUser();

        var checkingAccount = checkingAccountRepository.findCheckingAccountByPrimaryOwner((AccountHolder) accountHolder);
        accountUtils.checkUserDoesntHaveCheckingAccount(checkingAccount);

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

    public TransactionDto createSavingAccount(BigDecimal amount) {
        if (amount.intValueExact()<1000) throw new EspecificException("Minimum amount: 1000");
        var accountHolder = getLoginUser();
        var checkingAccount = accountUtils.checkUserHaveCheckingAccount(accountHolder);

        accountUtils.checkAccountNotFreezed(checkingAccount);
        accountUtils.checkFinalBalance(checkingAccount,amount);
        fraudDetectionUtils.verifyRecurrentOperations(checkingAccount);
        fraudDetectionUtils.verifyExpensiveOperation(checkingAccount,amount);

        Account accountCreated = new SavingAccount((AccountHolder) accountHolder);
        accountCreated.getBalance().increaseAmount(amount);
        accountRepository.save(accountCreated);

        // Register new SavingAccount
        var transaction = transactionUtils.registerNewSavingAccount(accountCreated,amount);
        // Register less balance in CheckingAccount
        transactionUtils.fromCheckingtoSaving(checkingAccount,amount,accountCreated);
        // Update new balance in checkingAccount
        checkingAccount.getBalance().decreaseAmount(amount);
        accountRepository.save(checkingAccount);
        return TransactionDto.fromTransaction(transaction);

    }

    public AccountDto createCreditAccount() {
        User accountHolder = getLoginUser();
        accountUtils.findCheckingAccountByAccountHolder((AccountHolder) accountHolder);
        var accountCreated = new CreditCardAccount((AccountHolder) accountHolder);
        accountRepository.save(accountCreated);
        return AccountDto.fromAccount(accountCreated);
    }


    public TransactionDto depositInCheckingAccount(BigDecimal amount) {
        User accountHolder = getLoginUser();
        var checkingAccount = accountUtils.checkUserHaveCheckingAccount(accountHolder);
        checkingAccount.getBalance().increaseAmount(amount);
        accountRepository.save(checkingAccount);
        return TransactionDto.fromTransaction(transactionUtils.registerDeposit(checkingAccount,amount));
    }

    public TransactionDto depositSavingAccount(String account, BigDecimal amount) {
        var accountHolder = getLoginUser();
        Account savingAccount = savingAccountRepository.findSavingAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account not found."));
        accountUtils.getAndVerifyAccount(account,accountHolder);
        savingAccount.getBalance().increaseAmount(amount);
        accountRepository.save(savingAccount);
        return TransactionDto.fromTransaction(transactionUtils.registerDepositSavingAccount(savingAccount, amount));
    }

    public TransactionDto depositSavingAccountFromChecking(String account, BigDecimal amount) {
        var accountHolder = getLoginUser();
        var checkingAccount = accountUtils.findCheckingAccountByAccountHolder((AccountHolder) accountHolder);
        accountUtils.getAndVerifyAccount(account,accountHolder);
        accountUtils.checkFinalBalance(checkingAccount,amount);
        fraudDetectionUtils.verifyExpensiveOperation(checkingAccount, amount);
        fraudDetectionUtils.verifyRecurrentOperations(checkingAccount);

        Account savingAccount = savingAccountRepository.findSavingAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account not found."));
        savingAccount.getBalance().increaseAmount(amount);
        accountRepository.save(savingAccount);
        // Register new SavingAccount
        var transaction = transactionUtils.registerDepositSavingAccount(savingAccount, amount);
        // Register less balance in CheckingAccount
        transactionUtils.fromCheckingtoSaving(checkingAccount, amount, savingAccount);
        // Update new balance in checkingAccount
        checkingAccount.getBalance().decreaseAmount(amount);
        accountRepository.save(checkingAccount);
        return TransactionDto.fromTransaction(transaction);
    }

    public AccountDto buyWithCredit(BigDecimal amount,String store) {
        User accountHolder = getLoginUser();
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

    public TransactionDto withdraw(BigDecimal amount) {
        User accountHolder = getLoginUser();

        var checkingAccount = accountUtils.checkUserHaveCheckingAccount(accountHolder);
        fraudDetectionUtils.verifyExpensiveOperation(checkingAccount,amount);
        fraudDetectionUtils.verifyRecurrentOperations(checkingAccount);
        accountUtils.checkAccountNotFreezed(checkingAccount);
        accountUtils.checkFinalBalance(checkingAccount,amount);

        checkingAccount.getBalance().decreaseAmount(amount);
        accountRepository.save(checkingAccount);
        return TransactionDto.fromTransaction(transactionUtils.registerWithdraw(checkingAccount,amount));
    }


    public List<AccountDto> allAccounts() {
        User accountHolder = getLoginUser();
        var accountList = accountRepository.findAccountByPrimaryOwner_Username(accountHolder.getUsername());
        var accountListDto = new ArrayList<AccountDto>();

        for(Account account : accountList){
            accountListDto.add(AccountDto.fromAccount(account));
        }
        return accountListDto;
    }

    public AccountDto viewAccount(String account) {
        User accountHolder = getLoginUser();
        Account accountFound = accountUtils.getAndVerifyAccount(account, accountHolder);
        return AccountDto.fromAccount(accountFound);
    }

    public List<TransactionDto> viewTransactions(String account) {
        User accountHolder = getLoginUser();
        accountUtils.getAndVerifyAccount(account, accountHolder);
        var transactionList = transactionRepository.findTransactionsByAccount_NumberOrderByDateDesc(account);
        var transacionDtoList = new ArrayList<TransactionDto>();
        for(Transaction transaction : transactionList){
            transacionDtoList.add(TransactionDto.fromTransaction(transaction));
        }
        return transacionDtoList;
    }






    public TransactionDto trasnferToAccount(String account, BigDecimal amount) {
        User accountHolder = getLoginUser();
        var fromAccount = accountUtils.findCheckingAccountByAccountHolder((AccountHolder) accountHolder);
        //TODO: ver si el account pertenece al banco o es externo....
        var toAccount = accountRepository.findAccountByNumber(account);

        // TODO Verifiacr SALDO, FRAUD y demas...

        // Register our transaction
        fromAccount.getBalance().decreaseAmount(amount);
        var transaction = transactionUtils.registerTransferToAnotherAccount(fromAccount,amount,account);

        if(toAccount.isPresent()){
            //If toAccount is from our bank, register the transaction
            toAccount.get().getBalance().increaseAmount(amount);
            transactionUtils.registerTransferFromThirdParty(toAccount.get(), amount, Settings.getBANK_NAME(),((AccountHolder) accountHolder).getFirstName());
        }
        return TransactionDto.fromTransaction(transaction);
    }





    public TransactionDto withdrawSavingAccount(String account, BigDecimal amount) {
        var accountHolder = getLoginUser();
        SavingAccount savingAccount = savingAccountRepository.findSavingAccountByNumber(account).orElseThrow(
                ()-> new EspecificException("Account not found."));
        accountUtils.getAndVerifyAccount(account,accountHolder);
        accountUtils.checkFinalBalance(savingAccount,amount);
        accountUtils.checkMinimumBalance(savingAccount,amount);

        var transaction = new Transaction();

        if (accountUtils.checkFinalBalanceZero(savingAccount,amount)){
            savingAccount.getBalance().decreaseAmount(amount);
            savingAccount.setStatus(Status.CLOSED);
            transaction = transactionUtils.registerWithdraw(savingAccount,amount);
        } else if(savingAccount.getBalance().getAmount().subtract(amount).doubleValue() <
                    savingAccount.getMinimumBalance().doubleValue()) {
            savingAccount.getBalance().decreaseAmount(savingAccount.getPenaltyFee().getPenaltyAmount());
            transactionUtils.registerPenalty(savingAccount, "minimum balance exceed");
            savingAccount.getBalance().decreaseAmount(amount);
            transaction = transactionUtils.registerWithdraw(savingAccount, amount);
        } else{
            savingAccount.getBalance().decreaseAmount(amount);
            transaction = transactionUtils.registerWithdraw(savingAccount,amount);
        }

        return TransactionDto.fromTransaction(transaction);
    }
}
