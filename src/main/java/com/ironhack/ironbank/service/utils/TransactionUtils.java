package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.entities.Transaction;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.accounts.CreditCardAccount;
import com.ironhack.ironbank.model.enums.TransactionType;
import com.ironhack.ironbank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionUtils {

    private final TransactionRepository transactionRepository;

    public void registerDeposit(Account account, BigDecimal amount){
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setObservations("Owner Deposit");
        transactionRepository.save(transaction);
    }

    public void registerWithdraw(Account account, BigDecimal amount){
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setObservations("Owner Withdraw");
        transactionRepository.save(transaction);
    }

    public void registerNewSavingAccount(Account account, BigDecimal amount) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setObservations("Create New Saving Account");
        transactionRepository.save(transaction);
    }

    public void fromCheckingtoSaving(Account account, BigDecimal amount,Account newSavigAccount) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.CREATE_SAVING_ACCOUNT);
        transaction.setObservations("Create New Saving Account -> "+newSavigAccount.getNumber());
        transactionRepository.save(transaction);
    }

    public void registerCreditBuy(Account account, BigDecimal amount, String store) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.PAY_WITH_CREDIT_CARD);
        transaction.setObservations("Payment at "+store);
        transactionRepository.save(transaction);
    }
}
