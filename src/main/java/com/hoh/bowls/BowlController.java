package com.hoh.bowls;

import com.hoh.Application;
import com.hoh.TSWebMvcConfiguration;
import com.hoh.accounts.Account;
import com.hoh.common.ErrorResponse;
import com.hoh.spoons.Spoon;
import com.hoh.spoons.SpoonService;
import org.imgscalr.Scalr;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.imgscalr.Scalr.resize;

/**
 * Created by jeong on 2016-02-24.
 */
@RestController
@RequestMapping("/api/v1/")
public class BowlController {

    @Autowired
    BowlService service;


    @Autowired
    SpoonService spoonService;


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
    public ResponseEntity createBowl(@RequestBody @Valid BowlDto.Create create, BindingResult result) throws IOException {
        if(result.hasErrors())
        {
            ErrorResponse errorResponse =   new ErrorResponse();

            errorResponse.setMessage(result.toString());
            errorResponse.setCode("bed.request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }



        //TODO Thumbnail
        String source   =   create.getContents();
        Document doc    =   Jsoup.parse(source);
        Elements elements   =   doc.select("img");
        String url          =   checkElements(elements);

        if(url != null)
        {
            String imageName    =   "";
            if(url.startsWith(TSWebMvcConfiguration.FILESYSTEM_PATH))
            {
                imageName               =   url.substring(url.lastIndexOf("/") + 1);
                File file               =   new File(Application.FILESERVER_IMG +"/"+ imageName);
                BufferedImage img       =   ImageIO.read(file);
                BufferedImage thumbnail =   createThumbnail(img);
                String ext  =   url.substring(url.lastIndexOf(".") + 1);


                File thumbnailoutput    =   new File(Application.FILESERVER_IMG_THUMBNAILS + imageName);
                ImageIO.write(thumbnail, ext, thumbnailoutput);
            }

            create.setImgthumbnail(TSWebMvcConfiguration.FILESYSTEM_THUMBNAILS_PATH+imageName);
        }

        Bowl newBowl    =   service.createBowl(create);


        return new ResponseEntity(modelMapper.map(newBowl, BowlDto.Response.class), HttpStatus.CREATED);
    }


    @RequestMapping(value="/bowls", method = RequestMethod.GET)
    public ResponseEntity getBowls(Account account, Bowl bowl, Pageable pageable){
        Specification<Bowl> spec    =   Specifications.where(BowlSpecs.bowlTypeEqual(bowl.getTheme()));

        Page<Bowl> page     =   repository.findAll(spec, pageable);

        List<BowlDto.Response> content  =   page.getContent().parallelStream()
                .map(newBowl -> modelMapper.map(newBowl, BowlDto.Response.class))
                .collect(Collectors.toList());



        Page<Spoon> spoonPage               =   spoonService.getAccountSpoons(account.getId());

        Map<Long, BowlDto.Response> rspMap  =   new HashMap();

        for(BowlDto.Response rsp: content)
        {
            rspMap.put(rsp.getId(), rsp);
        }


        for(Spoon sp: spoonPage.getContent())
        {
            Long bowlId             =   sp.getBowl().getId();

            BowlDto.Response rsp    =   rspMap.get(bowlId);

            rsp.setExistSpoon(true);
        }



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


        if(!file.isEmpty()){
            try{
                BufferedOutputStream stream     =   new BufferedOutputStream(
                        new FileOutputStream(new File(Application.FILESERVER_IMG+"/"+ name)));

                FileCopyUtils.copy(file.getInputStream(), stream);

                stream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return TSWebMvcConfiguration.FILESYSTEM_PATH+"img/"+name;
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



    private BufferedImage createThumbnail(BufferedImage img) {
        img     =   resize(img, Scalr.Method.SPEED, 150, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);

        return Scalr.pad(img, 4);
    }

    private String checkElements(Elements elements){
        if(!elements.isEmpty())
        {
            Elements elem   =   elements.get(0).getElementsByAttribute("src");
            String url      =   elem.toString();

            int pos     =   url.indexOf("src=\"") + 5;
            url         =   url.substring(pos, url.indexOf("\"", pos));

            return url;
        }

        return null;
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
