package com.ironhack.ironbank.model.entities;

import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.model.defaults.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class AccountHolder extends User{

    private String nif;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    @Embedded
    private Address address;

    @OneToMany (mappedBy = "primaryOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accountList;

    public AccountHolder fromDto(AccountHolderDto accountHolderDto){
        var accountHolder = new AccountHolder();

        accountHolder.setUsername(accountHolderDto.getUsername());
        accountHolder.setPassword(accountHolderDto.getPassword());
        accountHolder.setNif(accountHolderDto.getNif());
        accountHolder.setFirstName(accountHolderDto.getFirstName());
        accountHolder.setLastName(accountHolderDto.getLastName());
        accountHolder.setDateOfBirth(accountHolderDto.getDateOfBirth());
        accountHolder.setAddress(accountHolderDto.getAddress());
        return accountHolder;
    }


}
