package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.entities.users.ThirdParty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ThirdPartyDto {


    private String username;

    private String password;

    private String roles;

    private String nif;

    private String companyName;

    private String address;

    private String email;

    private String phone;



    public static ThirdPartyDto fromThirdParty (ThirdParty thirdParty){
        var thirdPartyDto = new ThirdPartyDto();
        thirdPartyDto.setUsername(thirdParty.getUsername());
        thirdPartyDto.setPassword(thirdParty.getPassword());
        thirdPartyDto.setNif(thirdParty.getNif());
        thirdPartyDto.setCompanyName(thirdParty.getCompanyName());
        thirdPartyDto.setRoles("THIRDPARTY");
        thirdPartyDto.setAddress(thirdParty.getAddress().getAddress());
        thirdPartyDto.setEmail(thirdParty.getAddress().getEmail());
        thirdPartyDto.setPhone(thirdParty.getAddress().getPhone());

        return thirdPartyDto;


    }


}
