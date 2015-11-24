package com.hoh.security.impl;

import com.hoh.security.AuthToken;

/**
 * Created by jeong on 2015-11-24.
 */
public interface AuthTokenService {
    AuthToken create(AuthToken authToken);

    AuthToken update(AuthToken authToken);

    AuthToken findUserByTokenAndServices(final String token, final String series);

    void deleteByTokenAndSeries(final String token, final String series);

    void deleteExpiredToken();
}
