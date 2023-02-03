package com.ironhack.ironbank.model.entities.accounts;


import com.ironhack.ironbank.model.entities.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "student_accounts")
public class StudentAccount extends Account {

    public StudentAccount(AccountHolder accountHolder) {
        super(accountHolder);
    }
}
