package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AccountHolderDto;
import com.ironhack.ironbank.dto.AdminDto;
import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
