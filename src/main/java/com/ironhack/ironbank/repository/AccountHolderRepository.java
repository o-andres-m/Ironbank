package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder,Long> {

    Optional<AccountHolder> findAccountHolderByNif(String nif);


}
