package com.hoh.accounts;

/**
 * Created by test on 2015-11-02.
 */
public class AccountNotFoundException extends RuntimeException {
    Long id;

    public AccountNotFoundException(Long id){
        this.id =   id;
    }

    public Long getId() {
        return id;
    }
}
