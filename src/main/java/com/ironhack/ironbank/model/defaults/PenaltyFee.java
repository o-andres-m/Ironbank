package com.ironhack.ironbank.model.defaults;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class PenaltyFee {

    private BigDecimal penaltyAmount;

    public PenaltyFee(BigDecimal valueOf) {
        setPenaltyAmount(valueOf);
    }

    public PenaltyFee() {
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }
}
