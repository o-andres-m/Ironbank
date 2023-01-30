package com.ironhack.ironbank.dto.users;

import com.ironhack.ironbank.model.entities.users.AccountHolder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AccountHolderDtoResponse {

    private Long id;

    private String username;

    private String nif;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String address;

    private String email;

    private String phone;


    public static AccountHolderDtoResponse fromAccountHolder(AccountHolder accountHolder){
        var accountHolderDtoResponse = new AccountHolderDtoResponse();
        accountHolderDtoResponse.setId(accountHolder.getId());
        accountHolderDtoResponse.setUsername(accountHolder.getUsername());
        accountHolderDtoResponse.setNif(accountHolder.getNif());
        accountHolderDtoResponse.setFirstName(accountHolder.getFirstName());
        accountHolderDtoResponse.setLastName(accountHolder.getLastName());
        accountHolderDtoResponse.setDateOfBirth(accountHolder.getDateOfBirth());
        accountHolderDtoResponse.setAddress(accountHolder.getAddress().getAddress());
        accountHolderDtoResponse.setEmail(accountHolder.getAddress().getEmail());
        accountHolderDtoResponse.setPhone(accountHolder.getAddress().getPhone());

        return accountHolderDtoResponse;
    }
}
