package com.lemuelinchrist.exercise.facepalm.service;

import com.lemuelinchrist.exercise.facepalm.FacepalmApplication;
import com.lemuelinchrist.exercise.facepalm.exception.AccountBlockedException;
import com.lemuelinchrist.exercise.facepalm.exception.AlreadyBlockedException;
import com.lemuelinchrist.exercise.facepalm.exception.AlreadySubscribedException;
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
        Mockito.when(accountRepository.findFriendListByEmail(email)).thenReturn(Optional.of(value));
        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        assertThat(accountService.getFriendListByEmail(email)).contains(email2, email3);

    }

    // USER STORY 2
    @Test
    public void friendEmailListOnNonExistentEmailShouldThrowException() throws Exception {
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
        Mockito.when(accountRepository.findFriendListByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getFriendListByEmail(email));

    }

    // USER STORY 3
    @Test
    public void commonFriendEmailListOnNonExistentEmailsShouldThrowException() throws Exception {
        Account account = new Account();
        String email = "nonexistent@gmail.com";
        account.setEmail(email);
        account.setId(123L);

        Account account2 = new Account();
        String email2 = "secondNonexistentEmail@gmail.com";
        account2.setEmail(email2);
        account2.setId(1L);

        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(accountRepository.findByEmail(email2)).thenReturn(Optional.empty());
        Mockito.when(accountRepository.findFriendListByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getCommonFriendsBetweenAccounts(email, email2));

    }

    // USER STORY 3
    @Test
    public void commonFriendEmailListShouldBeRetrieved() throws Exception {

        Account account1 = createNewAccount(1L, "somehtingNew@hotmail.com");
        Account account2 = createNewAccount(2L, "somehtingNew2@hotmail.com");

        String otherEmail1 = "other1@one.com";
        String otherEmail2 = "another2@another.com";
        List<String> friendList = Arrays.asList(otherEmail1, otherEmail2);

        Mockito.when(accountRepository.findByEmail(account1.getEmail())).thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.findByEmail(account2.getEmail())).thenReturn(Optional.of(account2));
        Mockito.when(accountRepository.findCommonFriendsBetweenAccounts(account1.getEmail(), account2.getEmail()))
                .thenReturn(Optional.of(friendList));

        assertThat(accountService.getCommonFriendsBetweenAccounts(account1.getEmail(), account2.getEmail())).contains(friendList.get(0), friendList.get(1));


    }

    // USER STORY 4
    @Test
    public void targetShouldSubscribeToUpdateSuccesfully() throws Exception {
        Account requestor = createNewAccount(1L, "somehtingNew@hotmail.com");
        Account target = createNewAccount(2L, "somehtingNew2@hotmail.com");

        Mockito.when(accountRepository.findByEmail(requestor.getEmail())).thenReturn(Optional.of(requestor));
        Mockito.when(accountRepository.findByEmail(target.getEmail())).thenReturn(Optional.of(target));

        target = accountService.subscribeToUpdates(requestor.getEmail(), target.getEmail());
        assertThat(target.getSubscribers())
                .hasSize(1)
                .contains(requestor);
    }

    // USER STORY 4
    @Test
    public void shouldThrowExceptionIfAlreadySubscribed() throws Exception {
        Account requestor = createNewAccount(1L, "somehtngNew@hotmail.com");
        Account target = createNewAccount(2L, "somehtingNew2@hotmail.com");
        target.addSubscriber(requestor);

        Mockito.when(accountRepository.findByEmail(requestor.getEmail())).thenReturn(Optional.of(requestor));
        Mockito.when(accountRepository.findByEmail(target.getEmail())).thenReturn(Optional.of(target));

        assertThatThrownBy(() -> accountService.subscribeToUpdates(requestor.getEmail(), target.getEmail())).isInstanceOf(AlreadySubscribedException.class);

    }

    // USER STORY 5
    @Test
    public void requestorShouldBlockSuccessfully() throws Exception {
        Account requestor = createNewAccount(1L, "andSomehtingNew@hotmail.com");
        Account target = createNewAccount(2L, "somehtingNew2@hotmail.com");

        Mockito.when(accountRepository.findByEmail(requestor.getEmail())).thenReturn(Optional.of(requestor));
        Mockito.when(accountRepository.findByEmail(target.getEmail())).thenReturn(Optional.of(target));

        requestor = accountService.blockAccount(requestor.getEmail(), target.getEmail());
        assertThat(requestor.getBlockedAccounts())
                .hasSize(1)
                .contains(target);
    }

    // USER STORY 5
    @Test
    public void shouldThrowExceptionIfAlreadyBlocked() throws Exception {
        Account requestor = createNewAccount(1L, "somehtngNew21@hotmail.com");
        Account target = createNewAccount(2L, "somehtingNew2@hotmail.com");
        requestor.addBlockedAccount(target);

        Mockito.when(accountRepository.findByEmail(requestor.getEmail())).thenReturn(Optional.of(requestor));
        Mockito.when(accountRepository.findByEmail(target.getEmail())).thenReturn(Optional.of(target));

        assertThatThrownBy(() -> accountService.blockAccount(requestor.getEmail(), target.getEmail())).isInstanceOf(AlreadyBlockedException.class);

    }

    // USER STORY 5
    // USER STORY 1
    @Test
    public void twoAccountsShouldNotBecomeFriendsIfAnAccountIsBlocked() throws Exception {
        Account firstAccount = new Account();
        String firstEmail = "firstEmail1@gmail.com";
        firstAccount.setEmail(firstEmail);
        firstAccount.setId(1L);
        Account secondAccount = new Account();
        String secondEmail = "secondEmail@gmail.com";
        secondAccount.setEmail(secondEmail);
        secondAccount.setId(2L);
        secondAccount.addBlockedAccount(firstAccount);

        Mockito.when(accountRepository.findByEmail(firstEmail)).thenReturn(Optional.of(firstAccount));
        Mockito.when(accountRepository.findByEmail(secondEmail)).thenReturn(Optional.of(secondAccount));


        assertThatThrownBy(() -> accountService.befriendAccounts(firstEmail, secondEmail)).isInstanceOf(AccountBlockedException.class);

    }

    private Account createNewAccount(long id, String email) {
        Account account = new Account();
        account.setEmail(email);
        account.setId(id);
        return account;
    }


}
