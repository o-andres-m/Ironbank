package com.ironhack.ironbank.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void calculateAge() {
        int age = 32;
        int ageCalculated = Utils.calculateAge(LocalDate.parse("1990-03-24"));
        assertEquals(age,ageCalculated);
    }

    @Test
    void isAdult() {
        assertTrue(Utils.isAdult(18));
        assertTrue(Utils.isAdult(25));
        assertFalse(Utils.isAdult(17));
    }

    @Test
    void isOver24() {
        assertTrue(Utils.isOver24(24));
        assertFalse(Utils.isOver24(23));

    }
}