package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.accounts.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {
}
