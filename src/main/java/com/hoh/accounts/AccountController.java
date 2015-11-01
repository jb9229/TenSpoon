package com.hoh.accounts;

import com.hoh.common.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by test on 2015-10-18.
 */
@RestController
public class AccountController {
    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    @RequestMapping(value="/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create, BindingResult result){

        if(result.hasErrors()){
            ErrorResponse errorResponse =   new ErrorResponse();

            errorResponse.setMessage(result.toString());
            errorResponse.setCode("bed.request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Account newAccount  =   service.createAccount(create);

        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

    @RequestMapping(value="/accounts", method = RequestMethod.GET)
    public ResponseEntity getAccounts(Pageable pageable){
        Page<Account> page  =      repository.findAll(pageable);

        //TODO stream() vs parallelStream()
        List<AccountDto.Response> content = page.getContent().parallelStream()
                .map(account -> modelMapper.map(account, AccountDto.Response.class))
                .collect(Collectors.toList());

        PageImpl<AccountDto.Response> result    =   new PageImpl<>(content, pageable, page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity handlerUserDuplicatedException(UserDuplicatedException e){
        ErrorResponse   errorResponse   =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getUsername() + "] 중복된 username 입니다.");
        errorResponse.setCode("duplicated.username.exception");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
