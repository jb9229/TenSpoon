package com.hoh.accounts;

public class AccountNotFoundException extends RuntimeException {
    Long id;
    String email;

    public AccountNotFoundException(Long id)
    {
        this.id = id;
    }

    public AccountNotFoundException(String email)
    {
        this.email = email;
    }

    public Long getId()
    {
        return this.id;
    }

    public String getEmail()
    {
        return this.email;
    }
}
