package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CheckingAccountRepository;
import com.ironhack.ironbank.setting.Settings;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountUtils {

    private final CheckingAccountRepository checkingAccountRepository;


    /**
     * Return automatic Account Number Generated.
     * Format:
     *
     * @param accountHolder
     * @return number of account
     */
    public static String generateAccountNumber(AccountHolder accountHolder){
        var dNow = new Date();
        var ft = new SimpleDateFormat("yyMMddhhmmss");

        return Settings.getBANK_ACCOUNT_START().toUpperCase()+
                accountHolder.getDateOfBirth().toString().substring(2,4)+
                ft.format(dNow);
    }






}
