package com.ironhack.ironbank.dto.response;

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
public class AdminDtoResponse {


    private String username;

    private String firstName;

    private String lastName;

    private String email;


    public static AdminDtoResponse fromAdmin(Admin admin) {
        var adminDtoResponse = new AdminDtoResponse();
        adminDtoResponse.setUsername(admin.getUsername());
        adminDtoResponse.setFirstName(admin.getFirstName());
        adminDtoResponse.setLastName(admin.getLastName());
        adminDtoResponse.setEmail(admin.getEmail());
        return adminDtoResponse;
    }
}
