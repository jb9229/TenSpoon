package com.hoh.bowls;

import com.hoh.spoons.Spoon;
import com.hoh.spoons.SpoonRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jeong on 2016-02-15.
 */
@Service
@Transactional
@Slf4j
public class BowlService {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private BowlRepository repository;

    @Autowired
    private SpoonRepository spoonRepository;



    public boolean addRice(Bowl bowl, int rice){
        boolean result  =   false;

        if(!bowl.isFull())
        {
            int currRiceTot =   bowl.getRiceTol();
            bowl.setRiceTol(currRiceTot+rice);

            repository.save(bowl);



            result  =   true;
        }
        return result;
    }

    public Bowl createBowl(BowlDto.Create create) {
        Bowl bowl   =   modelMapper.map(create, Bowl.class);
        
        
        return repository.save(bowl);
    }

    public Bowl getBowl(Long id) {
        Bowl bowl   =   repository.findOne(id);

        if(bowl == null)
        {
            throw new BowlNotFoundException(id);
        }

        return bowl;
    }

    public Bowl updateBowl(Bowl bowl, BowlDto.Update update) {
        bowl.setTheme(update.getTheme());
        bowl.setTitle(update.getTitle());
        bowl.setContents(update.getContents());
        bowl.setOrg(update.getOrg());
        bowl.setSummary(update.getSummary());


        return repository.save(bowl);
    }

    public void deleteBowl(Long id) {
        repository.delete(getBowl(id));
    }

    public void deleteBowl(Bowl bowl) {
        repository.delete(bowl);
    }


    public void closeBowl(Long id) {

        Bowl deleteBowl =   getBowl(id);

        spoonRepository.deleteByBowl(deleteBowl);

        deleteBowl(deleteBowl);
    }
}
