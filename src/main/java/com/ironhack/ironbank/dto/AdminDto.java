package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.entities.users.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

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
