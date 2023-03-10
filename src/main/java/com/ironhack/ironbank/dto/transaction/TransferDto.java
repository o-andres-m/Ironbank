package com.ironhack.ironbank.dto.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransferDto {

    private String toAccount;

    private BigDecimal amount;
}
