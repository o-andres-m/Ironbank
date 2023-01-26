package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AccountDto;
import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.dto.AdminDto;
import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.service.AdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;

    /**
     * Admin can register AccountHolder, ThirdParty and Admins.
     * Every user need Json Body Dto to register.
     */

    @PostMapping("/register/AH")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolderDto registerAH(@Valid @RequestBody  AccountHolderDto accountHolderDto) {
        return adminService.registerAH(accountHolderDto);
    }

    @PostMapping("/register/TP")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyDto registerTP(@Valid @RequestBody ThirdPartyDto thirdPartyDto){
        return adminService.registerTP(thirdPartyDto);
    }

    @PostMapping("/register/ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDto registerAdmin(@RequestBody AdminDto adminDto){
        return adminService.registerAdmin(adminDto);
    }

    /**
     * Search Users (AccountHolders/ThirdParty)
     */

    @GetMapping("/AH")
    public List<AccountHolderDto> searchAH(@RequestParam Optional<String> username,
                                           @RequestParam Optional<String> firstName,
                                           @RequestParam Optional<String> lastName,
                                           @RequestParam Optional<String> nif,
                                           @RequestParam Optional<String> phone){
        return adminService.searchAH(username,firstName,lastName,nif,phone);
    }

    @GetMapping("/TP")
    public List<ThirdPartyDto> searchTP(@RequestParam Optional<String> username,
                                        @RequestParam Optional<String> companyName,
                                        @RequestParam Optional<String> nif){
        return adminService.searchTP(username,companyName,nif);
    }

    /**
     * Modify User Data
     */
    @PatchMapping("/AH/{id}")
    public AccountHolderDto updateAH(@PathVariable Long id,
                                     @RequestParam Optional<String> username,
                                     @RequestParam Optional<String> firstName,
                                     @RequestParam Optional<String> lastName,
                                     @RequestParam Optional<String> nif,
                                     @RequestParam Optional<LocalDate> dateOfBirth,
                                     @RequestParam Optional<String> address,
                                     @RequestParam Optional<String> phone,
                                     @Email @RequestParam Optional<String> email
                                     ){
        return adminService.updateAH(id, username,firstName,lastName,nif,phone, email, dateOfBirth,address);
    }

    @PatchMapping("/TP/{id}")
    public ThirdPartyDto updateTP(@PathVariable Long id,
                                     @RequestParam Optional<String> username,
                                     @RequestParam Optional<String> companyName,
                                     @RequestParam Optional<String> nif,
                                     @RequestParam Optional<String> phone,
                                     @RequestParam Optional<String> address,
                                     @Email @RequestParam Optional<String> email
                                    ){
        return adminService.updateTP(id, username, companyName, nif, phone, email, address);
    }

    @PatchMapping("/admin/{id}")
    public AdminDto updateAdmin(@PathVariable Long id,
                                @RequestParam Optional<String> username,
                                @RequestParam Optional<String> firstName,
                                @RequestParam Optional<String> lastName,
                                @Email @RequestParam Optional<String> email
                                    ){
        return adminService.updateAdmin(id, username, firstName, lastName, email);
    }

    /**
     * Admin & Accounts
     */

    @GetMapping("/accounts")
    public List<AccountDto> allAccounts(@RequestParam Optional<String> username){
        return adminService.allAccounts(username);
    }

}
