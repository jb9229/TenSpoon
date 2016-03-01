package com.hoh.bowls;

import com.hoh.common.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jeong on 2016-02-24.
 */
@RestController
@RequestMapping("/api/v1/")
public class BowlController {

    @Autowired
    BowlService service;

    @Autowired
    BowlRepository repository;

    @Autowired
    private ModelMapper modelMapper;



    @RequestMapping(value="/bowls", method = RequestMethod.POST)
    public ResponseEntity createBowl(@RequestBody @Valid BowlDto.Create create, BindingResult result){
        if(result.hasErrors())
        {
            ErrorResponse errorResponse =   new ErrorResponse();

            errorResponse.setMessage(result.toString());
            errorResponse.setCode("bed.request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Bowl newBowl    =   service.createBowl(create);


        return new ResponseEntity(modelMapper.map(newBowl, BowlDto.Response.class), HttpStatus.CREATED);
    }


    @RequestMapping(value="/bowls/{id}", method = RequestMethod.POST)
    public ResponseEntity updateBowl(@PathVariable Long id, @RequestBody @Valid BowlDto.Update update, BindingResult result){

        if(result.hasErrors())
        {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }



        Bowl bowl           =   repository.findOne(id);

        if(bowl == null)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }



        Bowl updatedBowl    =   service.updateBowl(bowl, update);


        return new ResponseEntity(modelMapper.map(updatedBowl, BowlDto.Response.class), HttpStatus.OK);
    }


    @RequestMapping(value="/bowls/{id}", method = RequestMethod.DELETE)
    public ResponseEntity closeBowl(@PathVariable Long id){

        service.closeBowl(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(BowlNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBowlNotFoundException(BowlNotFoundException e){
        ErrorResponse errorResponse     =   new ErrorResponse();

        errorResponse.setMessage("["+ e.getId()+"]에 해당하는 기부처가 없습니다.");
        errorResponse.setCode("bowl.not.found.exception");

        return errorResponse;
    }
}
