package com.hoh.spoons;

import com.hoh.accounts.*;
import com.hoh.bowls.BowlFullException;
import com.hoh.bowls.BowlRepository;
import com.hoh.common.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by test on 2016-01-31.
 */
@RestController
public class SpoonController {

    @Autowired
    private SpoonService spoonService;


    @Autowired
    private AccountService accountService;



    @RequestMapping(value="/spoon/rice/add/{accountId}/{rice}", method = RequestMethod.GET)
    public ResponseEntity addRice(@PathVariable Long accountId, @PathVariable int rice){

        Account account     =   accountService.getAccount(accountId);


        spoonService.addRice(account, rice);


        //TODO RespnseEntity 결정 및 협의
        return new ResponseEntity<>(account, HttpStatus.OK);
    }


    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerAccountNotFoundException(AccountNotFoundException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getId()+"]에 해당하는 계정이 없습니다.");
        errorResponse.setCode("account.not.found.exception");

        return errorResponse;
    }


    @ExceptionHandler(BowlFullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBowlFullException(BowlFullException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setCode("bowl.full.exception");

        return errorResponse;
    }

}
