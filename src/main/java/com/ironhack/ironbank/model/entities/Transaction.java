package com.ironhack.ironbank.model.entities;

import com.ironhack.ironbank.model.defaults.Money;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String from;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account toAccount;

    // TODO: Preguntar aqui que pasa...
    //private Money amount;

    @CreationTimestamp
    private Instant date;

    private String description;


}
