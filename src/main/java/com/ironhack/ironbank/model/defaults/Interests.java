package com.ironhack.ironbank.model.defaults;

import jakarta.persistence.Embeddable;

@Embeddable
public class Interests {

    private Double interests;

    public Interests(Double value) {
        this.interests = value;
    }

    public Interests() {
    }

    public Double getValue() {
        return interests;
    }

    public void setValue(Double value) {
        this.interests = value;
    }
}
