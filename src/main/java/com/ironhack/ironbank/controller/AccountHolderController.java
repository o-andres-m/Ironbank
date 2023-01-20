package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.service.HoldersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/holders")
@RequiredArgsConstructor
public class AccountHolderController {

    private final HoldersService holdersService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolderDto register(@RequestBody AccountHolderDto accountHolderDto){
        return holdersService.register(accountHolderDto);
    }




}
