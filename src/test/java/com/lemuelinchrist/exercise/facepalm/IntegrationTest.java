package com.lemuelinchrist.exercise.facepalm;

import com.lemuelinchrist.exercise.facepalm.controllers.dto.FriendPairRequestDTO;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.FriendResponseDTO;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.RequestorTargetDTO;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.SuccessResponseDTO;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * This Integration Test Tests all Use Cases as a whole
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void accountShouldBeCreated() throws Exception {

        String email = "newEmail@yougotmail.com";
        Account createdAccount = createAccount(email);

        ResponseEntity<Account> responseEntity = restTemplate.getForEntity("/accounts/" + createdAccount.getId(), Account.class);
        assertThat(responseEntity.getBody().getEmail()).isEqualToIgnoringCase(email);

    }

    /**
     * USER STORY #1 - #6
     *
     * @throws Exception
     */
    @Test
    public void testAllUserStories() throws Exception {
        // *** create accounts
        String firstEmail = "firstAccount@yougotmail.com";
        String secondEmail = "secondAccount@yougotmail.com";

        // *********** USER STORY 1
        Account firstAccount = createAccount(firstEmail);
        Account secondAccount = createAccount(secondEmail);

        befriendAccounts(firstEmail, secondEmail);

        // *** try adding a second friend
        String thirdEmail = "third@email.com";
        Account thirdAccount = createAccount(thirdEmail);
        befriendAccounts(firstEmail, thirdEmail);

        // *********** USER STORY 2
        // *** lets get the list
        HttpEntity<Account> friendListRequestEntity = new HttpEntity<>(firstAccount);
        ResponseEntity<FriendResponseDTO> friendsDTOResponseEntity = restTemplate
                .postForEntity("/account-management/get-friends", friendListRequestEntity, FriendResponseDTO.class);
        FriendResponseDTO friendResponseDTO = friendsDTOResponseEntity
                .getBody();

        assertThat(friendResponseDTO.getSuccess()).isEqualToIgnoringCase("true");
        assertThat(friendResponseDTO.getFriends()).contains(secondEmail, thirdEmail);
        assertThat(friendResponseDTO.getCount()).isEqualTo(2);

        // ************* USER STORY 3
        // create more accounts
        Account fourthAccount = createAccount("fourth@simple.com");
        Account fifthAccount = createAccount("fifth@responsibility.com");
        befriendAccounts(firstEmail, fourthAccount.getEmail());
        befriendAccounts(secondEmail, fourthAccount.getEmail());
        befriendAccounts(firstEmail, fifthAccount.getEmail());
        befriendAccounts(secondEmail, fifthAccount.getEmail());

        HttpEntity<FriendPairRequestDTO> commonFriendListRequestEntity = new HttpEntity<>(new FriendPairRequestDTO(firstEmail, secondEmail));
        ResponseEntity<FriendResponseDTO> commonFriendsDTOResponseEntity = restTemplate
                .postForEntity("/account-management/get-common-friends", commonFriendListRequestEntity, FriendResponseDTO.class);
        FriendResponseDTO commonFriendResponseDTO = commonFriendsDTOResponseEntity
                .getBody();

        assertThat(commonFriendResponseDTO.getSuccess()).isEqualToIgnoringCase("true");
        assertThat(commonFriendResponseDTO.getFriends()).contains(fourthAccount.getEmail(), fifthAccount.getEmail());
        assertThat(commonFriendResponseDTO.getFriends()).doesNotContain(thirdEmail);
        assertThat(commonFriendResponseDTO.getCount()).isEqualTo(2);

        // ***************** USER STORY 4
        // subscribe to a few accounts
        subscribeToAccount(thirdEmail, fourthAccount.getEmail());
        subscribeToAccount(thirdEmail, firstEmail);
        subscribeToAccount(thirdEmail, fifthAccount.getEmail());

        firstAccount = getAccount(firstAccount.getId());


    }

    private void subscribeToAccount(String requestorEmail, String targetEmail) {
        HttpEntity<RequestorTargetDTO> subscriptionRequestEntity = new HttpEntity<>(new RequestorTargetDTO(requestorEmail, targetEmail));
        ResponseEntity<SuccessResponseDTO> subscriptionResponse = restTemplate
                .postForEntity("/account-management/subscribe-updates", subscriptionRequestEntity, SuccessResponseDTO.class);
        SuccessResponseDTO successResponseDTO = subscriptionResponse
                .getBody();
        assertThat(successResponseDTO.getSuccess()).isEqualToIgnoringCase("true");
    }

    private FriendPairRequestDTO befriendAccounts(String firstEmail, String secondEmail) {
        FriendPairRequestDTO friendPairRequestDTO = new FriendPairRequestDTO();
        friendPairRequestDTO.setFriends(Arrays.asList(firstEmail, secondEmail));

        HttpEntity<FriendPairRequestDTO> requestEntity = new HttpEntity<>(friendPairRequestDTO);
        ResponseEntity<Map> responseEntity =
                restTemplate.postForEntity("/account-management/befriend", requestEntity, Map.class);
        Map body = responseEntity.getBody();
        assertThat(body.get("success")).isEqualTo("true");
        return friendPairRequestDTO;
    }


    private Account createAccount(String email) {
        Account newAccount = new Account();
        newAccount.setEmail(email);

        HttpEntity<Account> requestEntity = new HttpEntity<>(newAccount);
        ResponseEntity<Account> responseEntity =
                restTemplate.postForEntity("/accounts", requestEntity, Account.class);
        Account createdAccount = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertThat(createdAccount.getId()).isNotNull();

        return createdAccount;
    }

    private Account getAccount(Long accountId) {
        return restTemplate.getForEntity("/accounts/" + accountId, Account.class).getBody();
    }


}
