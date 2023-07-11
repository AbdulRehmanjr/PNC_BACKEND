package com.pnc.marketplace.configuration.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pnc.marketplace.utils.UserDetailService;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailService userDetails;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * This function is a filter that extracts a JWT token from the request header,
     * validates it, and
     * sets the authentication details in the security context.
     * 
     * @param request     The HttpServletRequest object represents the HTTP request
     *                    made by the client to
     *                    the server. It contains information such as the request
     *                    method, request URI, headers, and
     *                    parameters.
     * @param response    The `response` parameter is an instance of the
     *                    `HttpServletResponse` class,
     *                    which represents the response that will be sent back to
     *                    the client. It is used to manipulate the
     *                    response headers and body, set the status code, and send
     *                    the response back to the client.
     * @param filterChain The `filterChain` parameter is an object that represents
     *                    the chain of filters
     *                    that will be applied to the incoming request. It allows
     *                    the filter to pass the request and
     *                    response objects to the next filter in the chain.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String RequestTokenHeader = request.getHeader("Authorization");

        log.info("Request Type: {}, Request URI: {}", request.getMethod(), request.getRequestURI());

        String email = null;
        String jwttoken = null;

        if (RequestTokenHeader != null && RequestTokenHeader.startsWith("Bearer ")) {
            jwttoken = RequestTokenHeader.substring(7);
            try {
                email = this.jwtUtil.extractUsername(jwttoken);

            } catch (Exception e) {
                log.error("Cannot extract username from token or expired token.");

            }

        } else {
            log.error("Invalid token, not start with bearer string");
        }

        // * validate token
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetail = this.userDetails.loadUserByUsername(email);

            if (this.jwtUtil.validateToken(jwttoken, userDetail)) {

                UsernamePasswordAuthenticationToken uPAT = new UsernamePasswordAuthenticationToken(userDetail, null,
                        userDetail.getAuthorities());
                uPAT.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(uPAT);
            }
        } else {
            log.error("Token validation error");
        }

        filterChain.doFilter(request, response);

    }

}
