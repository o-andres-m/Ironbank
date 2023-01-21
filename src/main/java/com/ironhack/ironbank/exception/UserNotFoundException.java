package com.ironhack.ironbank.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String name) {
        super("User with username '"+name+"' doesn't exist.");
    }
}
