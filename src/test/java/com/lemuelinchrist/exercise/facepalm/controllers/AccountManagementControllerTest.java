package com.lemuelinchrist.exercise.facepalm.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.FriendPairRequestDTO;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.RequestorTargetDTO;
import com.lemuelinchrist.exercise.facepalm.exception.AlreadyBlockedException;
import com.lemuelinchrist.exercise.facepalm.exception.AlreadySubscribedException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Lemuel Cantos
 * @since 30/7/2017
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = AccountManagementController.class, secure = false)
public class AccountManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    // USE CASE 1
    @Test
    public void befriendRequestShouldBefriendTwoAccountsWithProperEmails() throws Exception {
        String firstEmail = "good@email.com";
        String secondEmail = "secondGood@email.com";

        RequestBuilder requestBuilder = sendBefriendRequest(firstEmail, secondEmail);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is("true")))
                .andReturn();

    }

    // USE CASE 1
    @Test
    public void befriendRequestShouldNotAllowBadParameters() throws Exception {

        RequestBuilder requestBuilder = sendBefriendRequest("bad", "secondGood@email.com");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getErrorMessage()).containsIgnoringCase("Wrong Parameters");

        requestBuilder = sendBefriendRequest("ok@good.com", "");

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getErrorMessage()).containsIgnoringCase("Wrong Parameters");
        assertThat(result.getResolvedException().getMessage()).containsIgnoringCase("empty");

    }

    // USE CASE 2
    @Test
    public void friendListRequestShouldShowExactEmails() throws Exception {
        Account account = new Account();
        String email = "testEmail@gmail.com";
        account.setEmail(email);
        List<String> friendList = Arrays.asList("first@email.com", "second@email.com", "third@email.com");

        Mockito.when(accountService.getFriendListByEmail(email)).thenReturn(friendList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/account-management/get-friends")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(account))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is("true")))
                .andExpect(jsonPath("$.friends", hasItem("first@email.com")))
                .andExpect(jsonPath("$.friends", hasItem("second@email.com")))
                .andExpect(jsonPath("$.friends", hasItem("third@email.com")))
                .andExpect(jsonPath("$.count", is(friendList.size())))
                .andReturn();
    }

    // USE CASE 3
    @Test
    public void commonFriendListRequestShouldShowExactEmails() throws Exception {
        Account account = new Account();
        String email = "testEmail@gmail.com";
        account.setEmail(email);
        Account account2 = new Account();
        String email2 = "testEmail2@gmail.com";
        account2.setEmail(email2);

        List<String> friendList = Arrays.asList("first@email.com", "second@email.com", "third@email.com");

        FriendPairRequestDTO friendPairRequestDTO = new FriendPairRequestDTO(account.getEmail(), account2.getEmail());
        Mockito.when(accountService.getCommonFriendsBetweenAccounts(account.getEmail(), account2.getEmail())).thenReturn(friendList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/account-management/get-common-friends")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(friendPairRequestDTO))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is("true")))
                .andExpect(jsonPath("$.friends", hasItem("first@email.com")))
                .andExpect(jsonPath("$.friends", hasItem("second@email.com")))
                .andExpect(jsonPath("$.friends", hasItem("third@email.com")))
                .andExpect(jsonPath("$.count", is(friendList.size())))
                .andReturn();
    }

    // USE CASE 4
    @Test
    public void requestUpdateShouldRegisterSuccessfully() throws Exception {
        Account account = new Account();
        String email = "testEmail@gmail.com";
        account.setEmail(email);
        Account account2 = new Account();
        String email2 = "testEmail2@gmail.com";
        account2.setEmail(email2);


        RequestorTargetDTO requestorTargetDTO = new RequestorTargetDTO(account.getEmail(), account2.getEmail());
        Mockito.when(accountService.subscribeToUpdates(account.getEmail(), account2.getEmail())).thenReturn(account2);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/account-management/subscribe-updates")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(requestorTargetDTO))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is("true")))
                .andReturn();
    }

    // USE CASE 4
    @Test
    public void requestUpdateWithAlreadySubscribedAccountWillThrowError() throws Exception {
        Account account = new Account();
        String email = "testEmail@gmail.com";
        account.setEmail(email);
        Account account2 = new Account();
        String email2 = "testEmail2@gmadil.com";
        account2.setEmail(email2);
        account2.addSubscriber(account);


        RequestorTargetDTO requestorTargetDTO = new RequestorTargetDTO(account.getEmail(), account2.getEmail());
        Mockito.when(accountService.subscribeToUpdates(account.getEmail(), account2.getEmail())).thenThrow(AlreadySubscribedException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/account-management/subscribe-updates")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(requestorTargetDTO))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getErrorMessage()).containsIgnoringCase("already");

    }

    // USE CASE 5
    @Test
    public void blockingAnAccountShouldRegisterSuccessfully() throws Exception {
        Account account = new Account();
        String email = "testEmail1@gmail.com";
        account.setEmail(email);
        Account account2 = new Account();
        String email2 = "testEmail2@gmail.com";
        account2.setEmail(email2);


        RequestorTargetDTO requestorTargetDTO = new RequestorTargetDTO(account.getEmail(), account2.getEmail());
        Mockito.when(accountService.blockAccount(account.getEmail(), account2.getEmail())).thenReturn(account2);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/account-management/block-account")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(requestorTargetDTO))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is("true")))
                .andReturn();
    }

    // USE CASE 5
    @Test
    public void blockingAnAccountThatIsAlreadyBlockedWillThrowError() throws Exception {
        Account account = new Account();
        String email = "testEmail1@gmail.com";
        account.setEmail(email);
        Account account2 = new Account();
        String email2 = "testEmail2@gmadil.com";
        account2.setEmail(email2);
        account2.addSubscriber(account);


        RequestorTargetDTO requestorTargetDTO = new RequestorTargetDTO(account.getEmail(), account2.getEmail());
        Mockito.when(accountService.subscribeToUpdates(account.getEmail(), account2.getEmail())).thenThrow(AlreadyBlockedException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/account-management/subscribe-updates")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(requestorTargetDTO))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getErrorMessage()).containsIgnoringCase("already");

    }

    private RequestBuilder sendBefriendRequest(String firstEmail, String secondEmail) throws Exception {
        FriendPairRequestDTO friendPairRequestDTO = new FriendPairRequestDTO();
        friendPairRequestDTO.setFriends(Arrays.asList(firstEmail, secondEmail));
        return MockMvcRequestBuilders
                .post("/account-management/befriend")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(friendPairRequestDTO))
                .contentType(MediaType.APPLICATION_JSON);
    }

    public String convertToJson(Object o) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

}