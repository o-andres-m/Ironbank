package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.AccountHolder;
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

    private Address address;
    // TODO: Preguntar si es correcto recibir un ADDRESS o por separado...
    //private String email;

    public static AccountHolderDto fromAccountHolder(AccountHolder accountHolder){
        var accountHolderDto = new AccountHolderDto();

        accountHolderDto.setUsername(accountHolder.getUsername());
        accountHolderDto.setPassword(accountHolder.getPassword());
        accountHolderDto.setRoles(accountHolder.getRoles());
        accountHolderDto.setNif(accountHolder.getNif());
        accountHolderDto.setFirstName(accountHolder.getFirstName());
        accountHolderDto.setLastName(accountHolder.getLastName());
        accountHolderDto.setDateOfBirth(accountHolder.getDateOfBirth());
        accountHolderDto.setAddress(accountHolder.getAddress());
        return accountHolderDto;
    }
}
