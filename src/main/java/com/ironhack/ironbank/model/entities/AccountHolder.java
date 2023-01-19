package com.ironhack.ironbank.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class AccountHolder extends User{

    private String address;

    private Integer age;

    @OneToMany (mappedBy = "primaryOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accountList;



}
