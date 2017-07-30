package com.lemuelinchrist.exercise.facepalm.service;

import com.lemuelinchrist.exercise.facepalm.exception.ExistingEmailException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.model.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
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

    public Account findOne(Long id) {
        return accountRepository.findOne(id);
    }
}
