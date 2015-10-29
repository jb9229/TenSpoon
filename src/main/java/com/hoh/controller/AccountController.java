package com.hoh.controller;

import com.hoh.common.ErrorResponse;
import com.hoh.domain.Account;
import com.hoh.dto.AccountDto;
import com.hoh.repository.AccountRepository;
import com.hoh.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by test on 2015-10-18.
 */
@RestController
public class AccountController {
    @Autowired
    private AccountService service;

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


}
