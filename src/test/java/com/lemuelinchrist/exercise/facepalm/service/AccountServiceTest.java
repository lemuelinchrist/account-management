package com.lemuelinchrist.exercise.facepalm.service;

import com.lemuelinchrist.exercise.facepalm.FacepalmApplication;
import com.lemuelinchrist.exercise.facepalm.exception.ExistingEmailException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.model.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


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
        newAccount.setEmail("testEmail@gmail.com");
        newAccount.setId(1L);
        Mockito.when(accountRepository.save(newAccount)).thenReturn(newAccount);

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


}
