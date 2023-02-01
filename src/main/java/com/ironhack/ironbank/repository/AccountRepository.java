package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.accounts.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findAccountByPrimaryOwner_Username(String username);

    Optional<Account> findAccountByNumber(String number);

    @Query(value = "SELECT * FROM checking_accounts", nativeQuery = true)
    List<CheckingAccount> findAllCheckingAccounts();

    @Query(value = "SELECT * FROM saving_accounts", nativeQuery = true)
    List<SavingAccount> findAllSavingAccounts();

    @Query(value = "SELECT * FROM credit_card_accounts", nativeQuery = true)
    List<CreditCardAccount> findAllCreditCardAccounts();

    @Query(value = "SELECT * FROM student_accounts", nativeQuery = true)
    List<StudentAccount> findAllStudentAccounts();

}
