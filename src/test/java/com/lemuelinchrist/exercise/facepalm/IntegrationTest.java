package com.lemuelinchrist.exercise.facepalm;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
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
        Account newAccount = new Account();
        newAccount.setEmail("newEmail@yougotmail.com");
        HttpEntity<Account> requestEntity = new HttpEntity<>(newAccount);

        ResponseEntity<Account> responseEntity =
                restTemplate.postForEntity("/accounts", requestEntity, Account.class);
        Account createdAccount = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertThat(createdAccount.getId()).isNotNull();

        responseEntity = restTemplate.getForEntity("/accounts/" + createdAccount.getId(), Account.class);
        assertThat(responseEntity.getBody().getEmail()).isEqualToIgnoringCase("newEmail@yougotmail.com");

    }
}
