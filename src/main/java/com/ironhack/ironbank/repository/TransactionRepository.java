package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {

}
