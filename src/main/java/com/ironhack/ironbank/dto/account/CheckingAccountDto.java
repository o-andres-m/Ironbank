package com.ironhack.ironbank.dto.account;

import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.defaults.PenaltyFee;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.Transaction;
import com.ironhack.ironbank.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CheckingAccountDto {

    private String number;

    private String secretKey;

    private Money balance;

    private AccountHolder primaryOwner;

    private AccountHolder secondaryOwner;

    private Status status;

    private List<Transaction> transactionList;

    private PenaltyFee penaltyFee;


}
