package com.hoh.security.impl;


import com.hoh.security.AuthToken;
import com.hoh.security.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jeong on 2015-11-24.
 */
@Transactional(readOnly = true)
@Service
public class AuthTokenServiceImpl implements AuthTokenService{

    @Autowired
    private AuthTokenRepository authTokenRepository;


    @Transactional
    @Override
    public AuthToken create(AuthToken authToken) {
        return authTokenRepository.saveAndFlush(authToken);
    }

    @Transactional
    @Override
    public AuthToken update(AuthToken authToken) {
        return authTokenRepository.save(authToken);
    }

    @Override
    public AuthToken findUserByTokenAndServices(String token, String series) {
        return authTokenRepository.findUserByTokenAndSeries(token, series);
    }

    @Transactional
    @Override
    public void deleteByTokenAndSeries(String token, String series) {
        authTokenRepository.deleteByTokenAndSeries(token, series);
    }

    @Transactional
    @Override
    public void deleteExpiredToken() {
        authTokenRepository.deleteTimeoutToken();
    }
}
