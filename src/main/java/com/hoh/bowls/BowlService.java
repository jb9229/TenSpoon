package com.hoh.bowls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jeong on 2016-02-15.
 */
@Service
public class BowlService {

    @Autowired
    private BowlRepository bowlRepository;

    public boolean addRice(Bowl bowl, int rice){
        boolean result  =   false;

        if(!bowl.isFull())
        {
            bowl.setRiceTol(rice);

            bowlRepository.save(bowl);



            result  =   true;
        }
        return result;
    }
}
