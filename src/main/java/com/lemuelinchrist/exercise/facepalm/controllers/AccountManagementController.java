package com.lemuelinchrist.exercise.facepalm.controllers;

import com.lemuelinchrist.exercise.facepalm.controllers.dto.FriendPairRequestDTO;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.FriendResponseDTO;
import com.lemuelinchrist.exercise.facepalm.exception.InvalidParameterException;
import com.lemuelinchrist.exercise.facepalm.exception.NonExistentAccountException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
     * The Json request Object will have the following structure:
     * <p>
     * {
     * friends:
     * [
     * 'andy@example.com',
     * 'john@example.com'
     * ]
     * }
     * <p>
     *
     * @param friendPairRequestDTO A json object containing two email addresses
     * @return the JSON response will return a { "success" : true }
     * @throws InvalidParameterException   will be thrown if the frinds list doesn't exactly have two emails, or if there is an
     *                                     empty email, or if there is a malformed email
     * @throws NonExistentAccountException The service will not accept emails that do not have a created account yet
     */
    @RequestMapping(value = "/befriend", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> befriend(@RequestBody FriendPairRequestDTO friendPairRequestDTO) throws InvalidParameterException, NonExistentAccountException {
        friendPairRequestDTO.checkValidity();

        accountService.befriendAccounts(friendPairRequestDTO.getFirstFriend(), friendPairRequestDTO.getSecondFriend());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("success", "true");
        return ResponseEntity.ok().body(responseBody);
    }

    /**
     * USER STORY # 2
     * This service retrieves the friends list for an account
     * the request body should have the following structure:
     * {
     * email: 'something@email.com'
     * }
     *
     * @param account a json request with an 'email' parameter
     * @return returns json object with the following structure: {success: true, friends: ['email@email.com'], count:1 }
     * @throws NonExistentAccountException The service will not accept emails that do not have a created account yet
     */
    @RequestMapping(value = "/get-friends", method = RequestMethod.POST)
    public ResponseEntity<FriendResponseDTO> getFriends(@Valid @RequestBody Account account) throws NonExistentAccountException {
        List<String> friends = accountService.getFriendListByEmail(account.getEmail());
        FriendResponseDTO friendResponseDTO = new FriendResponseDTO("true", friends, friends.size());
        return ResponseEntity.ok().body(friendResponseDTO);
    }

    /**
     * User Story # 3
     * This service retrieves a common friends list between two email addresses
     * The Json request object will have the following structure:
     * {
     * friends:
     * [
     * 'andy@example.com',
     * 'john@example.com'
     * ]
     * }
     *
     * @param friendPairRequestDTO A json object containing two email addresses
     * @return returns a json object with the following structure: {success: true, friends: ['email@email.com'], count:1 }
     * @throws InvalidParameterException   will be thrown if the frinds list doesn't exactly have two emails, or if there is an
     *                                     empty email, or if there is a malformed email
     * @throws NonExistentAccountException The service will not accept emails that do not have a created account yet
     */
    @RequestMapping(value = "/get-common-friends", method = RequestMethod.POST)
    public ResponseEntity<FriendResponseDTO> getCommonFriends(@RequestBody FriendPairRequestDTO friendPairRequestDTO)
            throws InvalidParameterException, NonExistentAccountException {
        friendPairRequestDTO.checkValidity();
        List<String> commonFriends = accountService
                .getCommonFriendsBetweenAccounts(friendPairRequestDTO.getFirstFriend(), friendPairRequestDTO.getSecondFriend());
        FriendResponseDTO friendResponseDTO = new FriendResponseDTO("true", commonFriends, commonFriends.size());
        return ResponseEntity.ok().body(friendResponseDTO);

    }




}
