package com.ironhack.ironbank.model.entities.accounts;

import com.ironhack.ironbank.model.entities.users.AccountHolder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "checking_accounts")
public class CheckingAccount extends Account {

    private BigDecimal minimumBalance;

    private BigDecimal montlyMaintenance;

    public CheckingAccount(AccountHolder primaryOwner) {
        super(primaryOwner);
        this.minimumBalance = BigDecimal.valueOf(250);
        this.montlyMaintenance = BigDecimal.valueOf(12);
    }
}
