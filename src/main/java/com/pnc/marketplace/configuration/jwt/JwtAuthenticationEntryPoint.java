package com.pnc.marketplace.configuration.jwt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * The `commence` function sends an unauthorized error response to the client.
     * 
     * @param request       The HttpServletRequest object represents the HTTP
     *                      request made by the client to
     *                      the server. It contains information such as the request
     *                      method, headers, parameters, and body.
     * @param response      The HttpServletResponse object represents the response
     *                      that will be sent back to
     *                      the client. It is used to set the response status code
     *                      and send error messages.
     * @param authException The authException parameter is an object that represents
     *                      the authentication
     *                      exception that occurred during the authentication
     *                      process. It contains information about the
     *                      exception, such as the error message and the cause of
     *                      the exception.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");

    }

}
