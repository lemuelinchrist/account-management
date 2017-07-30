package com.lemuelinchrist.exercise.facepalm.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemuelinchrist.exercise.facepalm.exception.ExistingEmailException;
import com.lemuelinchrist.exercise.facepalm.model.Account;
import com.lemuelinchrist.exercise.facepalm.service.AccountService;
import org.junit.Assert;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lemuel Cantos
 * @since 29/7/2017
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = AccountCRUDController.class, secure = false)
public class AccountCRUDControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @Test
    public void shouldBeCreated() throws Exception {
        Account newAccount = new Account();
        newAccount.setEmail("testEmail@gmail.com");
        newAccount.setId(1L);
        Mockito.when(accountService.save(newAccount)).thenReturn(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/accounts")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(newAccount))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Assert.assertTrue(result.getResponse().getHeader("Location").contains("/accounts/1"));

    }

    @Test
    public void shouldBeRetrieved() throws Exception {
        Account newAccount = new Account();
        newAccount.setEmail("testEmail@gmail.com");
        newAccount.setId(1L);

        Mockito.when(accountService.findOne(Mockito.anyLong())).thenReturn(newAccount);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/accounts/1")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(newAccount))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

    @Test
    public void shouldNotHaveEmptyEmail() throws Exception {
        Account newAccount = new Account();
        newAccount.setEmail("");
        newAccount.setId(1L);
        Mockito.when(accountService.save(newAccount)).thenReturn(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/accounts")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(newAccount))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void shouldNotHaveBadEmailFormat() throws Exception {
        Account newAccount = new Account();
        newAccount.setEmail("badEmailFormat");
        newAccount.setId(1L);
        Mockito.when(accountService.save(newAccount)).thenReturn(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/accounts")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(newAccount))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldNotHaveExistingEmail() throws Exception {
        Account newAccount = new Account();
        newAccount.setEmail("existingEmail@something.com");
        newAccount.setId(1L);
        Mockito.when(accountService.save(newAccount)).thenThrow(new ExistingEmailException());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/accounts")
                .accept(MediaType.APPLICATION_JSON).content(convertToJson(newAccount))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getErrorMessage()).containsIgnoringCase("exist");
    }


    public String convertToJson(Object o) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }


}
