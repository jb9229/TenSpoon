package com.hoh.accounts;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by test on 2015-12-21.
 */
public class AccountNotAuthEmailException extends AuthenticationException {
    String email;

    public AccountNotAuthEmailException(String email) {
        super(email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
