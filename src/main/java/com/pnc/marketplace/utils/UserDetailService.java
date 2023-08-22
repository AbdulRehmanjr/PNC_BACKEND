package com.pnc.marketplace.utils;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.database.user.UserRepository;
import com.pnc.marketplace.model.Role;
import com.pnc.marketplace.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailService implements UserDetailsService{
        
 
    @Autowired
    private UserRepository userRepo;

    /**
     * This function loads user details by their email and returns a UserDetails object.
     * 
     * @param email The email parameter is the email address of the user that is being used to load the
     * user details.
     * @return The method is returning an instance of `UserDetails`.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User response = this.userRepo.findByUserEmail(email);

        if (response == null) {
            log.error("No user with {} given email ", email);
            return null;
        }

        return new org.springframework.security.core.userdetails.User(response.getUserEmail(),
                response.getUserPassword(),getAuthorities(response.getRole()));
    }

    /**
     * The function "getAuthorities" returns a list of GrantedAuthority objects based on the provided
     * Role object.
     * 
     * @param role The "role" parameter is an object of type "Role". It is used to determine the
     * authority of the user.
     * @return The method is returning a List of GrantedAuthority objects.
     */
    private List<GrantedAuthority> getAuthorities(Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
       
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        
        return authorities;
    }
}

