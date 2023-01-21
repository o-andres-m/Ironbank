package com.ironhack.ironbank.service;

import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.setting.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class Utils {



    public static int calculateAge(LocalDate dateOfBirth){
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public static boolean isAdult(int age){
        return age >= 18;
    }

    public static boolean isOver24(int age){
        return age >= 24;
    }

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