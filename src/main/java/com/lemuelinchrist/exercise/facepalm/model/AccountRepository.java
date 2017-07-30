package com.lemuelinchrist.exercise.facepalm.model;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);


}
