package com.hoh.security;

import com.hoh.accounts.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jeong on 2015-11-05.
 */
public class UserDetailsImpl extends User {

    public UserDetailsImpl(Account account) {
        super(account.getEmail(), account.getPassword(), authorities(account));
    }

    private static Collection<? extends GrantedAuthority> authorities(Account account) {
        List<GrantedAuthority> authorities  =   new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(account.isAdmin()){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }
}
