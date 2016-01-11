package com.hoh.security;

import com.hoh.accounts.Account;
import com.hoh.accounts.AccountNotAuthEmailException;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account     =   accountRepository.findByEmail(email);

        if(account == null){
            throw new UsernameNotFoundException(email);
        }

        if(account.getAuthMailkey() != null){
            throw new AccountNotAuthEmailException("["+email+"] 해당 이메일이 인증 되지 않았습니다.");
        }

        return new UserDetailsImpl(account);
    }
}
