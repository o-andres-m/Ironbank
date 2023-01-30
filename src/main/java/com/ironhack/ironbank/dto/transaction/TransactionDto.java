package com.ironhack.ironbank.dto.transaction;

import com.ironhack.ironbank.model.entities.Transaction;
import com.ironhack.ironbank.model.enums.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
public class TransactionDto {

    private String transactionId;

    private String account;

    private BigDecimal amount;

    private Instant date;

    private TransactionType transactionType;

    private String observations;


    public static TransactionDto fromTransaction(Transaction transaction) {
        var transactionDto = new TransactionDto();
        transactionDto.setTransactionId(transaction.getTransactionId());
        transactionDto.setAccount(transaction.getAccount().getNumber());
        transactionDto.setAmount(transaction.getAmount().getAmount());
        transactionDto.setDate(transaction.getDate());
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setObservations(transaction.getObservations());
        return transactionDto;
    }
}
