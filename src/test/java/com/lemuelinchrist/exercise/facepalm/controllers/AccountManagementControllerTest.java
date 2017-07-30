package com.lemuelinchrist.exercise.facepalm.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.BeFriendDTO;
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

    @Test
    public void friendListRequestShouldShowExactEmails() throws Exception {
        Account account = new Account();
        String email = "testEmail@gmail.com";
        account.setEmail(email);
        List<String> friendList = Arrays.asList("first@email.com", "second@email.com", "third@email.com");

        Mockito.when(accountService.getFriendEmailsByEmail(email)).thenReturn(friendList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/account-management/get-friends")
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

    private RequestBuilder sendBefriendRequest(String firstEmail, String secondEmail) throws Exception {
        BeFriendDTO beFriendDTO = new BeFriendDTO();
        beFriendDTO.setFriends(Arrays.asList(firstEmail, secondEmail));
        return MockMvcRequestBuilders
                .post("/account-management/befriend")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(beFriendDTO))
                .contentType(MediaType.APPLICATION_JSON);
    }

    public String convertToJson(Object o) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

}