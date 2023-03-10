package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.accounts.CreditCardAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CheckingAccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.setting.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountUtils {

    private final CheckingAccountRepository checkingAccountRepository;

    private final AccountRepository accountRepository;

    private final TransactionUtils transactionUtils;


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

    public CheckingAccount findCheckingAccountByAccountHolder(AccountHolder accountHolder) {
        return checkingAccountRepository.findCheckingAccountByPrimaryOwner(accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Checking Account."));
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

    public void checkUserDoesntHaveCheckingAccount(Optional<CheckingAccount> checkingAccount) {
        if (checkingAccount.isPresent()){
            throw new EspecificException("User have already one Checking Account");
        }
    }

    public void checkCreditLimit(CreditCardAccount creditAccount, BigDecimal amount) {
        var limit = creditAccount.getCreditLimit().doubleValue();
        var creditAmount = creditAccount.getBalance().getAmount().doubleValue();
        if(limit - creditAmount - amount.doubleValue()<0)
            throw new EspecificException("Credit Card doesn't have credit limit.");
    }

    public void verifyAccountIsFromThisBank(String account, BigDecimal amount, User user) {
        var toAccount = accountRepository.findAccountByNumber(account);
        var name = "";
        if(user instanceof Admin) name = ((Admin) user).getFirstName();
        if(user instanceof AccountHolder) name = ((AccountHolder) user).getFirstName();


        if(toAccount.isPresent()){
            checkAccountNotFreezed(toAccount.get());
            toAccount.get().getBalance().increaseAmount(amount);
            transactionUtils.registerTransferFromThirdParty(toAccount.get(), amount, Settings.getBANK_NAME(), name);
        }
    }

    public void verifyAccountAndSecretKey(Account accountToCharge, String secretKey) {
        if(!accountToCharge.getSecretKey().equals(secretKey))
            throw new EspecificException("SecretKey Invalid.");
    }

    public void checkIsCheckingAccount(Account fromAccount) {
        if(!(fromAccount instanceof CheckingAccount)) throw new EspecificException("Account is not CheckingAccount.");

    }

    public void checkToApplyPenaltyCheckingAC(CheckingAccount checkingAccount, BigDecimal amount) {
        if(checkingAccount.getBalance().getAmount().subtract(amount).doubleValue() < checkingAccount.getMinimumBalance().doubleValue()) {
            checkingAccount.getBalance().decreaseAmount(checkingAccount.getPenaltyFee().getPenaltyAmount());
            transactionUtils.registerPenalty(checkingAccount, "AUTOMATIC");
        }
    }
}
