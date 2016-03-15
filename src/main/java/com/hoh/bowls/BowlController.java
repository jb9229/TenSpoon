package com.hoh.bowls;

import com.hoh.Application;
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



    @RequestMapping(value="/bowls/{id}", method = RequestMethod.DELETE)
    public ResponseEntity closeBowl(@PathVariable Long id){

        service.closeBowl(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


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


    @RequestMapping(value="/bowls", method = RequestMethod.GET)
    public ResponseEntity getBowls(Bowl bowl, Pageable pageable){
        Specification<Bowl> spec    =   Specifications.where(BowlSpecs.bowlTypeEqual(bowl.getTheme()));

        Page<Bowl> page     =   repository.findAll(spec, pageable);

        List<BowlDto.Response> content  =   page.getContent().parallelStream()
                .map(newBowl -> modelMapper.map(newBowl, BowlDto.Response.class))
                .collect(Collectors.toList());

        PageImpl<BowlDto.Response> result   =   new PageImpl<>(content, pageable, page.getTotalElements());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value="/bowls/{id}", method = RequestMethod.GET)
    public ResponseEntity getBowl(@PathVariable Long id) {
        Bowl bowl       =   service.getBowl(id);

        BowlDto.Response response   =   modelMapper.map(bowl, BowlDto.Response.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value="/bowls/image/upload", method = RequestMethod.POST )
    public String uploadImage(@RequestParam("name") String name, @RequestParam("file")MultipartFile file){

        String imageUrl =   "";
        if(!file.isEmpty()){
            try{
                File imageFile;
                BufferedOutputStream stream     =   new BufferedOutputStream(
                        new FileOutputStream(imageFile = new File(Application.ROOT+"/"+ name)));
                imageUrl    =   imageFile.getAbsolutePath();

                FileCopyUtils.copy(file.getInputStream(), stream);

                stream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return imageUrl;
    }


    @RequestMapping(value="/bowls/{id}", method = RequestMethod.PUT)
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


    @ExceptionHandler(BowlNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBowlNotFoundException(BowlNotFoundException e){
        ErrorResponse errorResponse     =   new ErrorResponse();

        errorResponse.setMessage("["+ e.getId()+"]에 해당하는 기부처가 없습니다.");
        errorResponse.setCode("bowl.not.found.exception");

        return errorResponse;
    }
}
