package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {


    List<Transaction> findTransactionsByAccount_Number(String accountNumber);
}
