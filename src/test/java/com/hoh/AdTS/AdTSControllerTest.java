package com.hoh.AdTS;

import com.hoh.account.AccountControllerTest;
import com.hoh.accounts.Account;
import com.hoh.accounts.AccountDto;
import com.hoh.accounts.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by test on 2018-03-30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdTSControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired private AccountService accountService;
    @Autowired private AdTSService adTSService;


    //Fixture Method
    private static AdTSDto.Create createAdTSDto(int orderNum, String imgUrl, String link, String title, String summary, String contents) {
        AdTSDto.Create createDto = new AdTSDto.Create();
        createDto.setOrderNum(orderNum);
        createDto.setImgUrl(imgUrl);
        createDto.setLink(link);
        createDto.setTitle(title);
        createDto.setSummary(summary);
        createDto.setContents(contents);

        return createDto;
    }

    private static AdTSDto.Create createAdTSDtoFixture1(){
        return createAdTSDto(1, "http://tenspoon.com/admob", "http://cupang.com/product", "휴대용 모니터", "노트북 휴대용 모니터 ASUSE", "생산성을 두배 높일 수 있는 휴대용 모니터 어떠나요?");
    }


    @Test
    public void testPublish_FirstRequest() throws Exception {
        //Add Account
        AccountDto.Create create        =   AccountControllerTest.accountCreateFixture();

        Account account                 =   accountService.createAccount(create);


        assertThat(account.getAdtsOrderNum()).isEqualTo(1);



        //Add AdTS
        adTSService.createAdTS(createAdTSDtoFixture1());



        //Get First AdMob
        ResponseEntity<PublishDto.Publish> response = restTemplate
                .withBasicAuth("jb9229.", "adfasjkejo")
                .getForEntity("/api/v1/adts/"+account.getId(), PublishDto.Publish.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody().getPbsType().name()).isEqualTo(PublishType.AdTS.name());
        assertThat(response.getBody().getPbsType().getAdType()).isEqualTo(PublishType.AdTS.getAdType());

    }

    @Test
    public void testPublish_AdMob_SecondRequest() throws Exception {
        //Add Account
        AccountDto.Create create        =   AccountControllerTest.accountCreateFixture();

        Account account                 =   accountService.createAccount(create);   //Default OrderNum = 1



        //Add AdTS
        adTSService.createAdTS(createAdTSDtoFixture1());    //OrderNum 1
        adTSService.createAdTS(createAdTSDto(3, "http://tenspoon.com/admob", "http://cupang.com/product", "휴대용 모니터", "노트북 휴대용 모니터 ASUSE", "생산성을 두배 높일 수 있는 휴대용 모니터 어떠나요?"));


        //Request Order Num 1
        ResponseEntity<PublishDto.Publish> response = restTemplate
                .withBasicAuth("jb9229.", "adfasjkejo")
                .getForEntity("/api/v1/adts/"+account.getId(), PublishDto.Publish.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody().getPbsType().name()).isEqualTo(PublishType.AdTS.name());


        //Second Request => Next OrderNum not exist
        response = restTemplate
                .withBasicAuth("jb9229.", "adfasjkejo")
                .getForEntity("/api/v1/adts/"+account.getId(), PublishDto.Publish.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody().getPbsType().name()).isEqualTo(PublishType.AdMob.name());
    }

    @Test
    public void testPublish_Return_OrderNum() throws Exception {
        //Add Account
        AccountDto.Create create        =   AccountControllerTest.accountCreateFixture();

        Account account                 =   accountService.createAccount(create);   //Default OrderNum = 1



        //Add AdTS
        adTSService.createAdTS(createAdTSDtoFixture1());    //OrderNum 1
        adTSService.createAdTS(createAdTSDto(2, "http://tenspoon.com/admob", "http://cupang.com/product", "휴대용 모니터_v2", "노트북 휴대용 모니터 ASUSE", "생산성을 두배 높일 수 있는 휴대용 모니터 어떠나요?"));



        //Request Order Num 1
        ResponseEntity<PublishDto.AdTS> response = restTemplate
                .withBasicAuth("jb9229.", "adfasjkejo")
                .getForEntity("/api/v1/adts/"+account.getId(), PublishDto.AdTS.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody().getPbsType().name()).isEqualTo(PublishType.AdTS.name());



        //Second Request => OrderNum = 2
        response = restTemplate
                .withBasicAuth("jb9229.", "adfasjkejo")
                .getForEntity("/api/v1/adts/"+account.getId(), PublishDto.AdTS.class);

        PublishDto.AdTS adTS    =   (PublishDto.AdTS)response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody().getPbsType().name()).isEqualTo(PublishType.AdTS.name());
        assertThat(adTS.getTitle()).isEqualTo("휴대용 모니터_v2");



        //third Request => OrderNum = 3
        ResponseEntity<PublishDto.AdMob> adMobResponse = restTemplate
                .withBasicAuth("jb9229.", "adfasjkejo")
                .getForEntity("/api/v1/adts/"+account.getId(), PublishDto.AdMob.class);

        assertThat(adMobResponse.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(adMobResponse.getBody().getPbsType().name()).isEqualTo(PublishType.AdMob.name());



        //Fourth Request => OrderNum = 4
        response = restTemplate
                .withBasicAuth("jb9229.", "adfasjkejo")
                .getForEntity("/api/v1/adts/"+account.getId(), PublishDto.AdTS.class);

        adTS    =   response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody().getPbsType().name()).isEqualTo(PublishType.AdTS.name());
        assertThat(adTS.getTitle()).isEqualTo("휴대용 모니터");

    }

}