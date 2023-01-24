package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.entities.AccountMap;
import com.ironhack.ironbank.model.entities.users.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapRepository extends JpaRepository<AccountMap, Long> {


    List<AccountMap> findAllByThirdParty(ThirdParty thirdParty);
}
