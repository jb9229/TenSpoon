package com.hoh.accounts;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by test on 2015-10-18.
 */
@Service
@Transactional
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account createAccount(AccountDto.Create dto){
        Account account =   modelMapper.map(dto, Account.class);

        String username =   dto.getUsername();
        if(repository.findByUsername(username)  !=  null){
            throw new UserDuplicatedException(username);
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        Date date   =   new Date();
        account.setJoined(date);
        account.setUpdated(date);

        return repository.save(account);
    }

    public Account getAccount(Long id) {
        Account account     =   repository.findOne(id);

        if(account == null)
        {
            throw new AccountNotFoundException(id);
        }

        return account;
    }

    public Account getAccount(String email) {
        Account account     =   repository.findByEmail(email);

        if(account == null)
        {
            throw new AccountNotFoundException(email);
        }

        return account;
    }


    public Account updateAccount(Account account, AccountDto.Update updateDto) {
        account.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        account.setUsername(updateDto.getUsername());

        return repository.save(account);
    }

    public Account updateRiceAccount(Account account, AccountDto.RiceUpdate updateDto) {
        account.setRiceTol(updateDto.getRiceTol());
        account.setRiceMonth(updateDto.getRiceMonth());
        account.setRiceYear(updateDto.getRiceYear());
        account.setRiceTemp(updateDto.getRiceTemp());

        return repository.save(account);
    }

    public void deleteAccount(Long id){
        repository.delete(getAccount(id));
    }

    public int addRice(Account account, AccountDto.RiceUpdate riceUpdate,  int rice) {

        int donationRice;

        riceUpdate.setRiceTol(account.getRiceTol() + rice);
        riceUpdate.setRiceMonth(account.getRiceMonth() + rice);
        riceUpdate.setRiceYear(account.getRiceYear() + rice);

        riceUpdate.setRiceTemp(account.getRiceTemp()+rice);

        if(riceUpdate.getRiceTemp() > 100)
        {
            double donationRiceOri  =   account.getRiceTemp()/100;

            donationRice            =   (int)donationRiceOri;

            donationRice            =   donationRice * 100;



            riceUpdate.setRiceTemp(account.getRiceTemp()   -  donationRice );
        }else
        {
            donationRice    =   0;
        }



        return donationRice;
    }

    public String calRandomPW(int length) {

        int index   =   0;

        char[] charSet      =   new char[]{
                '0','1','2','3','4','5','6','7','8','9'
                ,'A','B','C','D','E','F','G','H','I','J','K','L','M'
                ,'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
                ,'a','b','c','d','e','f','g','h','i','j','k','l','m'
                ,'n','o','p','q','r','s','t','u','v','w','x','y','z'};

         StringBuffer sb     =   new StringBuffer();


         for(int i=0; i<length; i++)
         {
             index   =   (int) (charSet.length * Math.random());
             sb.append(charSet[index]);
         }

         return sb.toString();

    }
}
