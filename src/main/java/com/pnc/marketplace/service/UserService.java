package com.pnc.marketplace.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.model.User;

public interface UserService {
    
     /**
      * The function saves a user object and returns the saved user.
      * 
      * @param user The user object that needs to be saved.
      * @return The method `saveUser` is returning an object of type `User`.
      */
     User saveUser(User user,MultipartFile photo);

     /**
      * The function saves an admin user.
      * 
      * @param user The user object that needs to be saved as an admin.
      * @return The method `saveAdmin` is returning an object of type `User`.
      */
     User saveAdmin(User user);

     /**
      * The function getUserById returns a User object based on the given userId.
      * 
      * @param userId An integer representing the unique identifier of a user.
      * @return a User object.
      */
     User getUserById(int userId);

     /**
      * The function getUserByIdEdit returns a User object based on the given userId.
      * 
      * @param userId An integer representing the unique identifier of a user.
      * @return The method is returning a User object.
      */
     User getUserByIdEdit(int userId);

     /**
      * The updateUser function updates the information of a user and returns the updated user object.
      * 
      * @param user The user object that needs to be updated.
      * @return The method is returning an updated User object.
      */
     User updateUser(User user);

     /**
      * The function getAllUsers() returns a list of all users.
      * 
      * @return The method getAllUsers() returns a list of User objects.
      */
     List<User> getAllUsers();
     
     /**
      * The function retrieves a user object based on their email address.
      * 
      * @param email A string representing the email address of the user.
      * @return The method is returning a User object.
      */
     User getUserByEmail(String email);

     /**
      * The deleteUser function is used to delete a user with a specific ID.
      * 
      * @param id The id parameter is an integer that represents the unique identifier of the user to
      * be deleted.
      */
     void deleteUser(int id);

     /**
      * The function updatePassword takes a User object as input and returns the updated User object
      * after the password has been updated.
      * 
      * @param user The user object represents the user whose password needs to be updated.
      * @return The updated User object is being returned.
      */
     User updatePassword(User user);

}