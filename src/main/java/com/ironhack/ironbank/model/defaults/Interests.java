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
        if (value>0.5) value=0.5;
        if (value<0.0025) value=0.0025;
        this.interests = value;
    }
}
