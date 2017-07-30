package com.lemuelinchrist.exercise.facepalm.model;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);


}
