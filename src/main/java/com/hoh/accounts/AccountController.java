package com.hoh.accounts;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

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

    @RequestMapping(value="/accounts", method = GET)
    public ResponseEntity getAccounts(Account account, Pageable pageable){

        Specification<Account> spec     =   Specifications.where(AccountSpecs.emailEqual(account.getEmail()));//spec  =   spec.and()



        Page<Account> page              =      repository.findAll(spec, pageable);


        //TODO limit & email 조건
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

        if(account  ==  null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Account updateAccount     =   service.updateAccount(account, updateDto);

        return new ResponseEntity<>(modelMapper.map(updateAccount, AccountDto.Response.class),
                HttpStatus.OK);
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
