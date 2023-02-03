package com.ironhack.ironbank.model.entities.accounts;


import com.ironhack.ironbank.model.defaults.Interests;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "credit_card_accounts")
public class CreditCardAccount extends Account {

    private BigDecimal creditLimit;

    @Embedded
    private Interests interests;

    public CreditCardAccount(AccountHolder accountHolder) {
        super(accountHolder);
        this.creditLimit = BigDecimal.valueOf(100);
        this.interests = new Interests(0.2);
    }

    public void setInterests(Double value){
        if (value>0.2) value=0.2;
        if (value<0.1) value=0.1;
        this.interests.setValue(value);
    }

    public void setCreditLimit(BigDecimal value){
        if (value.intValueExact()<100) value = BigDecimal.valueOf(100);
        if (value.intValueExact()>100000) value = BigDecimal.valueOf(100000);
        this.creditLimit = value;
    }
}
