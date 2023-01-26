package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.model.entities.AccountMap;
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
    @ResponseStatus(HttpStatus.CREATED)
    public List<AccountMap> createUser (@RequestParam String account, @RequestParam String secretkey){
        return thirdPartyService.registerAccount(account,secretkey);
    }

}
