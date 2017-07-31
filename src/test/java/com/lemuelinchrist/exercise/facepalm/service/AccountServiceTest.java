package com.lemuelinchrist.exercise.facepalm.service;

import com.lemuelinchrist.exercise.facepalm.FacepalmApplication;
import com.lemuelinchrist.exercise.facepalm.exception.ExistingEmailException;
import com.lemuelinchrist.exercise.facepalm.exception.NonExistentAccountException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.model.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacepalmApplication.class)
public class AccountServiceTest {
    @MockBean
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Test
    public void accountShouldBeCreated() throws Exception {
        Account newAccount = new Account();
        String email = "testEmail@gmail.com";
        newAccount.setEmail(email);
        newAccount.setId(1L);
        Mockito.when(accountRepository.save(newAccount)).thenReturn(newAccount);
        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        Long id = accountService.save(newAccount);
        assert id == 1L;

    }

    @Test(expected = ExistingEmailException.class)
    public void shouldThrowExceptionWhenEmailExistsDuringCreation() throws ExistingEmailException {
        String email = "existing@email.com";
        Account newAccount = new Account();
        newAccount.setEmail(email);
        newAccount.setId(1L);
        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.of(newAccount));

        accountService.save(newAccount);

    }

    // USER STORY 1
    @Test
    public void twoAccountsShouldBeAbleToBecomeFriends() throws Exception {
        Account firstAccount = new Account();
        String firstEmail = "firstEmail@gmail.com";
        firstAccount.setEmail(firstEmail);
        firstAccount.setId(1L);
        Account secondAccount = new Account();
        String secondEmail = "secondEmail@gmail.com";
        secondAccount.setEmail(secondEmail);
        secondAccount.setId(2L);

        Mockito.when(accountRepository.findByEmail(firstEmail)).thenReturn(Optional.of(firstAccount));
        Mockito.when(accountRepository.findByEmail(secondEmail)).thenReturn(Optional.of(secondAccount));
        Mockito.when(accountRepository.save(firstAccount)).thenReturn(firstAccount);
        Mockito.when(accountRepository.save(secondAccount)).thenReturn(secondAccount);


        List<Account> list = accountService.befriendAccounts(firstEmail, secondEmail);
        assertThat(list).isNotNull();
        assertThat(list).extracting(Account::getEmail).contains(firstEmail, secondEmail);
        assertThat(list.get(0).getFriends()).hasSize(1);

    }

    // USER STORY 2
    @Test
    public void friendEmailListShouldBeRetrieved() throws Exception {
        Account account = new Account();
        String email = "firstEmail@gmail.com";
        account.setEmail(email);
        account.setId(1L);

        Account account2 = new Account();
        String email2 = "secondEmail@gmail.com";
        account2.setEmail(email2);
        account2.setId(1L);

        Account account3 = new Account();
        String email3 = "thirdEmail@gmail.com";
        account3.setEmail(email3);
        account3.setId(1L);

        account.setFriends(new HashSet<>(Arrays.asList(account2, account3)));

        List<String> value = Arrays.asList(email2, email3);
        Mockito.when(accountRepository.findFriendEmailsByAccountEmail(email)).thenReturn(Optional.of(value));
        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        assertThat(accountService.getFriendEmailsByEmail(email)).contains(email2, email3);

    }

    @Test
    public void friendEmailListOnNonexistenEmailShouldThrowException() throws Exception {
        Account account = new Account();
        String email = "nonexistent@gmail.com";
        account.setEmail(email);
        account.setId(123L);

        Account account2 = new Account();
        String email2 = "secondEmail@gmail.com";
        account2.setEmail(email2);
        account2.setId(1L);
        account.addFriend(account);

        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(accountRepository.findFriendEmailsByAccountEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getFriendEmailsByEmail(email));

    }


}
