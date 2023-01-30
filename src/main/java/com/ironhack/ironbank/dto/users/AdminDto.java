package com.ironhack.ironbank.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.model.entities.users.Admin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {


    @NotBlank(message = "Username required.")
    private String username;

    @NotBlank(message = "Password required.")
    @JsonIgnore
    private String password;

    @NotBlank(message = "First Name required.")
    private String firstName;

    @NotBlank(message = "Last Name required.")
    private String lastName;

    @NotBlank(message = "Email required.")
    @Email
    private String email;


    public static AdminDto fromAdmin(Admin admin) {
        var adminDto = new AdminDto();
        adminDto.setUsername(admin.getUsername());
        adminDto.setPassword(admin.getPassword());
        adminDto.setFirstName(admin.getFirstName());
        adminDto.setLastName(admin.getLastName());
        adminDto.setEmail(admin.getEmail());
        return adminDto;
    }
}
