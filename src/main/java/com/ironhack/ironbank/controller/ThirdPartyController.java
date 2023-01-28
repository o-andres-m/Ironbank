package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AccountMapDto;
import com.ironhack.ironbank.dto.ThirdPartyDto;
import com.ironhack.ironbank.dto.TransactionDto;
import com.ironhack.ironbank.dto.TransferDto;
import com.ironhack.ironbank.dto.response.ThirdPartyDtoResponse;
import com.ironhack.ironbank.service.ThirdPartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/thirdparty")
@RequiredArgsConstructor
public class ThirdPartyController {

    private final ThirdPartyService thirdPartyService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyDtoResponse createUser (@Valid @RequestBody ThirdPartyDto thirdPartyDto){
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

    /**
     * ThirdParty not registered
     */
    @PatchMapping("/chargeservice")
    public TransactionDto chargeService(@RequestHeader(name = "company") String company,
                                        @RequestHeader(name = "account") String account,
                                        @RequestHeader(name = "secretKey") String secretKey,
                                        @RequestParam BigDecimal amount
                                        ){
        return thirdPartyService.chargeService(company,account,secretKey,amount);
    }

    @PostMapping("/transfer")
    public TransactionDto transferToAccount(@RequestBody TransferDto transferDto,
                                            @RequestHeader(name = "bank") String bankName,
                                            @RequestHeader (name = "thirdClient") String name){
        return thirdPartyService.transferToAccount(transferDto, bankName, name);
    }
}
