package com.hoh.security;

import com.hoh.security.impl.AuthTokenGeneratorService;
import com.hoh.security.impl.AuthTokenService;
import com.hoh.security.impl.NoOpAuthenticationManager;
import com.hoh.security.impl.TokenBasedAuthenticationSuccessHandlerImpl;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jeong on 2015-11-24.
 */
public class TokenBasedAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String HEADER_SECURITY_TOKEN   =   "X-AuthToken";
    private final String TOKEN_FILTER_APPLIED           =   "TOKEN_FILTER_APPLIED";

    private AuthTokenGeneratorService authTokenGeneratorService;
    private AuthTokenService authTokenService;

    public TokenBasedAuthenticationFilter(String defaultFilterProcessesUrl, AuthTokenGeneratorService authTokenGeneratorService, AuthTokenService authTokenService) {
        super(defaultFilterProcessesUrl);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        super.setAuthenticationManager(new NoOpAuthenticationManager());
        setAuthenticationSuccessHandler(new TokenBasedAuthenticationSuccessHandlerImpl());
        this.authTokenGeneratorService  =   authTokenGeneratorService;
        this.authTokenService           =   authTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        AbstractAuthenticationToken userAuthenticationToken =   null;
        request.setAttribute(TOKEN_FILTER_APPLIED, Boolean.TRUE);

        String token    =   request.getHeader(HEADER_SECURITY_TOKEN);
        userAuthenticationToken =   authenticateByToken(token);

        if(userAuthenticationToken == null){
            throw new AuthenticationServiceException("Bad Token");
        }

        return userAuthenticationToken;
    }

    private AbstractAuthenticationToken authenticateByToken(String token){
        if(null ==  token){
            return null;
        }

        AbstractAuthenticationToken authToken   =   null;

        try{
            String[] tokens =   authTokenGeneratorService.decode(token);

            AuthToken tokenEntry    =   authTokenService.findUserByTokenAndServices(tokens[0], tokens[2]);

            if(null == tokenEntry){
                return null;
            }

            UserContext securityUser    =   new UserContext(tokenEntry.getUser());
        }
    }
}
