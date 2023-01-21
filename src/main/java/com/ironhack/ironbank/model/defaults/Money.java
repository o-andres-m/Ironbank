package com.ironhack.ironbank.model.defaults;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Embeddable
public class Money {

    private static final Currency EUR = Currency.getInstance("EUR");

    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;

    private final Currency currency = EUR;
    private BigDecimal amount;

    /**
     * Class constructor specifying amount, currency, and rounding
     **/

    public Money(BigDecimal amount, RoundingMode rounding) {
        setAmount(amount.setScale(currency.getDefaultFractionDigits(), rounding));
    }

    /**
     * Class constructor specifying amount, and currency. Uses default RoundingMode HALF_EVEN.
     **/

    public Money(BigDecimal amount) {
        this(amount, DEFAULT_ROUNDING);
    }



    /**
     * Class constructor specifying amount. Uses default RoundingMode HALF_EVEN and default currency EUR.
     **/


    public Money() {
    }


    public BigDecimal increaseAmount(Money money) {
        setAmount(this.amount.add(money.amount));
        return this.amount;
    }

    public BigDecimal increaseAmount(BigDecimal addAmount) {
        setAmount(this.amount.add(addAmount));
        return this.amount;
    }

    public BigDecimal decreaseAmount(Money money) {
        setAmount(this.amount.subtract(money.getAmount()));
        return this.amount;
    }

    public BigDecimal decreaseAmount(BigDecimal addAmount) {
        setAmount(this.amount.subtract(addAmount));
        return this.amount;
    }


    public static Currency getCurrency() {
        return EUR;
    }


    public BigDecimal getAmount() {
        return this.amount;
    }

    private void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String toString() {
        return getCurrency().getSymbol() + " " + getAmount();
    }


}
