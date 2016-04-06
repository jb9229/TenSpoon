package com.hoh.accounts;

import com.hoh.Application;
import com.hoh.TSWebMvcConfiguration;
import com.hoh.common.Email;
import com.hoh.common.EmailSender;
import com.hoh.common.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by test on 2015-10-18.
 */
@RestController
@RequestMapping("/api/v1/")
public class AccountController {
    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private EmailSender emailSender;



    @RequestMapping(value="/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create, BindingResult result){

        if(result.hasErrors())
        {
            ErrorResponse errorResponse =   new ErrorResponse();

            errorResponse.setMessage(result.toString());
            errorResponse.setCode("bed.request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        double authMailKey  =   Math.random();
        create.setAuthMailKey(authMailKey);

        Account newAccount  =   service.createAccount(create);



        Email authenMail    =   new Email();
        authenMail.setSubject("가입을 축하 드립니다, 이메일 인증을 해 주세요.");
        authenMail.setReceiver(newAccount.getEmail());
        authenMail.setContent("<p> 십시일반 가입을 축하 드립니다, 하기 링크로 이메일 인증을 완료 해 주세요.</p> <a href='http://localhost:8080/accounts/auth/" + newAccount.getEmail() + "/" + authMailKey + "'>이메일 인증하러 가기</a>");

        emailSender.sendMail(authenMail);


        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/auth/accounts/{email}/{key}", method = RequestMethod.GET)
    public ResponseEntity authAccountMail(@PathVariable String email, @PathVariable Double key){
        Account account         =   repository.findByEmail(email);

        account.setAuthMailkey(null);

        Account updateAccount   =   repository.save(account);

        return new ResponseEntity<>(modelMapper.map(updateAccount, AccountDto.Response.class),
                HttpStatus.OK);

    }

    @RequestMapping(value="/accounts/bowl/{bowlId}", method = GET)
    public ResponseEntity getBowlAccounts(@PathVariable Long bowlId, Pageable pageable){


        Page<Account> page              =      repository.findByBowl(bowlId, pageable);


        List<AccountDto.Response> content = page.getContent().parallelStream()
                .map(newAccount -> modelMapper.map(newAccount, AccountDto.Response.class))
                .collect(Collectors.toList());


        PageImpl<AccountDto.Response> result    =   new PageImpl<>(content, pageable, page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value="/accounts", method = GET)
    public ResponseEntity getAccounts(Account account, Pageable pageable){

        Specification<Account> spec     =   Specifications.where(AccountSpecs.emailEqual(account.getEmail()));//spec  =   spec.and()



        Page<Account> page              =      repository.findAll(spec, pageable);


        List<AccountDto.Response> content = page.getContent().parallelStream()
                .map(newAccount -> modelMapper.map(newAccount, AccountDto.Response.class))
                .collect(Collectors.toList());


        PageImpl<AccountDto.Response> result    =   new PageImpl<>(content, pageable, page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value="/accounts/{id}", method = GET )
    public ResponseEntity getAccount(@PathVariable Long id) {
        Account account                     =   service.getAccount(id);

        AccountDto.Response response        =   modelMapper.map(account, AccountDto.Response.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value="/accounts/{id}", method = PUT)
    public ResponseEntity updateAccount(@PathVariable Long id,
                                        @RequestBody @Valid AccountDto.Update updateDto,
                                        BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account account     =   repository.findOne(id);

        if(account  ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Account updateAccount     =   service.updateAccount(account, updateDto);

        return new ResponseEntity<>(modelMapper.map(updateAccount, AccountDto.Response.class),
                HttpStatus.OK);
    }


    @RequestMapping(value="/accounts/image/upload", method = RequestMethod.POST )
    public String uploadProfilePhoto(@RequestParam("name") String name, @RequestParam("file")MultipartFile file){


        File newFile    =   null;
        if(!file.isEmpty()){
            try{
                BufferedOutputStream stream     =   new BufferedOutputStream(
                        new FileOutputStream(newFile = new File(Application.FILESERVER_IMG_PROFILE+"/"+ name)));

                FileCopyUtils.copy(file.getInputStream(), stream);

                stream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        return TSWebMvcConfiguration.FILESYSTEM_PATH + "img/profile/" + newFile.getName();
    }


    @RequestMapping(value="/accounts/{id}", method = DELETE)
    public ResponseEntity deleteAccount(@PathVariable Long id){
        service.deleteAccount(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler(UserDuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerUserDuplicatedException(UserDuplicatedException e){
        ErrorResponse   errorResponse   =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getUsername() + "] 중복된 username 입니다.");
        errorResponse.setCode("duplicated.username.exception");

        return errorResponse;
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerAccountNotFoundException(AccountNotFoundException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getId()+"]에 해당하는 계정이 없습니다.");
        errorResponse.setCode("account.not.found.exception");

        return errorResponse;
    }


}
