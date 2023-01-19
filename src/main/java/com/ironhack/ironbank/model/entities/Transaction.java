package com.ironhack.ironbank.model.entities;

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
    private String transaction_id;

    private String fromAcount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account toAccount;

    // TODO: Preguntar aqui que pasa...
    //private Money amount;

    @CreationTimestamp
    private Instant date;

    private String description;


}
