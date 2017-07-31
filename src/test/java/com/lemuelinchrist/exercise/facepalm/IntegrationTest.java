package com.lemuelinchrist.exercise.facepalm;

import com.lemuelinchrist.exercise.facepalm.controllers.dto.FriendPairRequestDTO;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.FriendResponseDTO;
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
     * USER STORY #1
     *
     * @throws Exception
     */
    @Test
    public void twoAccountsShouldBecomeFriends() throws Exception {
        // *** create accounts
        String firstEmail = "firstAccount@yougotmail.com";
        String secondEmail = "secondAccount@yougotmail.com";

        Account firstAccount = createAccount(firstEmail);
        Account secondAccount = createAccount(secondEmail);

        FriendPairRequestDTO friendPairRequestDTO = new FriendPairRequestDTO();
        friendPairRequestDTO.setFriends(Arrays.asList(firstEmail, secondEmail));

        HttpEntity<FriendPairRequestDTO> requestEntity = new HttpEntity<>(friendPairRequestDTO);
        ResponseEntity<Map> responseEntity =
                restTemplate.postForEntity("/account-management/befriend", requestEntity, Map.class);
        Map body = responseEntity.getBody();
        assertThat(body.get("success")).isEqualTo("true");

        // *** try adding a second friend
        String thirdEmail = "third@email.com";
        Account thirdAccount = createAccount(thirdEmail);
        friendPairRequestDTO.setFriends(Arrays.asList(thirdEmail, firstEmail));
        requestEntity = new HttpEntity<>(friendPairRequestDTO);
        responseEntity =
                restTemplate.postForEntity("/account-management/befriend", requestEntity, Map.class);
        body = responseEntity.getBody();
        assertThat(body.get("success")).isEqualTo("true");

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


}
