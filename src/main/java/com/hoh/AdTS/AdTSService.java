package com.hoh.AdTS;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by test on 2018-03-23.
 */
@Service
public class AdTSService {

    @Autowired
    private AdTSRepository repository;
    //Util
    @Autowired private ModelMapper modelMapper;



    public PublishDto.Publish getPublish(int orderNum) {
        //Get AdTS
        AdTS adTS   =  getAdTS(orderNum);


        //AdMob Publish
        if(adTS == null){return new PublishDto.AdMob();}


        //Return
        return modelMapper.map(adTS, PublishDto.AdTS.class);
    }

    public AdTS getAdTS(int orderNum){
        return repository.findByOrderNum(orderNum);
    }

    public AdTS createAdTS(AdTSDto.Create adTSDto) {
        AdTS adTS   =   modelMapper.map(adTSDto, AdTS.class);

        return repository.save(adTS);
    }
}
