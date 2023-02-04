package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.entities.accounts.Account;
import com.ironhack.ironbank.model.enums.Status;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.TransactionRepository;
import com.ironhack.ironbank.setting.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class FraudDetectionUtils {

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;


    public void verifyRecurrentOperations(Account account) {
        var lastTransaction = transactionRepository.findLastTransactionByAccountId(account.getId());
        if (lastTransaction != null) {
            var duration = Duration.between(lastTransaction.getDate(), Instant.now());
            if (duration.getSeconds() < Settings.getSECONDS_TO_FRAUD_DETECT()) {
                account.setStatus(Status.FREEZE);
                accountRepository.save(account);
                throw new EspecificException("¡FRAUD DETECTED! Please go to the bank to activate account.");
            }
        }
    }

    public void verifyExpensiveOperation(Account account, BigDecimal amount) {
        var transactionList = transactionRepository.findTransactionsByAccount_NumberOrderByDateDesc(account.getNumber());
        var maxValuesList = new ArrayList<Double>();
        var maxValueIn24hs = (double) 0;
        if (transactionList.size() != 0) {
            for (int i = 0; i < transactionList.size(); i++) {
                for (int j = 0; j < transactionList.size(); j++) {
                    if (transactionList.get(i).getDate()
                            .truncatedTo(ChronoUnit.DAYS)
                            .equals(transactionList.get(j).getDate()
                                    .truncatedTo(ChronoUnit.DAYS)) &&
                            !transactionList.get(i).getDate().truncatedTo(ChronoUnit.DAYS)
                                    .equals(Instant.now().truncatedTo(ChronoUnit.DAYS))) {
                        if (transactionList.get(j).getAmount().getAmount().signum() < 0)
                            maxValueIn24hs = maxValueIn24hs + transactionList.get(j).getAmount().getAmount().doubleValue();
                    }
                }
                maxValuesList.add(maxValueIn24hs);
                maxValueIn24hs = 0;
            }
            Collections.sort(maxValuesList);
            if (maxValuesList.size() > 0) {
                if (maxValuesList.get(0) * (-1) * 1.5 < amount.doubleValue() && amount.doubleValue() > 1000 ) {
                    account.setStatus(Status.FREEZE);
                    accountRepository.save(account);
                    throw new EspecificException("¡FRAUD DETECTED! Please go to the bank to activate account.");
                }
            }
        }
    }
}
