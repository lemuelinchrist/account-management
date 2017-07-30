package com.lemuelinchrist.exercise.facepalm.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemuelinchrist.exercise.facepalm.controllers.dto.BeFriendDTO;
import com.lemuelinchrist.exercise.facepalm.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.assertj.core.api.Assertions.assertThat;
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
    public void shouldBefriendTwoAccountsWithProperEmails() throws Exception {
        String firstEmail = "good@email.com";
        String secondEmail = "secondGood@email.com";

        RequestBuilder requestBuilder = getRequestBuilder(firstEmail, secondEmail);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is("true")))
                .andReturn();

    }

    @Test
    public void shouldNotAllowBadParameters() throws Exception {

        RequestBuilder requestBuilder = getRequestBuilder("bad", "secondGood@email.com");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getErrorMessage()).containsIgnoringCase("Wrong Parameters");

        requestBuilder = getRequestBuilder("ok@good.com", "");

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getErrorMessage()).containsIgnoringCase("Wrong Parameters");
        assertThat(result.getResolvedException().getMessage()).containsIgnoringCase("empty");

    }

    private RequestBuilder getRequestBuilder(String firstEmail, String secondEmail) throws Exception {
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