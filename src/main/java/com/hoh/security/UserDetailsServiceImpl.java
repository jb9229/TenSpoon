package com.hoh.security;

import com.hoh.accounts.Account;
import com.hoh.accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by jeong on 2015-11-05.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account     =   accountRepository.findByUsername(username);

        if(account == null){
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(account);
    }
}
