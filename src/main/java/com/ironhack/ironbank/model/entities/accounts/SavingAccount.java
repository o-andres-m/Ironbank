package com.ironhack.ironbank.model.entities.accounts;


import com.ironhack.ironbank.model.defaults.Interests;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "saving_accounts")
public class SavingAccount extends Account {

    private BigDecimal minimumBalance;

    @Embedded
    private Interests interests;

    public SavingAccount(AccountHolder accountHolder) {
        super(accountHolder);
        this.minimumBalance = BigDecimal.valueOf(1000);
        this.interests = new Interests(0.0025);
    }

    public void setInterests(Double value){
        if (value>0.5) value=0.5;
        if (value<0.0025) value=0.0025;
        this.interests.setValue(value);
    }
}
