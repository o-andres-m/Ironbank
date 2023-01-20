package com.ironhack.ironbank.model.entities;

import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.defaults.PenaltyFee;
import com.ironhack.ironbank.model.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String number;

    @GeneratedValue(strategy = GenerationType.UUID)
    private String secretKey;

    // TODO : Preguntar aqui que pasa..
    @Embedded
    //@AttributeOverrides({
      //      @AttributeOverride(name = "currency", column = @Column(name = "first_name")),
        //    @AttributeOverride(name = "amount", column = @Column(name = "last_name"))
   // })
    private Money balance;

    @ManyToOne
    @JoinColumn(name = "primaryOwner_id")
    private AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondaryOwner_id")
    private AccountHolder secondaryOwner;

    private Status status;

    @OneToMany (mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionList;

    @Embedded
    private PenaltyFee penaltyFee;

    @CreationTimestamp
    private Instant creationDate;

}
