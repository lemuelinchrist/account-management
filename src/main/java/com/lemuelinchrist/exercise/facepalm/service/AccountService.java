package com.lemuelinchrist.exercise.facepalm.service;

import com.lemuelinchrist.exercise.facepalm.exception.AlreadySubscribedException;
import com.lemuelinchrist.exercise.facepalm.exception.ExistingEmailException;
import com.lemuelinchrist.exercise.facepalm.exception.NonExistentAccountException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.model.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Account Service is the Service bean that handles all kinds of management of Account from CRUD operations to querying.
 * This class also contains all logic for the given user stories of the project.
 *
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
@Component
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    /**
     * Gets Account according to the ID of the Account.
     *
     * @param id the ID of the account
     * @return returns the instance of the Account with the corresponding ID
     */
    public Account findOne(Long id) {
        return accountRepository.findOne(id);
    }

    /**
     * Creates a new Account. It contains an email field that should be unique to all accounts.
     *
     * @param newAccount This Account object requires a unique email field.
     * @return returns a generated Id of the object
     * @throws ExistingEmailException Thrown if the email of the Account object already exists in the database.
     */
    public Long save(Account newAccount) throws ExistingEmailException {
        if (accountRepository.findByEmail(newAccount.getEmail()).isPresent()) throw new ExistingEmailException();
        accountRepository.save(newAccount);
        return newAccount.getId();
    }

    /**
     * USER STORY #1
     * This function will create a friend connection between two accounts of the given emails
     *
     * @param firstEmail The email of the first Account
     * @param secondEmail The email of the second Account
     * @return returns a list of the two involved accounts with their updated friends field
     * @throws NonExistentAccountException Thrown if Account in the emails don't exist
     */
    public List<Account> befriendAccounts(String firstEmail, String secondEmail) throws NonExistentAccountException {
        Account firstAccount = checkIfEmailExistsAndGetAccount(firstEmail);
        Account secondAccount = checkIfEmailExistsAndGetAccount(secondEmail);
        firstAccount.addFriend(secondAccount);
        secondAccount.addFriend(firstAccount);
        firstAccount = accountRepository.save(firstAccount);
        secondAccount = accountRepository.save(secondAccount);
        return Arrays.asList(firstAccount, secondAccount);

    }

    /**
     * USER STORY #2
     * This function will return a list of emails of friends of an Account according to
     * the given email.
     *
     * @param email The email of the account to be retreived.
     * @return returns a list of friend emails.
     * @throws NonExistentAccountException Thrown if the email doesn't exist in the database.
     */
    public List<String> getFriendListByEmail(String email) throws NonExistentAccountException {
        checkIfEmailExistsAndGetAccount(email);

        return accountRepository.findFriendListByEmail(email).orElseGet(ArrayList::new);
    }

    /**
     * USER STORY #3
     * This function will return a list of emails of COMMON friends of two Accounts
     *
     * @param firstEmail  The first email to compare common friends.
     * @param secondEmail The second email to compare common friends.
     * @return returns a list of common friend emails.
     * @throws NonExistentAccountException Thrown if the emails don't exist in the database.
     */
    public List<String> getCommonFriendsBetweenAccounts(String firstEmail, String secondEmail) throws NonExistentAccountException {
        checkIfEmailExistsAndGetAccount(firstEmail);
        checkIfEmailExistsAndGetAccount(secondEmail);

        return accountRepository.findCommonFriendsBetweenAccounts(firstEmail, secondEmail).orElseGet(ArrayList::new);
    }

    /**
     * USER STORY #4
     * This function will subscribe a requesting Account to the updates of another target Account such that any activity that
     * the target account does will notify the subscribing Account. Both Accounts do not need to establish a friend connection.
     *
     * @param requestorEmail The email of the requesting Account
     * @param targetEmail    The email of the Account to subscribe to
     * @return the updated target Account
     * @throws NonExistentAccountException Thrown if any of the emails do not exist in the database
     * @throws AlreadySubscribedException  Thrown if the target already has requestor as subscriber
     */
    public Account subscribeToUpdates(String requestorEmail, String targetEmail) throws NonExistentAccountException, AlreadySubscribedException {
        Account requestor = checkIfEmailExistsAndGetAccount(requestorEmail);
        Account target = checkIfEmailExistsAndGetAccount(targetEmail);

        if (target.getSubscribers() != null && target.getSubscribers().contains(requestor)) {
            throw new AlreadySubscribedException();
        }

        target.addSubscriber(requestor);
        accountRepository.save(target);

        return target;
    }

    private Account checkIfEmailExistsAndGetAccount(String email) throws NonExistentAccountException {
        // check first if account exists
        return accountRepository.findByEmail(email).orElseThrow(NonExistentAccountException::new);
    }

}
