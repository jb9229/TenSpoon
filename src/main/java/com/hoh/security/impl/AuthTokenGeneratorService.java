package com.hoh.security.impl;

import org.springframework.security.core.Authentication;

/**
 * Created by jeong on 2015-11-24.
 */
public interface AuthTokenGeneratorService {
    String generateToken(Authentication authentication);

    public String[] decode(String token);
}
