package com.ironhack.ironbank.model.entities;


import com.ironhack.ironbank.model.defaults.Interests;
import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.defaults.MontlyMaintenance;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "credit_card_accounts")
public class CreditCardAccount extends Account{

    private BigDecimal minimumBalance;

    private BigDecimal creditLimit;

    @Embedded
    private Interests interests;
}
