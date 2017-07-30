package com.lemuelinchrist.exercise.facepalm.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountTest {

    @Autowired
    AccountRepository accountRepository;
    private String FIRST_EMAIL;
    private String SECOND_EMAIL;
    private String THIRD_EMAIL;

    @Before
    public void createAccounts() {
        Account firstAccount = new Account();
        FIRST_EMAIL = "firstEmail@facepalm.com";
        firstAccount.setEmail(FIRST_EMAIL);
        accountRepository.save(firstAccount);
        Account secondAccount = new Account();
        SECOND_EMAIL = "secondEmail@facepalm.com";
        secondAccount.setEmail(SECOND_EMAIL);
        accountRepository.save(secondAccount);
        Account thirdAccount = new Account();
        THIRD_EMAIL = "thirdEmail@facepalm.com";
        thirdAccount.setEmail(THIRD_EMAIL);
        accountRepository.save(thirdAccount);
    }

    @Test
    public void accountsShouldBeAllowedToHaveManyFriends() {
        Account firstAccount = accountRepository.findByEmail(FIRST_EMAIL);
        Account secondAccount = accountRepository.findByEmail(SECOND_EMAIL);
        firstAccount.addFriend(secondAccount);
        secondAccount.addFriend(firstAccount);
        accountRepository.save(firstAccount);
        accountRepository.save(secondAccount);

        assertThat(firstAccount.getFriends()).contains(secondAccount);
        assertThat(secondAccount.getFriends()).contains(firstAccount);
        assertThat(firstAccount.getFriends()).doesNotContain(accountRepository.findByEmail(THIRD_EMAIL));

        firstAccount.addFriend(secondAccount);
        accountRepository.save(firstAccount);
        System.out.println("hello");
    }

}