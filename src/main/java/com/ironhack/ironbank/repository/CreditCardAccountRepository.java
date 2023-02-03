package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.accounts.CreditCardAccount;
import com.ironhack.ironbank.model.entities.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardAccountRepository extends JpaRepository<CreditCardAccount,Long> {

    Optional<CreditCardAccount> findCreditCardAccountByPrimaryOwner(AccountHolder primaryOwner);
}
