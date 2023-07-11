package com.pnc.marketplace.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * The function findByUserEmail takes an email as input and returns a User
     * object that matches the
     * email.
     * 
     * @param email The email parameter is a string that represents the email
     *              address of a user.
     * @return The method findByUserEmail returns a User object.
     */
    User findByUserEmail(String email);
}
