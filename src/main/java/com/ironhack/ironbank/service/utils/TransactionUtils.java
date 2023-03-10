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

    public Transaction registerDeposit(Account account, BigDecimal amount){
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setObservations("Owner Deposit");
        return transactionRepository.save(transaction);
    }

    public Transaction registerAdminDeposit(Account account, BigDecimal amount, String user){
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setObservations("Admin balance adjust(DEPOSIT). User: "+user);
        return transactionRepository.save(transaction);
    }

    public Transaction registerWithdraw(Account account, BigDecimal amount){
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount.negate()));
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setObservations("Owner Withdraw");
        return transactionRepository.save(transaction);
    }

    public Transaction registerAdminWithdraw(Account account, BigDecimal amount, String user){
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount.negate()));
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setObservations("Admin balance adjust(WITHDRAW). User: "+user);
        return transactionRepository.save(transaction);
    }

    public Transaction registerNewSavingAccount(Account account, BigDecimal amount) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setObservations("Create New Saving Account");
        return transactionRepository.save(transaction);
    }

    public Transaction registerDepositSavingAccount(Account account, BigDecimal amount) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setObservations("Deposit to Saving Account");
        return transactionRepository.save(transaction);
    }

    public void fromCheckingtoSaving(Account account, BigDecimal amount,Account newSavingAccount) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount.negate()));
        transaction.setTransactionType(TransactionType.CREATE_SAVING_ACCOUNT);
        transaction.setObservations("Deposit to SavingAccount -> "+newSavingAccount.getNumber());
        transactionRepository.save(transaction);
    }

    public Transaction registerCreditBuy(Account account, BigDecimal amount, String store) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.PAY_WITH_CREDIT_CARD);
        transaction.setObservations("Payment at "+store);
        return transactionRepository.save(transaction);
    }

    public Transaction registerChargeService(Account account, BigDecimal amount, String company){
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount.negate()));
        transaction.setTransactionType(TransactionType.PAY_SERVICE);
        transaction.setObservations("Service Debit in "+company);
        return transactionRepository.save(transaction);
    }

    public Transaction registerTransferFromThirdParty(Account accountToCredit,BigDecimal amount, String bankName, String name) {
        var transaction = new Transaction();
        transaction.setAccount(accountToCredit);
        transaction.setAmount(new Money(amount));
        transaction.setTransactionType(TransactionType.CREDIT_FROM_TRANSFER);
        transaction.setObservations("Transfer from "+name+" (Bank: "+bankName+")");
        return transactionRepository.save(transaction);
    }

    public Transaction registerTransferToAnotherAccount(CheckingAccount fromAccount, BigDecimal amount, String account) {
        var transaction = new Transaction();
        transaction.setAccount(fromAccount);
        transaction.setAmount(new Money(amount.negate()));
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setObservations("Transfer to "+account);
        return transactionRepository.save(transaction);
    }

    public Transaction registerPenalty(Account accountFound, String username) {
        var transaction = new Transaction();
        transaction.setAccount(accountFound);
        transaction.setAmount(new Money(accountFound.getPenaltyFee().getPenaltyAmount().negate()));
        transaction.setTransactionType(TransactionType.PENALTY);
        transaction.setObservations("PenaltyFee applied by "+username);
        return transactionRepository.save(transaction);
    }

    public Transaction registerInterests(Account account, double interests) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(BigDecimal.valueOf(interests)));
        transaction.setTransactionType(TransactionType.INTERESTS);
        transaction.setObservations("Interests Applied");
        return transactionRepository.save(transaction);
    }

    public Transaction registerMonthlyMaintenance(Account account, BigDecimal amount) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(amount.negate()));
        transaction.setTransactionType(TransactionType.MONTHLY_MAINTENANCE);
        transaction.setObservations("Monthly Maintenance Applied");
        return transactionRepository.save(transaction);
    }

    public Transaction registerDebitCreditCard(Account account, CreditCardAccount creditCard) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new Money(creditCard.getBalance().getAmount().negate()));
        transaction.setTransactionType(TransactionType.DEBIT_CREDIT_CARD);
        transaction.setObservations("Payment CreditCard N?? "+creditCard.getNumber());
        return transactionRepository.save(transaction);
    }

    public Transaction registerPaymentCreditCard(CreditCardAccount creditCard, CheckingAccount checkingAccount) {
        var transaction = new Transaction();
        transaction.setAccount(creditCard);
        transaction.setAmount(new Money(creditCard.getBalance().getAmount().negate()));
        transaction.setTransactionType(TransactionType.DEBIT_CREDIT_CARD);
        transaction.setObservations("CreditCard Debited from Account N?? "+checkingAccount.getNumber());
        return transactionRepository.save(transaction);
    }
}
