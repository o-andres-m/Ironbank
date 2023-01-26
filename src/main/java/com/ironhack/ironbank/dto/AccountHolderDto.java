package com.ironhack.ironbank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AccountHolderDto {

    private Long id;

    @NotBlank(message = "Username required.")
    private String username;

    @NotBlank(message = "Password required.")
    private String password;

    @JsonIgnore
    private String roles;

    @NotBlank(message = "Nif required.")
    private String nif;

    @NotBlank(message = "First Name required.")
    private String firstName;

    @NotBlank(message = "Last Name required.")
    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address required.")
    private String address;

    @Email
    private String email;

    @NotBlank(message = "Phone required.")
    private String phone;


    public static AccountHolderDto fromAccountHolder(AccountHolder accountHolder){
        var accountHolderDto = new AccountHolderDto();
        accountHolderDto.setId(accountHolder.getId());
        accountHolderDto.setUsername(accountHolder.getUsername());
        accountHolderDto.setPassword(accountHolder.getPassword());
        accountHolderDto.setRoles(accountHolder.getRoles());
        accountHolderDto.setNif(accountHolder.getNif());
        accountHolderDto.setFirstName(accountHolder.getFirstName());
        accountHolderDto.setLastName(accountHolder.getLastName());
        accountHolderDto.setDateOfBirth(accountHolder.getDateOfBirth());
        accountHolderDto.setAddress(accountHolder.getAddress().getAddress());
        accountHolderDto.setEmail(accountHolder.getAddress().getEmail());
        accountHolderDto.setPhone(accountHolder.getAddress().getPhone());

        return accountHolderDto;
    }
}
