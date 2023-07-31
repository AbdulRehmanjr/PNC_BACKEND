package com.pnc.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnc.marketplace.configuration.jwt.JwtUtil;
import com.pnc.marketplace.model.User;
import com.pnc.marketplace.model.jwt.JwtRequest;
import com.pnc.marketplace.model.jwt.JwtResponse;
import com.pnc.marketplace.service.UserService;
import com.pnc.marketplace.utils.UserDetailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/token")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * The function generates a JWT token for a user based on their email and
     * password.
     * 
     * @param request The request parameter is an object of type JwtRequest. It is
     *                annotated with
     *                @RequestBody, which means that it will be deserialized from
     *                the request body JSON.
     * @return The method is returning a ResponseEntity object. The response entity
     *         contains the HTTP
     *         status code and the body of the response. In this case, if the
     *         authentication is successful, it
     *         returns a ResponseEntity with HTTP status code 201 (CREATED) and the
     *         body contains a JwtResponse
     *         object which contains the generated token. If the authentication
     *         fails, it returns a
     *         ResponseEntity with HTTP status code 404 (NOT_FOUND)
     */
    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest request) {

        try {
            authentication(request.getEmail(), request.getPassword());
        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error user not found.");
        }

        UserDetails user = this.userDetailsService.loadUserByUsername(request.getEmail());

        String token = this.jwtUtil.generateToken(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(token));

    }

    /**
     * The authentication function takes a username and password as input, attempts
     * to authenticate the
     * user using the authenticationManager, and logs the result.
     * 
     * @param username The username parameter is a String that represents the
     *                 username entered by the
     *                 user for authentication.
     * @param password The password parameter is a String that represents the user's
     *                 password.
     */
    private void authentication(String username, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            log.info("Authenticated User: {} ", username);
        } catch (DisabledException e) {
            log.error("USER DISABLED  {} ", e.getMessage());
            throw new Exception("USER DISABLED");
        } catch (BadCredentialsException e) {
            log.error("INVALID CREDENTIALS {} ", e.getMessage());
            throw new Exception("INVALID CREDENTIALS");
        }

    }

    /**
     * This function retrieves the current user based on the email provided in the
     * request body.
     * 
     * @param request The parameter "request" is of type JwtRequest, which is a
     *                class that represents a
     *                JSON Web Token (JWT) request. It is annotated
     *                with @RequestBody, which means that the request
     *                body will be automatically mapped to an instance of
     *                JwtRequest.
     * @return The method is returning a User object.
     */
    @PostMapping("/current-user")
    public User getCurrentUser(@RequestBody JwtRequest request) {

        User user = this.userService.getUserByEmail(request.getEmail());

        return user;
    }
}
