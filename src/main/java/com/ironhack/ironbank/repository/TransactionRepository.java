package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {

    List<Transaction> findTransactionsByAccount_NumberOrderByDateDesc(String accountNumber);

    @Query(value = "SELECT * FROM transactions WHERE account_id =:accountId ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Transaction findLastTransactionByAccountId(@Param("accountId") Long accountId);
}
