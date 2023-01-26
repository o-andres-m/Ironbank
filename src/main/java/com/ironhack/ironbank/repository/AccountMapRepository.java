package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.AccountMap;
import com.ironhack.ironbank.model.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountMapRepository extends JpaRepository<AccountMap, Long> {


    List<AccountMap> findAllByThirdParty(User thirdParty);

    Optional<AccountMap> findAccountMapByAccountNumberAndThirdPartyUsername(String account, String username);
}
