package com.ironhack.ironbank.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ThirdPartyDto {

    private Long id;

    @NotBlank(message = "Username required.")
    private String username;

    @NotBlank(message = "Password required.")
    private String password;

    @JsonIgnore
    private String roles;

    @NotBlank(message = "Nif required.")
    private String nif;

    @NotBlank(message = "Company Name required.")
    private String companyName;

    @NotBlank(message = "Address required.")
    private String address;

    @NotBlank(message = "Email required.")
    @Email
    private String email;

    private String phone;

    public static ThirdPartyDto fromThirdParty (ThirdParty thirdParty){
        var thirdPartyDto = new ThirdPartyDto();
        thirdPartyDto.setId(thirdParty.getId());
        thirdPartyDto.setUsername(thirdParty.getUsername());
        thirdPartyDto.setPassword(thirdParty.getPassword());
        thirdPartyDto.setNif(thirdParty.getNif());
        thirdPartyDto.setCompanyName(thirdParty.getCompanyName());
        thirdPartyDto.setRoles("ROLE_THIRDPARTY");
        thirdPartyDto.setAddress(thirdParty.getAddress().getAddress());
        thirdPartyDto.setEmail(thirdParty.getAddress().getEmail());
        thirdPartyDto.setPhone(thirdParty.getAddress().getPhone());

        return thirdPartyDto;
    }
}
