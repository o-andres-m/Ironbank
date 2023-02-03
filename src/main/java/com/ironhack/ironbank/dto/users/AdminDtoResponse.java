package com.ironhack.ironbank.dto.users;

import com.ironhack.ironbank.model.entities.users.Admin;
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
