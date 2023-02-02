package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.accounts.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingAccountRepository extends JpaRepository<SavingAccount,Long> {

    Optional<SavingAccount> findSavingAccountByNumber(String number);
}
