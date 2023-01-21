package com.ironhack.ironbank.model.entities.users;

import com.ironhack.ironbank.model.entities.AccountMap;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class ThirdParty extends User {

    private String nif;

    private String companyName;

    @OneToMany (mappedBy = "thirdParty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountMap> accountMapList;

}
