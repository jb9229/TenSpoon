package com.hoh.service;

import com.hoh.domain.Account;
import com.hoh.dto.AccountDto;
import com.hoh.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by test on 2015-10-18.
 */
@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto.Create dto){

        Account account =   modelMapper.map(dto, Account.class);

        return repository.save(account);
    }
}
