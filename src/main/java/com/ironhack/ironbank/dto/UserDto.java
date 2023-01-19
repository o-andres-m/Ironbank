package com.ironhack.ironbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.User;

@Data
@AllArgsConstructor
public class UserDto {

    private String username;

    private String password;

    public static UserDto fromUser(User user){
        return new UserDto(user.getUsername(), user.getPassword());
    }

}
