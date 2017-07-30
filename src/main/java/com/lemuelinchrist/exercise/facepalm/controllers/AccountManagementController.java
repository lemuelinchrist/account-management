package com.lemuelinchrist.exercise.facepalm.controllers;

import com.lemuelinchrist.exercise.facepalm.controllers.dto.BeFriendDTO;
import com.lemuelinchrist.exercise.facepalm.exception.InvalidParameterException;
import com.lemuelinchrist.exercise.facepalm.exception.NonExistentAccountException;
import com.lemuelinchrist.exercise.facepalm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Account Management Controller for the User Stories of this Project.
 *
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
@RestController
@RequestMapping("/account-management")
public class AccountManagementController {

    private AccountService accountService;

    @Autowired
    public AccountManagementController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * USER STORY # 1
     * This Service will create a friend connection (befriend) between two email addresses.
     * The Json Object will have the following structure:
     * <p>
     * {
     * friends:
     * [
     * 'andy@example.com',
     * 'john@example.com'
     * ]
     * }
     * <p>
     * the JSON response will return a { "success" : true }
     *
     * @param beFriendDTO A json object containing two email addresses
     * @return
     * @throws InvalidParameterException   will be thrown if the frinds list doesn't exactly have two emails, or if there is an
     *                                     empty email, or if there is a malformed email
     * @throws NonExistentAccountException The service will not accept emails that do not have a created account yet
     */
    @RequestMapping(value = "/befriend", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> befriend(@RequestBody BeFriendDTO beFriendDTO) throws InvalidParameterException, NonExistentAccountException {
        beFriendDTO.checkValidity();
        accountService.befriendAccounts(beFriendDTO.getFirstFriend(), beFriendDTO.getSecondFriend());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("success", "true");
        return ResponseEntity.ok().body(responseBody);
    }


}
