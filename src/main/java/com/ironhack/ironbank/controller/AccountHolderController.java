package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AccountDto;
import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.service.HoldersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/holders")
@RequiredArgsConstructor
public class AccountHolderController {

    private final HoldersService holdersService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolderDto register(@Valid @RequestBody AccountHolderDto accountHolderDto){
        return holdersService.register(accountHolderDto);
    }


    @PostMapping("/create/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createCheckingAccount(){
        return holdersService.createCheckingAccount();
    }


    @PostMapping("/create/saving")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createCheckingAccount(@RequestParam BigDecimal amount){
        return holdersService.createSavingAccount(amount);
    }

    @PostMapping("/create/credit")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createCreditAccount(){
        return holdersService.createCreditAccount();
    }

    @PutMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto depositInCheckingAccount(@RequestParam BigDecimal amount){
        return  holdersService.depositInCheckingAccount(amount);
    }

    @PutMapping("/buywithcredit")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto buyWithCredit(@RequestParam BigDecimal amount,
                                    @RequestHeader(name = "store") String store){
        return  holdersService.buyWithCredit(amount, store);
    }

    @PutMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto withdraw(@RequestParam BigDecimal amount){
        return  holdersService.withdraw(amount);
    }

}
