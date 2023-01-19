package com.ironhack.ironbank.model.entities;

import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.defaults.PenaltyFee;
import com.ironhack.ironbank.model.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private String secretKey;

    // TODO : Preguntar aqui que pasa..
    //private Money balance;

    //private Money minimumBalance;

    @ManyToOne
    @JoinColumn(name = "accountHolder_id")
    private AccountHolder primaryOwner;

    //TODO: Ver tema de doble relacion...
    //@ManyToOne
    //private AccountHolder secondaryOwner;

    private Status status;

    @OneToMany (mappedBy = "toAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionList;

    //TODO: Preguntar...
    //private PenaltyFee penaltyFee;

    @CreationTimestamp
    private Instant creationDate;

}
