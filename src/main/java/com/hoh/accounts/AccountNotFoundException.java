package com.hoh.accounts;

/**
 * Created by test on 2015-11-02.
 */
public class AccountNotFoundException extends RuntimeException {
    Long id;
    String email;

    public AccountNotFoundException(Long id){
        this.id     =   id;
    }

    public AccountNotFoundException(String email){
        this.email  =   email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
