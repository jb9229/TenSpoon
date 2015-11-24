package com.hoh.security.impl;

import org.springframework.security.core.Authentication;

/**
 * Created by jeong on 2015-11-24.
 */
public class AuthTokenGeneratorServiceImpl implements AuthTokenGeneratorService {
    @Override
    public String generateToken(Authentication authentication) {
        return null;
    }

    @Override
    public String[] decode(String token) {
        return new String[0];
    }
}
