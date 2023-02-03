package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.account.AccountAdminViewDto;
import com.ironhack.ironbank.dto.account.AccountDto;
import com.ironhack.ironbank.dto.transaction.TransactionDto;
import com.ironhack.ironbank.dto.users.AccountHolderDto;
import com.ironhack.ironbank.dto.users.AdminDto;
import com.ironhack.ironbank.dto.users.ThirdPartyDto;
import com.ironhack.ironbank.dto.users.AccountHolderDtoResponse;
import com.ironhack.ironbank.dto.users.AdminDtoResponse;
import com.ironhack.ironbank.dto.users.ThirdPartyDtoResponse;
import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.exception.UserNotFoundException;
import com.ironhack.ironbank.model.entities.accounts.*;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.Admin;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CheckingAccountRepository;
import com.ironhack.ironbank.repository.UserRepository;
import com.ironhack.ironbank.service.utils.AccountUtils;
import com.ironhack.ironbank.service.utils.TransactionUtils;
import com.ironhack.ironbank.service.utils.UserUtils;
import com.ironhack.ironbank.service.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final AccountUtils accountUtils;

    private final UserUtils userUtils;

    private final AccountRepository accountRepository;

    private final TransactionUtils transactionUtils;

    private final CheckingAccountRepository checkingAccountRepository;


    /**
     * Rgister Methods
     */
    public AccountHolderDtoResponse registerAH(AccountHolderDto accountHolderDto) {
        var user = accountHolderDto.getUsername();
        accountUtils.verifyUserExists(accountHolderDto.getUsername());
        accountUtils.verifyNifExists(accountHolderDto.getNif());

        var accountHolder = accountUtils.createAccountHolder(accountHolderDto);
        return AccountHolderDtoResponse.fromAccountHolder(userRepository.save(accountHolder));
    }

    public ThirdPartyDtoResponse registerTP(ThirdPartyDto thirdPartyDto) {
        accountUtils.verifyUserExists(thirdPartyDto.getUsername());
        accountUtils.verifyNifExists(thirdPartyDto.getNif());

        var thirdParty = accountUtils.createThirdParty(thirdPartyDto);
        return ThirdPartyDtoResponse.fromThirdParty(userRepository.save(thirdParty));
    }

    public AdminDtoResponse registerAdmin(AdminDto adminDto) {
        var user = adminDto.getUsername();
        accountUtils.verifyUserExists(user);

        var admin = accountUtils.createAdmin(adminDto);
        return AdminDtoResponse.fromAdmin(userRepository.save(admin));
    }

    /**
     * Search Methods
     */
    public ArrayList<AccountHolderDtoResponse> searchAH(Optional<String> username, Optional<String> firstName, Optional<String> lastName, Optional<String> nif, Optional<String> phone) {

        var listOfAH2 = userRepository.searchAccountHolders(username.orElse(""),
                firstName.orElse(""),lastName.orElse(""),
                nif.orElse(""),phone.orElse(""));

        var listOfAHDto = new ArrayList<AccountHolderDtoResponse>();
        for (User user : listOfAH2){
            listOfAHDto.add(AccountHolderDtoResponse.fromAccountHolder((AccountHolder) user));
        }
        return listOfAHDto;
    }


    public ArrayList<ThirdPartyDtoResponse> searchTP(Optional<String> username, Optional<String> companyName, Optional<String> nif) {

        var listOfTP = userRepository.searchThirdParty(username.orElse(""),
                companyName.orElse(""), nif.orElse(""));

        var listOfTPDto = new ArrayList<ThirdPartyDtoResponse>();
        for (User user : listOfTP){
            listOfTPDto.add(ThirdPartyDtoResponse.fromThirdParty((ThirdParty) user));
        }
        return listOfTPDto;
    }

    /**
     * Modify Methods
     */
    public AccountHolderDtoResponse updateAH(Long id, Optional<String> username, Optional<String> firstName, Optional<String> lastName, Optional<String> nif, Optional<String> phone, Optional<String> email, Optional<LocalDate> dateOfBirth, Optional<String> address) {

        User foundUser = accountUtils.getUserById(id);
        username.ifPresent(accountUtils::verifyUserExists);

        if(foundUser.getRoles().equals("ROLE_ACCOUNTHOLDER")) {
            var user =  (AccountHolder) foundUser;
            username.ifPresent(user::setUsername);
            firstName.ifPresent(user::setFirstName);
            lastName.ifPresent(user::setLastName);
            nif.ifPresent(user::setNif);
            dateOfBirth.ifPresent(user::setDateOfBirth);
            address.ifPresent(s -> user.getAddress().setAddress(s));
            phone.ifPresent(s -> user.getAddress().setPhone(s));
            email.ifPresent(s -> user.getAddress().setEmail(s));

            return AccountHolderDtoResponse.fromAccountHolder(userRepository.save(user));
        }else{
            throw new EspecificException("ID "+id+" is not AccountHolder");
        }
    }

    public ThirdPartyDtoResponse updateTP(Long id, Optional<String> username, Optional<String> companyName, Optional<String> nif, Optional<String> phone, Optional<String> email, Optional<String> address) {

        User foundUser = accountUtils.getUserById(id);
        username.ifPresent(accountUtils::verifyUserExists);

        if(foundUser.getRoles().equals("ROLE_THIRDPARTY")) {
            var user =  (ThirdParty) foundUser;
            username.ifPresent(user::setUsername);
            companyName.ifPresent(user::setCompanyName);
            nif.ifPresent(user::setNif);
            address.ifPresent(s -> user.getAddress().setAddress(s));
            phone.ifPresent(s -> user.getAddress().setPhone(s));
            email.ifPresent(s -> user.getAddress().setEmail(s));

            return ThirdPartyDtoResponse.fromThirdParty(userRepository.save(user));
        }else{
            throw new EspecificException("ID "+id+" is not ThirdParty");
        }
    }

    public AdminDtoResponse updateAdmin(Long id, Optional<String> username, Optional<String> firstName, Optional<String> lastName, Optional<String> email) {

        User foundUser = accountUtils.getUserById(id);
        username.ifPresent(accountUtils::verifyUserExists);

        if(foundUser.getRoles().equals("ROLE_ADMIN")) {
            var user =  (Admin) foundUser;
            username.ifPresent(user::setUsername);
            firstName.ifPresent(user::setFirstName);
            lastName.ifPresent(user::setLastName);
            email.ifPresent(user::setEmail);

            return AdminDtoResponse.fromAdmin(userRepository.save(user));
        }else{
            throw new EspecificException("ID "+id+" is not Admin");
        }
    }



    /**
     * Accounts Methods
     */
    public List<AccountDto> allAccounts(Optional<String> username) {
        var accountList = new ArrayList<Account>();
        if (username.isEmpty()){
            accountList = (ArrayList<Account>) accountRepository.findAll();
        }else{
            accountList = (ArrayList<Account>) accountRepository.findAccountByPrimaryOwner_Username(username.get());
        }
        var accountListDto = new ArrayList<AccountDto>();

        for(Account account : accountList){
            accountListDto.add(AccountDto.fromAccount(account));
        }
        return accountListDto;
    }

    public AccountAdminViewDto viewAccount(String account) {
        Account accountFound = accountUtils.getAccountByNumber(account);
        return AccountAdminViewDto.fromAccount(accountFound);
    }

    public AccountAdminViewDto updateAccount(String account, Optional<Long> secondaryOwnerId, Optional<BigDecimal> minimumBalance, Optional<BigDecimal> creditLimit, Optional<Double> interests) {
        Account accountFound = accountUtils.getAccountByNumber(account);
        if (secondaryOwnerId.isPresent() && secondaryOwnerId.get() == 0L){
            accountFound.setSecondaryOwner(null);
        }else {
            secondaryOwnerId.ifPresent(aLong -> accountFound.setSecondaryOwner(userUtils.getAccountHolder(aLong)));
        }
        if (minimumBalance.isPresent()) {
            if(accountFound instanceof CheckingAccount){
                ((CheckingAccount) accountFound).setMinimumBalance(minimumBalance.get());
            }
            if(accountFound instanceof SavingAccount){
                ((SavingAccount) accountFound).setMinimumBalance(minimumBalance.get());
            }
        }
        creditLimit.ifPresent(bigDecimal -> ((CreditCardAccount) accountFound).setCreditLimit(bigDecimal));
        if (interests.isPresent()) {
            if(accountFound instanceof CreditCardAccount){
                ((CreditCardAccount) accountFound).setInterests(interests.get());
            }
            if(accountFound instanceof SavingAccount){
                ((SavingAccount) accountFound).setInterests(interests.get());
            }
        }
        return AccountAdminViewDto.fromAccount(accountRepository.save(accountFound));
    }

    public String activateUser(Long id) {
        User foundUser = accountUtils.getUserById(id);
        foundUser.setIsAccountNonLocked(true);
        userRepository.save(foundUser);
        return "User id: "+id+", username: "+foundUser.getUsername()+", is now ACTIVATED";
    }

    public String deactivateUser(Long id) {
        User foundUser = accountUtils.getUserById(id);
        foundUser.setIsAccountNonLocked(false);
        userRepository.save(foundUser);
        return "User id: "+id+", username: "+foundUser.getUsername()+", is now DESACTIVATED";
    }

    public String deleteUser(Long id) {

        var foundUser = userUtils.getAccountHolder(id);
        userRepository.delete(foundUser);
        return "User Nº "+id+" deleted. Their accounts have been deleted as well";
    }

    public AccountDto freezeAccount(String account, Integer action) {
        if(action != 0 &&  action != 1){
            throw new EspecificException("Please put 'action' param. (0 = FREEZE, 1 = ACTIVE)");
        }
        Account accountFound =accountUtils.getAccountByNumber(account);
        if(action == 1){
            accountFound.setStatus(Status.ACTIVE);
        }
        if(action == 0){
            accountFound.setStatus(Status.FREEZE);
        }
        return AccountDto.fromAccount(accountRepository.save(accountFound));
    }


    public String deleteAccount(String account) {
        Account accountFound = accountUtils.getAccountByNumber(account);
        accountRepository.delete(accountFound);
        return "Account Nº "+account+" deleted. Their transactions have been deleted as well";
    }

    public TransactionDto penaltyAccount(String account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var admin = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundException(authentication.getName()));
        Account accountFound = accountUtils.getAccountByNumber(account);
        accountFound.getBalance().decreaseAmount(accountFound.getPenaltyFee().getPenaltyAmount());
        return TransactionDto.fromTransaction(transactionUtils.registerPenalty(accountFound, admin.getUsername()));
    }

    public List<TransactionDto> applyInterestsCredit() {
        var listOfAccounts = accountRepository.findAllCreditCardAccounts();
        var transactionList = new ArrayList<TransactionDto>();
        for(CreditCardAccount creditCardAccount : listOfAccounts){
            //Interests Value = Interests * Balance / 100
            var interests = creditCardAccount.getInterests().getValue() * creditCardAccount.getBalance().getAmount().doubleValue() / 100;
            creditCardAccount.getBalance().increaseAmount(BigDecimal.valueOf(interests));
            accountRepository.save(creditCardAccount);
            transactionList.add(TransactionDto.fromTransaction(transactionUtils.registerInterests(creditCardAccount,interests)));
        }
        return transactionList;
    }

    public List<TransactionDto> applyInterestsSaving() {
        var listOfAccounts = accountRepository.findAllSavingAccounts();
        var transactionList = new ArrayList<TransactionDto>();
        for(SavingAccount checkingAccount : listOfAccounts){
            //Interests Value = Interests * Balance / 100
            var interests = checkingAccount.getInterests().getValue() * checkingAccount.getBalance().getAmount().doubleValue() / 100;
            checkingAccount.getBalance().increaseAmount(BigDecimal.valueOf(interests));
            accountRepository.save(checkingAccount);
            transactionList.add(TransactionDto.fromTransaction(transactionUtils.registerInterests(checkingAccount,interests)));
        }
        return transactionList;
    }

    public List<TransactionDto> applyMaintenance() {
        var listOfAccounts = accountRepository.findAllCheckingAccounts();
        var transactionList = new ArrayList<TransactionDto>();
        for(CheckingAccount account : listOfAccounts) {
            account.getBalance().decreaseAmount(account.getMontlyMaintenance());
            accountRepository.save(account);
            transactionList.add(TransactionDto.fromTransaction(transactionUtils.registerMonthlyMaintenance(account, account.getMontlyMaintenance())));
        }
        return transactionList;
    }

    public List<TransactionDto> debitCreditCard() {
        var listOfCreditCards = accountRepository.findAllCreditCardAccounts();
        var transactionList = new ArrayList<TransactionDto>();
        for(CreditCardAccount creditCard : listOfCreditCards) {
            if(creditCard.getBalance().getAmount().signum() == 1) {
                var owner = userRepository.findById(creditCard.getPrimaryOwner().getId());
                var checkingAccount = checkingAccountRepository.findCheckingAccountByPrimaryOwner((AccountHolder) owner.get());

                checkingAccount.get().getBalance().decreaseAmount(creditCard.getBalance().getAmount());
                transactionList.add(TransactionDto.fromTransaction(transactionUtils.registerDebitCreditCard(checkingAccount.get(), creditCard)));
                accountRepository.save(checkingAccount.get());

                transactionUtils.registerPaymentCreditCard(creditCard, checkingAccount.get());
                creditCard.getBalance().decreaseAmount(creditCard.getBalance().getAmount());
                accountRepository.save(creditCard);
            }
        }
        return transactionList;
    }

    public AccountDto createChecking(Long id) {

        var accountHolder = userUtils.getAccountHolder(id);
        var account = checkingAccountRepository.findCheckingAccountByPrimaryOwner(accountHolder);
        if (account.isPresent()){
            throw new EspecificException("User have already one Checking Account");
        }
        if (Utils.isOver24(Utils.calculateAge( accountHolder.getDateOfBirth()))) {
            var accountCreated = new CheckingAccount(accountHolder);
            accountRepository.save(accountCreated);
            return AccountDto.fromAccount(accountCreated);
        } else {
            var accountCreated = new StudentAccount(accountHolder);
            accountRepository.save(accountCreated);
            return AccountDto.fromAccount(accountCreated);
        }
    }

    public AccountDto createCredit(Long id) {
        var accountHolder = userUtils.getAccountHolder(id);
        accountUtils.findCheckingAccountByAccountHolder(accountHolder);
        var accountCreated = new CreditCardAccount(accountHolder);
        accountRepository.save(accountCreated);
        return AccountDto.fromAccount(accountCreated);
    }

    public AccountDto createSaving(Long id, BigDecimal amount) {
        var accountHolder = userUtils.getAccountHolder(id);
        if (amount.intValueExact()<1000) throw new EspecificException("Minimum amount: 100");

        var checkingAccount = checkingAccountRepository.findCheckingAccountByPrimaryOwner(accountHolder).
                orElseThrow(()-> new EspecificException("The user doesn't have Checking Account."));

        var balance = checkingAccount.getBalance().getAmount().doubleValue();
        accountUtils.checkFinalBalance(checkingAccount,amount);

        Account accountCreated = new SavingAccount(accountHolder);
        accountCreated.getBalance().increaseAmount(amount);
        accountRepository.save(accountCreated);
        // Register new SavingAccount
        transactionUtils.registerNewSavingAccount(accountCreated,amount);
        // Register less balance in CheckingAccount
        transactionUtils.fromCheckingtoSaving(checkingAccount,amount,accountCreated);
        // Update new balance in checkingAccount
        checkingAccount.getBalance().decreaseAmount(amount);
        accountRepository.save(checkingAccount);
        return AccountDto.fromAccount(accountCreated);
    }
}

