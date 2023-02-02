package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.transaction.TransactionDto;
import com.ironhack.ironbank.service.AdminService;
import com.ironhack.ironbank.setting.Settings;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/auto")
@Log
@RequiredArgsConstructor
public class AutomaticControllers {

    private final AdminService adminService;

    private static String getTodayNumber() {
        var today = LocalDate.now().toString().substring(8,10);
        return today;
    }

    /**
     * Automatic Add Interests one time at month
     */

    @PutMapping("/interests/credit")
    public void applyInterestsCredit(){
        adminService.applyInterestsCredit();
    }

    @Scheduled(fixedRate =  864000000) // 1 day: 864000000
    public void executeInterestCredit() {
        String today = getTodayNumber();
        if(today.equals(Settings.getDAY_TO_APPLY_INTERESTS())) {
            log.info("Automatic Interests to Credit Cards. Day: "+today);
            RestTemplate restTemplate = new RestTemplate();
            String endpointURL = "http://localhost:8080/auto/interests/credit";
            restTemplate.put(endpointURL, String.class);
        }
    }


    @PutMapping("/interests/saving")
    public void applyInterestsSaving(){
        adminService.applyInterestsSaving();
    }

    @Scheduled(fixedRate =  864000000) // 1 day: 864000000
    public void executeInterestSaving() {
        String today = getTodayNumber();
        if(today.equals(Settings.getDAY_TO_APPLY_INTERESTS())) {
            log.info("Automatic Interests to Saving Accounts. Day: "+today);
            RestTemplate restTemplate = new RestTemplate();
            String endpointURL = "http://localhost:8080/auto/interests/saving";
            restTemplate.put(endpointURL, String.class);
        }
    }

    @PutMapping("/maintenance")
    public void applyMaintenance(){
        adminService.applyMaintenance();
    }

    @Scheduled(fixedRate =  864000000) // 1 day: 864000000
    public void executeMaintenance() {
        String today = getTodayNumber();
        if(today.equals(Settings.getDAY_TO_APPLY_MAINTENANCE())) {
            log.info("Automatic Maintenance Applied. Day: "+today);
            RestTemplate restTemplate = new RestTemplate();
            String endpointURL = "http://localhost:8080/auto/maintenance";
            restTemplate.put(endpointURL, String.class);
        }
    }



}
