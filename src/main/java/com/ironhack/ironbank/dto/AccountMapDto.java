package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.entities.AccountMap;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountMapDto {

    private String accountNumber;

    private String accountKey;


    public static AccountMapDto fromAccountMap(AccountMap accountMap){
        var accountMapDto = new AccountMapDto();

        accountMapDto.setAccountKey(accountMap.getAccountKey());
        accountMapDto.setAccountNumber(accountMap.getAccountNumber());

        return accountMapDto;
    }
}
