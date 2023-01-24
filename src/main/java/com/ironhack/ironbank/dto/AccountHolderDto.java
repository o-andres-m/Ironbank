package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.entities.users.AccountHolder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AccountHolderDto {

    private String username;

    private String password;

    private String roles;

    private String nif;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String address;

    private String email;

    private String phone;


    public static AccountHolderDto fromAccountHolder(AccountHolder accountHolder){
        var accountHolderDto = new AccountHolderDto();

        accountHolderDto.setUsername(accountHolder.getUsername());
        accountHolderDto.setPassword(accountHolder.getPassword());
        accountHolderDto.setRoles(accountHolder.getRoles());
        accountHolderDto.setNif(accountHolder.getNif());
        accountHolderDto.setFirstName(accountHolder.getFirstName());
        accountHolderDto.setLastName(accountHolder.getLastName());
        accountHolderDto.setDateOfBirth(accountHolder.getDateOfBirth());
        accountHolderDto.setAddress(accountHolder.getAddress().getAddress());
        accountHolderDto.setEmail(accountHolder.getAddress().getEmail());
        accountHolderDto.setPhone(accountHolder.getAddress().getPhone());

        return accountHolderDto;
    }
}
