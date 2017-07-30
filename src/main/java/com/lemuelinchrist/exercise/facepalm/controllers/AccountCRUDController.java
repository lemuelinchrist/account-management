package com.lemuelinchrist.exercise.facepalm.controllers;

import com.lemuelinchrist.exercise.facepalm.exception.ExistingEmailException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * REST Controller Specifically for CRUD functions of Accounts
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
@RestController
@RequestMapping("/accounts")
public class AccountCRUDController {

    private AccountService accountService;

    @Autowired
    public AccountCRUDController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Account> create(@Valid @RequestBody Account newAccount) throws ExistingEmailException {
        Long id = accountService.save(newAccount);
        newAccount.setId(id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).body(newAccount);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    Account get(@PathVariable Long id) {
        return accountService.findOne(id);
    }



}
