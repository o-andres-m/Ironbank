package com.ironhack.ironbank.model.entities.users;

import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.model.defaults.Address;
import com.ironhack.ironbank.model.entities.AccountMap;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
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

    @Embedded
    private Address address;

    @OneToMany (mappedBy = "thirdParty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountMap> accountMapList;

    public ThirdParty fromDto(ThirdPartyDto thirdPartyDto){
        var thirdParty = new ThirdParty();
        thirdParty.setUsername(thirdPartyDto.getUsername());
        thirdParty.setPassword(thirdPartyDto.getPassword());
        thirdParty.setNif(thirdPartyDto.getNif());
        thirdParty.setCompanyName(thirdPartyDto.getCompanyName());
        thirdParty.setRoles("THIRDPARTY");
        thirdParty.setAddress(new Address(thirdPartyDto.getAddress(), thirdPartyDto.getEmail(), thirdPartyDto.getPhone()));

        return thirdParty;
    }

}
