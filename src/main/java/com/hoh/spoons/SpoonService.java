package com.hoh.spoons;

import com.hoh.accounts.Account;
import com.hoh.accounts.AccountDto;
import com.hoh.accounts.AccountService;
import com.hoh.bowls.BowlFullException;
import com.hoh.bowls.BowlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by test on 2016-01-31.
 */
@Service
@Transactional
@Slf4j
public class SpoonService {
    @Autowired
    private AccountService accountService;

    @Autowired
    private SpoonRepository spoonRepository;

    @Autowired
    private BowlService bowlService;


    public void addRice(Account account, int rice){

        boolean existFullBowl               =   false;

        AccountDto.RiceUpdate riceUpdate    =   new AccountDto.RiceUpdate();



        int donationRice                    =   accountService.addRice(account, riceUpdate, rice);



        if(donationRice > 0)
        {
            Specification<Spoon> spec       =   Specifications.where(SpoonSpecs.accountIdEqual(account.getId()));

            Page<Spoon> page                =   getSpoons(spec, new PageRequest(0, 10));

            List<Spoon> spoons              =   page.getContent();



            for(Spoon s:   spoons)
            {
                int percent =   s.getRicePercent();

                if(percent > 0)
                {

                    int addRice = rice * (s.getRicePercent() / 100);



                    boolean result  =   bowlService.addRice(s.getBowl(), addRice);

                    if(result)
                    {
                        SpoonDto.Update update  =   new SpoonDto.Update();
                        update.setRiceTol(addRice);
                        update.setRicePercent(s.getRicePercent());

                        updateSpoon(s, update);
                    }else
                    {
                        riceUpdate.setRiceTol(account.getRiceTol()+addRice);

                        existFullBowl       =   true;
                    }
                }
            }
        }



        accountService.updateRiceAccount(account, riceUpdate);


        if(existFullBowl) throw new BowlFullException("기부처 목표 금액에 달성 했습니다");
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
