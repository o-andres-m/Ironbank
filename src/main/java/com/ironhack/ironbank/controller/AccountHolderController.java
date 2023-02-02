package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.account.AccountDto;
import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.transaction.TransactionDto;
import com.ironhack.ironbank.dto.info.AccountHolderInfoDto;
import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.service.HoldersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/holders")
@RequiredArgsConstructor
public class AccountHolderController {

    private final HoldersService holdersService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolderDtoResponse register(@Valid @RequestBody AccountHolderDto accountHolderDto){
        return holdersService.register(accountHolderDto);
    }

    @PostMapping("/forgotpassword")
    public String forgotPassword(@RequestParam String nif,
                                 @RequestParam String email){
        return holdersService.forgotPassword(nif, email);
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

    @PutMapping("/deposit/saving/{account}")
    public TransactionDto depositSavingAccount(@RequestParam BigDecimal amount,
                                           @PathVariable String account){
        return holdersService.depositSavingAccount(account, amount);
    }

    @PutMapping("/deposit/savingfromchecking/{account}")
    public TransactionDto depositSavingAccountFromChecking(@RequestParam BigDecimal amount,
                                                       @PathVariable String account){
        return holdersService.depositSavingAccountFromChecking(account, amount);
    }
    @GetMapping("/withdraw/saving/{account}")
    public TransactionDto withdrawSavingAccount(@RequestParam BigDecimal amount,
                                            @PathVariable String account){
        return holdersService.withdrawSavingAccount(account, amount);
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
    public TransactionDto withdraw(@RequestParam BigDecimal amount){
        return  holdersService.withdraw(amount);
    }

    @GetMapping("/all")
    public List<AccountDto> allAccounts(){
        return holdersService.allAccounts();
    }

    @GetMapping("/account")
    public AccountDto viewAccount(@RequestParam String account){
        return holdersService.viewAccount(account);
    }

    //TODO: ponerle que filtre por fecha
    @GetMapping("/transactions")
    public List<TransactionDto> viewTransactions(@RequestParam String account){
        return holdersService.viewTransactions(account);
    }

    @GetMapping("/info")
    public AccountHolderInfoDto viewPersonalInfo(){
        return holdersService.viewPersonalInfo();
    }

    @PatchMapping("/update")
    public AccountHolderDtoResponse updateInfo(@RequestParam Optional<String> username,
                                             @RequestParam Optional<String> password,
                                             @RequestParam Optional<String> address,
                                             @RequestParam Optional<String> phone,
                                             @Email @RequestParam Optional<String> email
                                            ){
        return holdersService.update(username,password, address, phone, email);
    }

    @PutMapping("/setSecondaryOwner/{account}")
    public AccountDto setSecondaryOwner(@PathVariable String account,
                                        @RequestParam String secondaryOwnerNif){
        return holdersService.setSecondaryOwner(account, secondaryOwnerNif);
    }


    @PutMapping("/transfer/{account}")
    public TransactionDto trasnferToAccount(@PathVariable String account,
                                            @RequestParam BigDecimal amount){
        return  holdersService.trasnferToAccount(account, amount);
    }




}
