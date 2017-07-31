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

    // USER STORY 1
    @Test
    public void accountsShouldBeAllowedToHaveManyFriends() throws Exception {
        Account firstAccount = accountRepository.findByEmail(FIRST_EMAIL).orElseThrow(Exception::new);
        Account secondAccount = accountRepository.findByEmail(SECOND_EMAIL).orElseThrow(Exception::new);
        Account thirdAccount = accountRepository.findByEmail(THIRD_EMAIL).orElseThrow(Exception::new);
        firstAccount.addFriend(secondAccount);
        secondAccount.addFriend(firstAccount);
        firstAccount = accountRepository.save(firstAccount);
        secondAccount = accountRepository.save(secondAccount);

        assertThat(firstAccount.getFriends()).contains(secondAccount);
        assertThat(secondAccount.getFriends()).contains(firstAccount);
        assertThat(firstAccount.getFriends()).doesNotContain(accountRepository.findByEmail(THIRD_EMAIL).orElseThrow(Exception::new));

        firstAccount.addFriend(thirdAccount);
        thirdAccount.addFriend(firstAccount);
        firstAccount = accountRepository.save(firstAccount);
        thirdAccount = accountRepository.save(thirdAccount);
        assertThat(firstAccount.getFriends()).contains(thirdAccount, secondAccount);
        assertThat(thirdAccount.getFriends()).contains(firstAccount);

        System.out.println("Hello " + accountRepository.findFriendListByEmail(FIRST_EMAIL));


    }


    // USER STORY 2
    @Test
    public void shouldBeAbleToFindFriendEmailsByAccount() throws Exception {
        establishFriends();

        assertThat(accountRepository.findFriendListByEmail(FIRST_EMAIL).orElseThrow(Exception::new))
                .contains(SECOND_EMAIL, THIRD_EMAIL);
        assertThat(accountRepository.findFriendListByEmail(SECOND_EMAIL).orElseThrow(Exception::new))
                .contains(FIRST_EMAIL, THIRD_EMAIL);


    }

    // USER STORY 3
    @Test
    public void shouldBeAbleToFindCommonFriendEmailsByAccount() throws Exception {
        establishFriends();

        assertThat(accountRepository.findCommonFriendsBetweenAccounts(FIRST_EMAIL, SECOND_EMAIL).orElseThrow(Exception::new))
                .containsExactly(THIRD_EMAIL);

    }

    private void establishFriends() throws Exception {
        Account firstAccount = accountRepository.findByEmail(FIRST_EMAIL).orElseThrow(Exception::new);
        Account secondAccount = accountRepository.findByEmail(SECOND_EMAIL).orElseThrow(Exception::new);
        Account thirdAccount = accountRepository.findByEmail(THIRD_EMAIL).orElseThrow(Exception::new);
        firstAccount.addFriend(secondAccount);
        firstAccount.addFriend(thirdAccount);
        accountRepository.save(firstAccount);

        secondAccount = accountRepository.findByEmail(SECOND_EMAIL).orElseThrow(Exception::new);
        secondAccount.addFriend(firstAccount);
        secondAccount.addFriend(thirdAccount);
        accountRepository.save(secondAccount);
    }

    // USER STORY 4, and 5
    @Test
    public void accountsShouldHaveSubscriptionsAndBlockers() throws Exception {
        Account firstAccount = accountRepository.findByEmail(FIRST_EMAIL).orElseThrow(Exception::new);
        Account secondAccount = accountRepository.findByEmail(SECOND_EMAIL).orElseThrow(Exception::new);
        Account thirdAccount = accountRepository.findByEmail(THIRD_EMAIL).orElseThrow(Exception::new);

        Account fourthAccount = new Account();
        String FOURTH_EMAIL = "fourthEmail@facepalm.com";
        fourthAccount.setEmail(FOURTH_EMAIL);
        accountRepository.save(fourthAccount);

        Account fifthAccount = new Account();
        String FIFTH_EMAIL = "fifthEmail@facepalm.com";
        fifthAccount.setEmail(FIFTH_EMAIL);
        accountRepository.save(fifthAccount);

        Account sixthAccount = new Account();
        String SIXTH_EMAIL = "sixthEmail@facepalm.com";
        sixthAccount.setEmail(SIXTH_EMAIL);
        accountRepository.save(sixthAccount);

        firstAccount.addBlockedAccount(fifthAccount);
        firstAccount.addBlockedAccount(sixthAccount);
        firstAccount.addSubscriber(secondAccount);
        firstAccount.addSubscriber(fourthAccount);
        firstAccount = accountRepository.save(firstAccount);
        fifthAccount.addBlockedAccount(firstAccount);
        fifthAccount = accountRepository.save(fifthAccount);

        assertThat(firstAccount.getBlockedAccounts()).contains(fifthAccount, sixthAccount);
        assertThat(firstAccount.getSubscribers()).contains(fourthAccount);
        assertThat(fifthAccount.getBlockedAccounts()).contains(firstAccount);


    }

}
