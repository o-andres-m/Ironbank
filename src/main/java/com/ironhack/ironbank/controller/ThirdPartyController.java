package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AccountMapDto;
import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.service.ThirdPartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thirdparty")
@RequiredArgsConstructor
public class ThirdPartyController {

    private final ThirdPartyService thirdPartyService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyDto createUser (@Valid @RequestBody ThirdPartyDto thirdPartyDto){
        return thirdPartyService.createUser(thirdPartyDto);
    }


    @PostMapping("/account")
    public AccountMapDto registerAccount (@RequestParam String account, @RequestParam String secretKey){
        return thirdPartyService.registerAccount(account, secretKey);
    }

    @GetMapping("/account")
    public List<AccountMapDto> allAccountMap(){
        return thirdPartyService.allAcountMap();
    }
}
