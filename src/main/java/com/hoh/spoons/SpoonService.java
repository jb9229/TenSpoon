package com.hoh.spoons;

import com.hoh.accounts.Account;
import com.hoh.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

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

            Page<Spoon> page                =   getSpoons(spec, new Pageable);


        }else
        {

        }
    }


    public Page<Spoon> getSpoons(Specification<Spoon> spec, Pageable pageable){


        Page<Spoon> page                =   spoonRepository.findAll(spec, pageable);


        return page;
    }

}
