package com.ironhack.ironbank.service.utils;

import com.ironhack.ironbank.exception.EspecificException;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.User;
import com.ironhack.ironbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;


    public AccountHolder getAccountHolder(Long id) {
        return (AccountHolder) userRepository.findById(id).orElseThrow(
                () -> new EspecificException("User Not Found"));
    }

    public AccountHolder getUserByNif(String nif) {
        return userRepository.findAccountHolderByNif(nif).orElseThrow(()-> new EspecificException("User with NIF "+ nif +" not found."));
    }

}
