package com.ironhack.ironbank.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.defaults.PenaltyFee;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.Transaction;
import com.ironhack.ironbank.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AccountDto {

    private String number;

    private String secretKey;

    private Money balance;

    private AccountHolderDtoResponse primaryOwner;

    private AccountHolderDtoResponse secondaryOwner;

    private Status status;

    @JsonIgnore
    private List<Transaction> transactionList;

    @JsonIgnore
    private PenaltyFee penaltyFee;

    public static AccountDto fromAccount(Account account){
        var accountDto = new AccountDto();

        accountDto.setNumber(account.getNumber());
        accountDto.setSecretKey(account.getSecretKey());
        accountDto.setBalance(account.getBalance());
        accountDto.setPrimaryOwner(AccountHolderDtoResponse.fromAccountHolder(account.getPrimaryOwner()));
        if(account.getSecondaryOwner() != null)accountDto.setSecondaryOwner(AccountHolderDtoResponse.fromAccountHolder(account.getSecondaryOwner()));
        accountDto.setStatus(account.getStatus());
        accountDto.setTransactionList(account.getTransactionList());
        accountDto.setPenaltyFee(account.getPenaltyFee());

        return accountDto;
    }
}
