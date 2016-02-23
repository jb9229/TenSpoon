package com.hoh.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoh.Application;
import com.hoh.accounts.Account;
import com.hoh.accounts.AccountDto;
import com.hoh.accounts.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jeong on 2015-10-20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AccountControllerTest {

    MockMvc mockMvc = null;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    AccountService service;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(filterChainProxy)
                .build();
    }



    public static AccountDto.Create accountCreateFixture(){
        AccountDto.Create createDto =   new AccountDto.Create();
        createDto.setUsername("Jinbeom");
        createDto.setEmail("jinbeomjeong@google.com");
        createDto.setPassword("123456");
        createDto.setFemale(false);
        createDto.setSingle(true);
        createDto.setBirth(1983);
        createDto.setResidence("충청북도 영동군");


        return createDto;
    }



    @Test
    public void createAccount() throws Exception {

        AccountDto.Create createDto =   accountCreateFixture();



        ResultActions result = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));
        result.andExpect(jsonPath("$.username", is("Jinbeom")));

        result.andDo(print());
        result.andExpect(status().isCreated());


        Account newAccount  =   service.getAccount((long)1);

        Double authMailKey  =   newAccount.getAuthMailkey();

        assertNotNull(authMailKey);

        ResultActions authMailResult = mockMvc.perform(get("/api/v1/auth/accounts/" + createDto.getEmail() + "/" + authMailKey));


        authMailResult.andDo(print());
        authMailResult.andExpect(status().isOk());

    }

    @Test
    public void createAccount_BedRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("   ");
        createDto.setPassword("1234");

        ResultActions result    =   mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createAccout_DuplicatedRequest() throws Exception {

        AccountDto.Create createDto =  accountCreateFixture();

        ResultActions result = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isCreated());

        result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));


        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }



    @Test
    public void chekAuthEmailAccount() throws Exception {
        AccountDto.Create   createDto   =   accountCreateFixture();
        createDto.setAuthMailKey(0.234);

        Account account                 =   service.createAccount(createDto);

        ResultActions result            =   mockMvc.perform(get("/api/v1/accounts/"+account.getId())
                .with(httpBasic(createDto.getEmail(), createDto.getPassword())));

        result.andDo(print());
        result.andExpect(status().isUnauthorized());
    }



    @Test
    public void getAccounts() throws Exception {
        AccountDto.Create create        =   accountCreateFixture();

        Account account                 =   service.createAccount(create);

        ResultActions result    =   mockMvc.perform(get("/api/v1/accounts")
                .param("email", "jinbeomjeong@google.com")
                .param("size", "2")
                .with(httpBasic(create.getEmail(), create.getPassword())));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void getAccount() throws Exception {
        AccountDto.Create   createDto   =   accountCreateFixture();
        createDto.setAuthMailKey(0.234);

        Account account                 =   service.createAccount(createDto);

        ResultActions result            =   mockMvc.perform(get("/api/v1/accounts/"+account.getId())
                .with(httpBasic(createDto.getEmail(), createDto.getPassword())));

        result.andDo(print());
        result.andExpect(status().isOk());
    }



    @Test
    public void updateAccount() throws Exception {
        AccountDto.Create   createDto   =   accountCreateFixture();
        Account account                 =   service.createAccount(createDto);

        AccountDto.Update   updateDto   =   new AccountDto.Update();
        updateDto.setPassword("changePassword");
        updateDto.setUsername("Jeong Jinbeom");

        ResultActions resultActions     =   mockMvc.perform(put("/api/v1/accounts/" + account.getId())
                .with(httpBasic(createDto.getEmail(), createDto.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.username", is("Jeong Jinbeom")));
    }



    @Test
    public void deleteAccount() throws Exception {
        AccountDto.Create   createDto   =   accountCreateFixture();
        Account account                 =   service.createAccount(createDto);


        ResultActions resultActions     =   mockMvc.perform(delete("/api/v1/accounts/" + account.getId())
        .with(httpBasic(createDto.getEmail(), createDto.getPassword())));


        resultActions.andDo(print());
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteAccount_BedRequest() throws Exception {
        ResultActions resultActions     =   mockMvc.perform(delete("/api/v1/accounts/1"));


        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());
    }



    @Test
    public void loginAccount() throws Exception {
        AccountDto.Create   createDto   =   accountCreateFixture();
        Account account                 =   service.createAccount(createDto);


        ResultActions resultActions     =   mockMvc.perform(post("/api/v1/auth/login").param("email", "jinbeomjeong@google.com").param("password", "123456"));

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void logoutAccount() throws Exception {
        AccountDto.Create   createDto   =   accountCreateFixture();
        Account account                 =   service.createAccount(createDto);

        ResultActions resultActions     =   mockMvc.perform(get("/api/v1/auth/logout"));

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
    }


}