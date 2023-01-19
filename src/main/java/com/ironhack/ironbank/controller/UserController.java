package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.UserDto;
import com.ironhack.ironbank.model.User;
import com.ironhack.ironbank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping ("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO: Delete tests endppoints.
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser (@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll (){
        return userService.findAll();
    }

}

