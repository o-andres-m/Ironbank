package com.ironhack.ironbank.model.entities;

import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Embedded
    private Money amount;

    @CreationTimestamp
    private Instant date;

    private String observations;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
