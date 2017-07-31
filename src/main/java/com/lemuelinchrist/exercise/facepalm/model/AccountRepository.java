package com.lemuelinchrist.exercise.facepalm.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @Query(value = "select f.email from Account a join a.friends f where a in (select a from Account a join a.friends f where a.email = ?1)")
    Optional<List<String>> findFriendListByEmail(String email);

    @Query(value = "select distinct f.email from Account a join a.friends f where f.email in (select f.email from Account a join a.friends f where a.email = ?1) and f.email in (select f.email from Account a join a.friends f where a.email = ?2)")
    Optional<List<String>> findCommonFriendEmailsByAccountEmail(String firstEmail, String secondEmail);


}
