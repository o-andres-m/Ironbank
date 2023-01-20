package com.ironhack.ironbank.model.defaults;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String address;

    private String email;
}
