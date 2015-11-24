package com.hoh.security.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jeong on 2015-11-24.
 */
public class TokenBasedAuthenticationSuccessHandlerImpl extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String context  =   request.getContextPath();
        String fullURL  =   request.getRequestURI();
        String url      =   fullURL.substring(fullURL.indexOf(context) + context.length());

        request.getRequestDispatcher(url).forward(request, response);
    }
}
