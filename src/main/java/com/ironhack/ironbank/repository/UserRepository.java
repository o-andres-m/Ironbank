package com.ironhack.ironbank.repository;


import com.ironhack.ironbank.model.entities.users.AccountHolder;
import com.ironhack.ironbank.model.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    List<User> findUsersByRoles(String roles);

    @Query(value = "SELECT * FROM users WHERE dtype='AccountHolder' AND" +
            " username LIKE %:username% AND first_name LIKE %:firstName% " +
            "AND last_name LIKE %:lastName% AND nif LIKE %:nif% AND phone LIKE %:phone%", nativeQuery = true)
    List<User> searchAccountHolders(@Param("username")String username,
                                    @Param("firstName") String firstName,
                                    @Param("lastName") String lastName,
                                    @Param("nif") String nif,
                                    @Param("phone") String phone);

    @Query(value = "SELECT * FROM users WHERE dtype='ThirdParty' AND" +
            " username LIKE %:username% AND company_name LIKE %:companyName% " +
            "AND nif LIKE %:nif% ", nativeQuery = true)
    List<User> searchThirdParty(@Param("username")String username,
                                    @Param("companyName") String companyName,
                                    @Param("nif") String nif);


    Optional<AccountHolder> findAccountHolderByNif(String nif);
}
