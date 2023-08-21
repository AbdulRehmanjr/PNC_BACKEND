package com.pnc.marketplace.service.implementation.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.pnc.marketplace.database.RoleRepository;
import com.pnc.marketplace.database.UserRepository;
import com.pnc.marketplace.model.Role;
import com.pnc.marketplace.model.User;
import com.pnc.marketplace.service.firebase.FireBaseService;
import com.pnc.marketplace.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    final String DEFAULT_USER = "USER";
    final String ADMIN = "ADMIN";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private FireBaseService fbService;

    /**
     * This function saves a new user in the database with a default role and
     * encoded password.
     * 
     * @param user The user object that needs to be saved in the database.
     * @return The method is returning the saved User object.
     */
    @Override
    public User saveUser(User user,MultipartFile photo) {

        Role role = this.roleRepo.findByRoleName(DEFAULT_USER);

        if (role == null) {
            log.error("No role found with given name {}", DEFAULT_USER);
            return null;
        }

        user.setRole(role);
        user.setUserPassword(encoder.encode(user.getUserPassword()));

        try {
            user.setPhotoUri(this.fbService.saveFile(user.getFirstName().trim()+"-image", photo.getInputStream(), photo.getContentType()));
        } catch (IOException e) {
            log.error("Can't save Image Error {}",e.getCause());
            user.setPhotoUri("assets/placeholder.png");
        }
        
        User response = this.userRepo.save(user);

        if(response == null)
            return null;
            
        return response;
    }

    /**
     * This function saves a new admin user in the database, encrypts the password,
     * assigns the admin
     * role, and returns the saved user.
     * 
     * @param user The user object contains the details of the admin user that needs
     *             to be saved in the
     *             database.
     * @return The method is returning a User object.
     */
    @Override
    public User saveAdmin(User user) {
        log.info("inserting new admin in database");

        Role role = this.roleRepo.findByRoleName(ADMIN);

        if (role == null) {
            log.info("No role found with given name {}", ADMIN);
            return null;
        }

        user.setRole(role);
        user.setUserPassword(encoder.encode(user.getUserPassword()));
        User saved = null;

        try {
            saved = this.userRepo.save(user);
            return saved;
        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }

    }

    /**
     * The function retrieves a user from the user repository based on the provided
     * user ID.
     * 
     * @param userId The parameter `userId` is an integer representing the unique
     *               identifier of the
     *               user that we want to fetch.
     * @return The method is returning a User object.
     */
    @Override
    public User getUserById(int userId) {

        log.info("fetching user with id {}", userId);
        try {
            User user = this.userRepo.findById(userId).get();
            return user;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * The function retrieves a user from the database based on their ID and returns
     * it.
     * 
     * @param userId The userId parameter is an integer that represents the unique
     *               identifier of the
     *               user that we want to fetch from the database.
     * @return The method is returning a User object.
     */
    @Override
    public User getUserByIdEdit(int userId) {

        log.info("fetching user with id {}", userId);

        User user = null;

        try {
            user = this.userRepo.findById(userId).get();
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
        }

        return user;
    }

    /**
     * The deleteUser function deletes a user from the database based on their id.
     * 
     * @param id The id parameter represents the unique identifier of the user that
     *           needs to be deleted
     *           from the system.
     */
    @Override
    public void deleteUser(int id) {
        log.info("DELETE with id :{}", id);
        this.userRepo.deleteById(id);
    }

    /**
     * The function getAllUsers() retrieves all users from the user repository and
     * returns them as a
     * list.
     * 
     * @return The getAllUsers() method is returning a List of User objects.
     */
    @Override
    public List<User> getAllUsers() {
        log.info("Getting all users");
        return this.userRepo.findAll();
    }

    /**
     * This function retrieves a user from the user repository based on their email
     * address.
     * 
     * @param email The parameter "email" is a String that represents the email
     *              address of the user.
     * @return The method is returning a User object.
     */
    @Override
    @Deprecated
    public User getUserByEmail(String email) {

        User user = null;

        try {
            user = this.userRepo.findByUserEmail(email);
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
        }

        return user;
    }

    /**
     * The function updates a user in the database and returns the updated user.
     * 
     * @param user The user object that needs to be updated.
     * @return The method is returning an updated User object.
     */
    @Override
    public User updateUser(User user) {
        log.info("UPDATE : user");

        return this.userRepo.save(user);
    }

    /**
     * The function updates the password of a user by encoding it and saving it in
     * the database.
     * 
     * @param user The "user" parameter is an object of the User class. It
     *             represents the user whose
     *             password needs to be updated.
     * @return The updated User object is being returned.
     */
    @Override
    public User updatePassword(User user) {
        log.info("savng new password");

        user.setUserPassword(encoder.encode(user.getUserPassword()));
        return this.userRepo.save(user);
    }

}
