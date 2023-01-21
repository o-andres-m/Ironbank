package com.ironhack.ironbank.service.utils;

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



}
