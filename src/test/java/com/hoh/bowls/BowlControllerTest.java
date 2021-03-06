package com.hoh.bowls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoh.accounts.Account;
import com.hoh.accounts.AccountDto;
import com.hoh.accounts.AccountService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jeong on 2016-03-01.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
public class BowlControllerTest {

    MockMvc mockMvc = null;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BowlService service;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;


    Account account     =   null;
    Bowl bowl           =   null;

    @Before
    public void setUp() throws Exception {
        account       =   null;
        bowl          =   null;

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(filterChainProxy)
                .build();
    }

    @After
    public void setDown() throws Exception {
        if(account != null)
            accountService.deleteAccount(account.getId());

        if(bowl != null)
            service.deleteBowl(bowl.getId());

    }


    public static AccountDto.Create accountCreateFixture(){
        AccountDto.Create createDto =   new AccountDto.Create();
        createDto.setUsername("JinbeomTest");
        createDto.setEmail("jinbeomjeongTest@google.com");
        createDto.setPassword("123456");
        createDto.setFemale(false);
        createDto.setSingle(true);
        createDto.setBirth(1983);
        createDto.setResidence("충청북도 영동군");


        return createDto;
    }


    public static BowlDto.Create bowlCreateFixture(){
        BowlDto.Create createDto =   new BowlDto.Create();
        createDto.setTitle("위험에 빠진 할머니 돕기");
        createDto.setTheme(BowlType.Senior);
        createDto.setSummary("폐지를 주우시며 힘들었던 할머니가 몸까지 않좋아 지셨어요, 이젠 저희가 도울 때 입니다.");
        createDto.setOrg("십시일반");
        createDto.setContents("<img src='/filesystem/img/Koala.jpg'/>생활보허 대상자 할머니가 폐지를 주우며 근근히 살아가셨는데, 얼마전 허리를 다치셔서 이제는 거동이 불편" +
                "하싶니다, 아끼며 드시는 쌀은 곰팡이가 든지 오래 됬습니다, 안 믿기시겠지만 저희가 무관심할 때 주위 한 편에서는" +
                "상상하기도 어려움 일들이 벌어지고 있습니다.");

        createDto.setStartday(new Date());
        createDto.setEndday(new Date());


        return createDto;
    }


    @Test
    public void testCreateBowl() throws Exception {
        AccountDto.Create create    =   accountCreateFixture();
        account                     =   accountService.createAccount(create);

        BowlDto.Create createBowl          =   bowlCreateFixture();

        
        ResultActions result = mockMvc.perform(post("/api/v1/bowls/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBowl))
                .with(httpBasic(create.getEmail(), create.getPassword())));

        result.andDo(print());
        result.andExpect(status().isCreated());
    }

    @Test
     public void testGetBowl() throws Exception {
        AccountDto.Create create    =   accountCreateFixture();
        account                     =   accountService.createAccount(create);

        BowlDto.Create createBowl       =   bowlCreateFixture();
        bowl                            =   service.createBowl(createBowl);


        ResultActions result = mockMvc.perform(get("/api/v1/bowls/" + bowl.getId())
                .with(httpBasic(create.getEmail(), create.getPassword())));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void testUploadImage() throws Exception {
        AccountDto.Create create    =   accountCreateFixture();
        account                     =   accountService.createAccount(create);

        BowlDto.Create createBowl       =   bowlCreateFixture();
        bowl                            =   service.createBowl(createBowl);

        File ufile   =   new File("C:\\notitle.PNG");

        MockMultipartFile file      =   new MockMultipartFile("file", "upload.PNG", "text/plain", Files.readAllBytes(ufile.toPath()));


        ResultActions result = mockMvc.perform(fileUpload("/api/v1/bowls/image/upload")
                .file(file)
                .param("name", "upload.PNG")
                .with(httpBasic(create.getEmail(), create.getPassword())));


        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetBowls() throws Exception {
        AccountDto.Create create     =   accountCreateFixture();
        account                      =   accountService.createAccount(create);

        BowlDto.Create createBowl   =   bowlCreateFixture();
        bowl                        =   service.createBowl(createBowl);


        ResultActions result = mockMvc.perform(get("/api/v1/bowls/")
                .param("theme", "Senior")
                .param("size", "2")
                .with(httpBasic(create.getEmail(), create.getPassword())));


        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void testUpdateBowl() throws Exception {
        AccountDto.Create create        =   accountCreateFixture();
        Account account                 =   accountService.createAccount(create);

        BowlDto.Create createBowl       =   bowlCreateFixture();
        bowl                            =   service.createBowl(createBowl);


        BowlDto.Update  updateDto       =   modelMapper.map(createBowl, BowlDto.Update.class);

        updateDto.setOrg("TenSpoon");

        ResultActions result = mockMvc.perform(put("/api/v1/bowls/"+bowl.getId())
                .with(httpBasic(create.getEmail(), create.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.org", is("TenSpoon")));
    }

    @Test
    public void testCloseBowl() throws Exception {
        AccountDto.Create create           =   accountCreateFixture();
        BowlDto.Create createBowl          =   bowlCreateFixture();

        Account account                 =   accountService.createAccount(create);
        bowl                            =   service.createBowl(createBowl);


        ResultActions result = mockMvc.perform(delete("/api/v1/bowls/" + bowl.getId())
                .with(httpBasic(create.getEmail(), create.getPassword())));

        result.andDo(print());
        result.andExpect(status().isNoContent());
    }

    @Test
    public void testHandlerBowlNotFoundException() throws Exception {

    }
}