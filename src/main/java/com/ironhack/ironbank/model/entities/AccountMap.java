package com.ironhack.ironbank.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class AccountMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private String accountKey;

    @ManyToOne
    @JoinColumn(name = "thirdParty_id")
    private ThirdParty thirdParty;

}