package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.UserDto;
import com.ironhack.ironbank.model.entities.User;
import com.ironhack.ironbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;



    public User createUser(UserDto userDto) {
        var user = new User();
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        user.setRoles("ROLE_ADMIN");
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
