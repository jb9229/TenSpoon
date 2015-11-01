package com.hoh.accounts;

/**
 * Created by test on 2015-10-31.
 */
public class UserDuplicatedException extends RuntimeException{
    String username;

    public UserDuplicatedException(String username){
        this.username   =   username;
    }

    public String getUsername() {
        return username;
    }
}
