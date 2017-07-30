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
    List<String> findFriendEmailsByAccountEmail(String email);


}
