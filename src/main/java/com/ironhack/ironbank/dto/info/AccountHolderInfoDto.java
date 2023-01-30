package com.ironhack.ironbank.dto.info;

import com.ironhack.ironbank.dto.account.AccountListDto;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class AccountHolderInfoDto {

    private Long id;

    private String username;

    private String nif;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String address;

    private String email;

    private String phone;

    private List<AccountListDto> accountList;


    public static AccountHolderInfoDto fromAccountHolder(AccountHolder accountHolder){
        var accountHolderDtoResponse = new AccountHolderInfoDto();
        accountHolderDtoResponse.setId(accountHolder.getId());
        accountHolderDtoResponse.setUsername(accountHolder.getUsername());
        accountHolderDtoResponse.setNif(accountHolder.getNif());
        accountHolderDtoResponse.setFirstName(accountHolder.getFirstName());
        accountHolderDtoResponse.setLastName(accountHolder.getLastName());
        accountHolderDtoResponse.setDateOfBirth(accountHolder.getDateOfBirth());
        accountHolderDtoResponse.setAddress(accountHolder.getAddress().getAddress());
        accountHolderDtoResponse.setEmail(accountHolder.getAddress().getEmail());
        accountHolderDtoResponse.setPhone(accountHolder.getAddress().getPhone());
        accountHolderDtoResponse.setAccountList(AccountListDto.fromAccountList(accountHolder.getAccountList()));

        return accountHolderDtoResponse;
    }
}
