package com.hoh.security;

import com.hoh.accounts.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by jeong on 2015-11-24.
 */
public class UserContext implements UserDetails {
    String username;
    String password;
    Long id;

    private Account user;

    public UserContext(Account user){
        this.user   =   user;

        if(user != null){
            this.username   =   user.getEmail();
            this.password   =   user.getPassword();
            this.id         =   user.getId();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
