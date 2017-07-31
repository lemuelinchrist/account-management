package com.lemuelinchrist.exercise.facepalm.controllers;

import com.lemuelinchrist.exercise.facepalm.controllers.dto.FriendPairRequestDTO;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.FriendResponseDTO;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.RequestorTargetDTO;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.SuccessResponseDTO;
import com.lemuelinchrist.exercise.facepalm.exception.*;
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
     * <code>{
     * friends:
     * [
     * 'andy@example.com',
     * 'john@example.com'
     * ]
     * }</code>
     * <p>
     *
     * @param friendPairRequestDTO A json object containing two email addresses
     * @return the JSON response will return a { "success" : true }
     * @throws InvalidParameterException   will be thrown if the frinds list doesn't exactly have two emails, or if there is an
     *                                     empty email, or if there is a malformed email
     * @throws NonExistentAccountException The service will not accept emails that do not have a created account yet
     * @throws AccountBlockedException Thrown when one Account is blocking another
     */
    @RequestMapping(value = "/befriend", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> befriend(@RequestBody FriendPairRequestDTO friendPairRequestDTO)
            throws AccountBlockedException, InvalidParameterException, NonExistentAccountException {
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
     * <code>{
     * email: 'something@email.com'
     * }</code>
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
     * This service retrieves a common friends list between two email addresses.
     * The Json request object will have the following structure:
     * <code>{
     * friends:
     * [
     * 'andy@example.com',
     * 'john@example.com'
     * ]
     * }</code>
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

    /**
     * USER STORY # 4
     * This service subscribes an Account to another Account's updates. Note that this is not equivalent to "befriending".
     * An account can still subscribe to another regardless of friendship status.
     * The Json request object will have the following structure:
     * <p>
     * <code>
     * {
     * "requestor": "lisa@example.com",
     * "target": "john@example.com"
     * }</code>
     *
     * @param requestorTargetDTO a Json Object containing a requestor and a target. a target is the Account the requestor will be
     *                           subscribed to
     * @return the JSON response will return a { "success" : true }
     * @throws InvalidParameterException   Thrown if any of the two emails are empty or if the email is malformed
     * @throws AlreadySubscribedException  Thrown if requestor is already subscribed to target
     * @throws NonExistentAccountException Thrown if any of the emails are not existent
     */
    @RequestMapping(value = "/subscribe-updates", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponseDTO> subscribeUpdates(@RequestBody RequestorTargetDTO requestorTargetDTO)
            throws InvalidParameterException, AlreadySubscribedException, NonExistentAccountException {
        requestorTargetDTO.checkValidity();
        accountService.subscribeToUpdates(requestorTargetDTO.getRequestor(), requestorTargetDTO.getTarget());
        return ResponseEntity.ok().body(new SuccessResponseDTO());
    }

    /**
     * USER STORY #5
     * This service will block a targetted Account such that the requestor will not see any Activity from the former anymore.
     * If they are connected as friends, the requestor will no longer receive notifications from the target. If they are not connected
     * as friends, then no new friends can be added.
     *
     * @param requestorTargetDTO
     * @return the JSON response will return a { "success" : true }
     * @throws InvalidParameterException   Thrown if any of the two emails are empty or if the email is malformed
     * @throws AlreadyBlockedException     Thrown if requestor already blocked target before
     * @throws NonExistentAccountException Thrown if any of the emails are not existent
     */
    @RequestMapping(value = "/block-account", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponseDTO> blockAccount(@RequestBody RequestorTargetDTO requestorTargetDTO)
            throws InvalidParameterException, AlreadyBlockedException, NonExistentAccountException {
        requestorTargetDTO.checkValidity();
        accountService.blockAccount(requestorTargetDTO.getRequestor(), requestorTargetDTO.getTarget());
        return ResponseEntity.ok().body(new SuccessResponseDTO());
    }


}
