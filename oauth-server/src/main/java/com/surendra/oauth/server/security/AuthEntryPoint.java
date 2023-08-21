package com.surendra.oauth.server.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.surendra.oauth.server.api.response.error.ErrorResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    Logger log = LoggerFactory.getLogger(AuthEntryPoint.class);
    Gson gson = new Gson();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ErrorResponseModel errorResponseModel = new ErrorResponseModel("Authentication token expired. Please refresh the page and try again.", HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(gson.toJson(errorResponseModel));
    }
}
