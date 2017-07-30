package com.lemuelinchrist.exercise.facepalm.service;

import com.lemuelinchrist.exercise.facepalm.exception.ExistingEmailException;
import com.lemuelinchrist.exercise.facepalm.exception.NonExistentAccountException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.model.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Account Service is the Service bean that handles all kinds of management of Account from CRUD operations to querying.
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


    public Long save(Account newAccount) throws ExistingEmailException {
        if (accountRepository.findByEmail(newAccount.getEmail()).isPresent()) throw new ExistingEmailException();
        accountRepository.save(newAccount);
        return newAccount.getId();
    }

    public List<Account> befriendAccounts(String firstEmail, String secondEmail) throws NonExistentAccountException {
        Account firstAccount = accountRepository.findByEmail(firstEmail).orElseThrow(NonExistentAccountException::new);
        Account secondAccount = accountRepository.findByEmail(secondEmail).orElseThrow(NonExistentAccountException::new);
        firstAccount.addFriend(secondAccount);
        secondAccount.addFriend(firstAccount);
        firstAccount = accountRepository.save(firstAccount);
        secondAccount = accountRepository.save(secondAccount);
        return Arrays.asList(firstAccount, secondAccount);

    }

    public Account findOne(Long id) {
        return accountRepository.findOne(id);
    }
}
