package com.ironhack.ironbank.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class ThirdParty extends User{

    private String nif;

    private String companyName;

    @OneToMany (mappedBy = "thirdParty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountMap> accountMapList;

}
