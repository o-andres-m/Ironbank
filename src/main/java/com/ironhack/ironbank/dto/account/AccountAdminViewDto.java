package com.ironhack.ironbank.dto.account;

import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.accounts.CreditCardAccount;
import com.ironhack.ironbank.model.entities.accounts.SavingAccount;
import com.ironhack.ironbank.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
public class AccountAdminViewDto {

    private String number;

    private String secretKey;

    private BigDecimal balance;

    private AccountHolderDtoResponse primaryOwner;

    private AccountHolderDtoResponse secondaryOwner;

    private Status status;

    private Instant creationDate;

    private BigDecimal penaltyFee;

    private BigDecimal minimumBalance;

    private BigDecimal montlyMaintenance;

    private Double interests;

    private BigDecimal creditLimit;

    public static AccountAdminViewDto fromAccount(Account account){
        var accountAdminViewDto = new AccountAdminViewDto();

        accountAdminViewDto.setNumber(account.getNumber());
        accountAdminViewDto.setSecretKey(account.getSecretKey());
        accountAdminViewDto.setBalance(account.getBalance().getAmount());
        accountAdminViewDto.setPrimaryOwner(AccountHolderDtoResponse.fromAccountHolder(account.getPrimaryOwner()));
        accountAdminViewDto.setCreationDate(account.getCreationDate());
        if(account.getSecondaryOwner() != null)accountAdminViewDto.setSecondaryOwner(AccountHolderDtoResponse.fromAccountHolder(account.getSecondaryOwner()));
        accountAdminViewDto.setStatus(account.getStatus());
        accountAdminViewDto.setPenaltyFee(account.getPenaltyFee().getPenaltyAmount());
        if(account instanceof CreditCardAccount){
            accountAdminViewDto.setCreditLimit(((CreditCardAccount) account).getCreditLimit());
            accountAdminViewDto.setInterests(((CreditCardAccount) account).getInterests().getValue());
        }
        if(account instanceof CheckingAccount){
            accountAdminViewDto.setMinimumBalance(((CheckingAccount) account).getMinimumBalance());
            accountAdminViewDto.setMontlyMaintenance(((CheckingAccount) account).getMontlyMaintenance());
        }
        if(account instanceof SavingAccount){
            accountAdminViewDto.setMinimumBalance(((SavingAccount) account).getMinimumBalance());
            accountAdminViewDto.setInterests(((SavingAccount) account).getInterests().getValue());
        }
        return accountAdminViewDto;
    }
}
