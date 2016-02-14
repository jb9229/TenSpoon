package com.hoh.spoons;

import com.hoh.accounts.Account;
import com.hoh.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by test on 2016-01-31.
 */
@Service
public class SpoonService {
    @Autowired
    private AccountService accountService;

    @Autowired
    private SpoonRepository spoonRepository;


    public void addRice(Account account, int rice){

        int donationRice    =   accountService.addRice(account, rice);


        if(donationRice > 0)
        {
            Specification<Spoon> spec       =   Specifications.where(SpoonSpecs.accountIdEqual(account.getId()));

            Page<Spoon> page                =   null; //TODO Pageable 구현체 적용 기술 숙지 getSpoons(spec, Pageable);

            List<Spoon> spoons              =   page.getContent();



            for(Spoon s:   spoons)
            {
                int percent =   s.getRicePercent();

                if(percent > 0)
                {
                    SpoonDto.Update update  =   new SpoonDto.Update();

                    int addRice = rice * s.getRicePercent() * 100;    //TODO 쌀 퍼센트 소수점 문제 해결
                    update.setRiceTol(addRice);
                    update.setRicePercent(s.getRicePercent());


                    updateSpoon(s, update);

                    //TODO bowlRepository.get(bolwld)       bowlService.addRice(bowl)
                }
            }
        }


        //TODO Account Save
//        accountService.updateAccount(account, new AccountDto)
    }


    public Page<Spoon> getSpoons(Specification<Spoon> spec, Pageable pageable){


        Page<Spoon> page                =   spoonRepository.findAll(spec, pageable);


        return page;
    }


    public Spoon updateSpoon(Spoon spoon, SpoonDto.Update update){
        spoon.setRiceTol(update.getRiceTol());
        spoon.setRicePercent(update.getRicePercent());

        return spoonRepository.save(spoon);
    }

}
