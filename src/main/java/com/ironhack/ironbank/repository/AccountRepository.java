package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findAccountByPrimaryOwner_Username(String username);
}
