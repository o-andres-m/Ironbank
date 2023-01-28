package com.ironhack.ironbank.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ThirdPartyDtoResponse {

    private Long id;

    private String username;

    private String nif;

    private String companyName;

    private String address;

    private String email;

    private String phone;



    public static ThirdPartyDtoResponse fromThirdParty (ThirdParty thirdParty){
        var thirdPartyDtoResponse = new ThirdPartyDtoResponse();
        thirdPartyDtoResponse.setId(thirdParty.getId());
        thirdPartyDtoResponse.setUsername(thirdParty.getUsername());
        thirdPartyDtoResponse.setNif(thirdParty.getNif());
        thirdPartyDtoResponse.setCompanyName(thirdParty.getCompanyName());
        thirdPartyDtoResponse.setAddress(thirdParty.getAddress().getAddress());
        thirdPartyDtoResponse.setEmail(thirdParty.getAddress().getEmail());
        thirdPartyDtoResponse.setPhone(thirdParty.getAddress().getPhone());

        return thirdPartyDtoResponse;
    }


}
