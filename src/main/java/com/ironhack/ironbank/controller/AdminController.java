package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.account.AccountAdminViewDto;
import com.ironhack.ironbank.dto.account.AccountDto;
import com.ironhack.ironbank.dto.transaction.TransactionDto;
import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.users.AdminDto;
import com.ironhack.ironbank.dto.users.ThirdPartyDto;
import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.dto.users.AdminDtoResponse;
import com.ironhack.ironbank.dto.users.ThirdPartyDtoResponse;
import com.ironhack.ironbank.service.AdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public AccountHolderDtoResponse registerAH(@Valid @RequestBody  AccountHolderDto accountHolderDto) {
        return adminService.registerAH(accountHolderDto);
    }

    @PostMapping("/register/TP")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyDtoResponse registerTP(@Valid @RequestBody ThirdPartyDto thirdPartyDto){
        return adminService.registerTP(thirdPartyDto);
    }

    @PostMapping("/register/ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDtoResponse registerAdmin(@RequestBody AdminDto adminDto){
        return adminService.registerAdmin(adminDto);
    }

    /**
     * Search Users (AccountHolders/ThirdParty)
     */

    @GetMapping("/AH")
    public ArrayList<AccountHolderDtoResponse> searchAH(@RequestParam Optional<String> username,
                                                        @RequestParam Optional<String> firstName,
                                                        @RequestParam Optional<String> lastName,
                                                        @RequestParam Optional<String> nif,
                                                        @RequestParam Optional<String> phone){
        return adminService.searchAH(username,firstName,lastName,nif,phone);
    }

    @GetMapping("/TP")
    public ArrayList<ThirdPartyDtoResponse> searchTP(@RequestParam Optional<String> username,
                                                     @RequestParam Optional<String> companyName,
                                                     @RequestParam Optional<String> nif){
        return adminService.searchTP(username,companyName,nif);
    }

    /**
     * Modify User Data
     */
    @PatchMapping("/AH/{id}")
    public AccountHolderDtoResponse updateAH(@PathVariable Long id,
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
    public ThirdPartyDtoResponse updateTP(@PathVariable Long id,
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
    public AdminDtoResponse updateAdmin(@PathVariable Long id,
                                        @RequestParam Optional<String> username,
                                        @RequestParam Optional<String> firstName,
                                        @RequestParam Optional<String> lastName,
                                        @Email @RequestParam Optional<String> email
                                    ){
        return adminService.updateAdmin(id, username, firstName, lastName, email);
    }

    @PatchMapping("/activate/{id}")
    public String activateUser(@PathVariable Long id){
        return adminService.activateUser(id);
    }

    @PatchMapping("/desactivate/{id}")
    public String desactivateUser(@PathVariable Long id){
        return adminService.desactivateUser(id);
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id){
        return adminService.deleteUser(id);
    }

    /**
     * Admin & Accounts
     */

    @GetMapping("/accounts")
    public List<AccountDto> allAccounts(@RequestParam Optional<String> username){
        return adminService.allAccounts(username);
    }

    @GetMapping("/accounts/{account}")
    public AccountAdminViewDto viewAccount(@PathVariable String account){
        return adminService.viewAccount(account);
    }

    @PatchMapping("/accounts/{account}")
    public AccountAdminViewDto updateAccount(@PathVariable String account,
                                             @RequestParam Optional<Long> secondaryOwnerId,
                                             @RequestParam Optional<BigDecimal> minimumBalance,
                                             @RequestParam Optional<BigDecimal> creditLimit,
                                             @RequestParam Optional<Double> interests
                                             ){
        return adminService.updateAccount(account,secondaryOwnerId,minimumBalance,creditLimit,interests);
    }

    @PatchMapping("/accounts/freeze/{account}")
    public AccountDto freezeAccount(@PathVariable String account,
                                    @PathVariable Integer action){
        return adminService.freezeAccount(account, action);
    }

    @DeleteMapping("/accounts/delete/{account}")
    public String deleteAccount(@PathVariable String account){
        return adminService.deleteAccount(account);
    }

    /**
     * Admin to Accounts: Penalty, Interests, MonthlyMaintenance
     */
    @PutMapping("/accounts/penalty/{account}")
    public TransactionDto penaltyAccount(@PathVariable String account){
        return adminService.penaltyAccount(account);
    }

    @PutMapping("/accounts/interests/credit")
    public List<TransactionDto> applyInterestsCredit(){
        return adminService.applyInterestsCredit();
    }

    @PutMapping("/accounts/interests/saving")
    public List<TransactionDto> applyInterestsSaving(){
        return adminService.applyInterestsSaving();
    }

    @PutMapping("/accounts/maintenance")
    public List<TransactionDto> applyMaintenance(){
        return adminService.applyMaintenance();
    }

    @PutMapping("/accounts/debitcredit")
    public List<TransactionDto> debitCreditCard(){
        return adminService.debitCreditCard();
    }

    /**
     * Admin to Accounts: Create Accounts to AccountHolder.
     */
    @PostMapping("/accounts/create/checking/{id}")
    public AccountDto createChecking(@PathVariable Long id){
        return adminService.createChecking(id);
    }

    @PostMapping("/accounts/create/credit/{id}")
    public AccountDto createCredit(@PathVariable Long id){
        return adminService.createCredit(id);
    }

    @PostMapping("/accounts/create/saving/{id}")
    public AccountDto createSaving(@PathVariable Long id,
                                   @RequestParam BigDecimal amount){
        return adminService.createSaving(id, amount);
    }
}
