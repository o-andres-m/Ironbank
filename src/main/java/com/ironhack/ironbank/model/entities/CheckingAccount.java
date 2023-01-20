package com.ironhack.ironbank.model.entities;

import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.defaults.MontlyMaintenance;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "checking_accounts")
public class CheckingAccount extends Account{

    private BigDecimal minimumBalance;

    @Embedded
    private MontlyMaintenance montlyMaintenance;
}
