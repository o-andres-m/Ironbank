package com.ironhack.ironbank.dto.account;

import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AccountListDto {

    private String number;

    private Money balance;

    private Status status;

    public static List<AccountListDto> fromAccountList (List<Account> accountList){
        var accountListDto = new ArrayList<AccountListDto>();
        for(Account account : accountList){
            var accountDto = new AccountListDto();
            accountDto.setNumber(account.getNumber());
            accountDto.setBalance(account.getBalance());
            accountDto.setStatus(account.getStatus());
            accountListDto.add(accountDto);
        }
        return accountListDto;
    }
}
