package com.ironhack.ironbank.model.entities;


import com.ironhack.ironbank.model.defaults.Interests;
import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.defaults.MontlyMaintenance;
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
public class SavingAccount extends Account{

    private BigDecimal minimumBalance;

    @Embedded
    private Interests interests;
}
