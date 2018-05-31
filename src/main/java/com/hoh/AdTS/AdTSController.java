package com.hoh.AdTS;

import com.hoh.accounts.AccountDto;
import com.hoh.accounts.AccountNotFoundException;
import com.hoh.accounts.AccountService;
import com.hoh.common.ErrorResponse;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by test on 2018-03-13.
 */
@RestController
@RequestMapping("/api/v1/")
public class AdTSController {
    @Autowired private AdTSService service;
    @Autowired private AccountService accountService;
    //Util
    @Autowired private ModelMapper modelMapper;



    @RequestMapping(value="adts/{accountID}", method = RequestMethod.GET)
    public ResponseEntity getAdTS(@PathVariable Long accountID){

        //Get AdTS Order Number
        Integer currOrderNum    =   accountService.getOrderNumAdTS(accountID);

        if(currOrderNum == null){throw new AccountNotFoundException(accountID);}  //TODO Exception 처리(잘못된 accountID 이다), client에선?(약간 번거럽고, 이름짖기도 어렵고, )



        //Get Publish
        PublishDto.Publish publish   =   service.getPublish(currOrderNum);



        //Update Account Next OrderNum
        accountService.updateNextOrderNumAdTS(accountID, currOrderNum);
        //TODO 실패에 대한 Log를 남기고, 알림 서비스까지


        //Return
        return new ResponseEntity<>(modelMapper.map(publish, publish.getClass()), HttpStatus.FOUND);
    }


    //Exception Handler Method
    @ExceptionHandler(AccountNotFoundException.class)@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerUserDuplicatedException(AccountNotFoundException e){
        ErrorResponse   errorResponse   =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getId() + "] 찾을 수 없는 사용자 입니다.");
        errorResponse.setCode("account.not.found.exception");

        return errorResponse;
    }
}
