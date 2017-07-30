package com.lemuelinchrist.exercise.facepalm.service;

import com.lemuelinchrist.exercise.facepalm.exception.ExistingEmailException;
import com.lemuelinchrist.exercise.facepalm.exception.NonExistentAccountException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.model.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
        Account firstAccount = accountRepository.findByEmail(firstEmail).orElseThrow(NonExistentAccountException::new);
        Account secondAccount = accountRepository.findByEmail(secondEmail).orElseThrow(NonExistentAccountException::new);
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
     * @return returns a set of friend emails.
     * @throws NonExistentAccountException Thrown if the email doesn't exist in the database.
     */
    public List<String> getFriendEmailsByEmail(String email) throws NonExistentAccountException {

        //TODO: Create JPQL to fetch email for better efficiency
        final List<String> friendEmails = new ArrayList<>();
        for (Account account : getFriends(email)) {
            friendEmails.add(account.getEmail());
        }
        return friendEmails;
    }

    private Set<Account> getFriends(String email) throws NonExistentAccountException {
        return accountRepository.findByEmail(email).orElseThrow(NonExistentAccountException::new).getFriends();
    }
}
