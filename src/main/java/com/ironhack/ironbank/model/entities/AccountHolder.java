package com.ironhack.ironbank.model.entities;

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


}
