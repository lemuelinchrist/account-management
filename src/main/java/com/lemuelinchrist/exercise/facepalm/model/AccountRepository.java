package com.lemuelinchrist.exercise.facepalm.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @Query(value = "select f.email from Account a join a.friends f where a.email = ?1")
    Optional<List<String>> findFriendListByEmail(String email);

    @Query(value = "select distinct a.email from Account a where a.email " +
            "in (select f.email from Account a join a.friends f where a.email = ?1) " +
            "and a.email in (select f.email from Account a join a.friends f where a.email = ?2)")
    Optional<List<String>> findCommonFriendsBetweenAccounts(String firstEmail, String secondEmail);

    @Query(value = "select distinct a.email from Account a where " +
            "(a.email in (select f.email from Account a join a.friends f where a.email = ?1) " +
            "or a.email in (select s.email from Account a join a.subscribers s where a.email = ?1))")
    Optional<List<String>> findFriendsAndSubscribersOf(String email);

    Optional<List<Account>> findByBlockedAccountsContaining(Account account);

//    @Query(value = "select distinct a.email from Account a where a.email not in (select b.email from Account a join a.blockedAccounts b where a.email = ?1)")
//    Optional<List<String>> findBlockersOf(String email);


}
