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


    public Account updateAccount(Account account, AccountDto.Update updateDto) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setUsername(updateDto.getUsername());

        return repository.save(account);
    }

    public void deleteAccount(Long id){
        repository.delete(getAccount(id));
    }

}
