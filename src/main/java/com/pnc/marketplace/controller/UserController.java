package com.pnc.marketplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnc.marketplace.model.User;
import com.pnc.marketplace.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * The function `registerUser` is a POST request handler that registers a new
     * user by saving their
     * information and profile picture.
     * 
     * @param profilePicture The "profilePicture" parameter is of type
     *                       MultipartFile, which is used to
     *                       handle file uploads in Spring MVC. It represents the
     *                       uploaded file data, such as the file name,
     *                       content type, and actual file content. In this case, it
     *                       is used to receive the user's profile
     *                       picture file.
     * @param user           The "user" parameter is a JSON string that represents
     *                       the user object. It is
     *                       deserialized using the ObjectMapper class from the
     *                       Jackson library to convert the JSON string
     *                       into a User object.
     * @return The method is returning a ResponseEntity object. The response entity
     *         can have different
     *         HTTP status codes and a body. In this case, if the user is
     *         successfully registered, the method
     *         returns a ResponseEntity with a status code of 201 (CREATED) and the
     *         registered user as the body.
     *         If there is an error in creating the user, the method returns a
     *         ResponseEntity with a status code
     *         of 501 (NOT
     */
    @PostMapping("/create")
    public ResponseEntity<?> resgisterUser(@RequestParam("file") MultipartFile profilePicture, String user) {

        User n_user = new User();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            n_user = objectMapper.readValue(user, User.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }

        User found = this.userService.getUserByEmail(n_user.getUserEmail());

        if (found != null) {
            log.error("User already exists with given Email: {}", n_user.getUserEmail());
            return ResponseEntity.badRequest().body("User already exists with given email");
        }

        n_user = this.userService.saveUser(n_user, profilePicture);

        if (n_user != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(n_user);
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Error in creation user");
    }

    /**
     * The function `registerAdmin` is a POST request handler that takes in a
     * profile picture file and
     * user information, creates a new User object from the user information, sets
     * the profile picture
     * for the user, saves the user as an admin, and returns a response indicating
     * the success or
     * failure of the user creation.
     * 
     * @param profilePicture The "profilePicture" parameter is of type
     *                       MultipartFile, which is used to
     *                       handle file uploads in Spring MVC. It represents the
     *                       uploaded file for the user's profile
     *                       picture.
     * @param user           The "user" parameter is a JSON string that represents
     *                       the user object. It contains
     *                       information such as the user's name, email, password,
     *                       etc.
     * @return The method is returning a ResponseEntity<String>.
     */
    @PostMapping("/register-admin")
    public ResponseEntity<String> resgisterAdmin(@RequestParam("file") MultipartFile profilePicture, String user) {

        User n_user = new User();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            n_user = objectMapper.readValue(user, User.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        n_user = this.userService.saveAdmin(n_user);
        if (n_user != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Created.");
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Error in creation user");

    }

    /**
     * This function retrieves a user by their ID and returns a response entity with
     * the user
     * information if found, or a not found status if not found.
     * 
     * @param userId The userId is a path variable that is passed in the URL. It
     *               represents the unique
     *               identifier of the user that we want to retrieve.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") int userId) {

        User result = this.userService.getUserById(userId);

        if (result == null) {
            log.error("Users not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        log.info("User Found.");

        return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

    /**
     * This function retrieves a user by their ID for editing purposes and returns a
     * response entity
     * with the user information.
     * 
     * @param userId The userId is a path variable that represents the unique
     *               identifier of a user. It
     *               is used to retrieve a specific user from the database for
     *               editing purposes.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/edit/{userId}")
    public ResponseEntity<?> getUserByIdEdit(@PathVariable("userId") int userId) {

        User result = this.userService.getUserByIdEdit(userId);

        if (result == null) {
            log.error("Users not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        log.info("User Found.");

        return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

    // @Deprecated
    // @PostMapping("/forgot-password/{email}")
    // ResponseEntity<?> restPassword(@PathVariable String email,@RequestBody Otp
    // otp) {
    // log.info("Resting the password");

    // User user = this.userService.getUserByEmail(email);

    // if(user == null){
    // return ResponseEntity.status(401).body(null);
    // }
    // this.userService.restPassword(otp.getOtp(),email);
    // return ResponseEntity.status(201).body(user);
    // }

    /**
     * The above function is a deprecated method in Java that changes a user's
     * password and returns a
     * response entity.
     * 
     * @param email    The email parameter is a path variable that represents the
     *                 email address of the
     *                 user whose password needs to be changed.
     * @param password The "password" parameter is of type User and is used to pass
     *                 the new password
     *                 for the user.
     * @return The method is returning a ResponseEntity object.
     */
    @Deprecated
    @PostMapping("/change-password/{email}")
    ResponseEntity<?> change(@PathVariable String email, @RequestBody User password) {
        log.info("Resting the password");

        User user = this.userService.getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        user.setUserPassword(password.getUserPassword());
        user = this.userService.updatePassword(user);

        return ResponseEntity.status(201).body(user);
    }

    /**
     * The function returns a ResponseEntity containing a list of all users, with a
     * status code of 201
     * if successful or 401 if the list is null.
     * 
     * @return The method is returning a ResponseEntity object. The response entity
     *         can contain a
     *         status code and a body. In this case, if the list of users is not
     *         null, the status code is set
     *         to 201 (created) and the body contains the list of users. If the list
     *         of users is null, the
     *         status code is set to 401 (unauthorized) and the body is set to null
     */
    @GetMapping("/all")
    ResponseEntity<?> Allusers() {
        log.info("Geting all users");

        List<User> users = this.userService.getAllUsers();

        if (users == null) {
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.status(201).body(users);
    }

    /**
     * This function deletes a user with the specified ID and returns a response
     * entity with a success
     * message.
     * 
     * @param id The "id" parameter is the unique identifier of the user that needs
     *           to be deleted. It
     *           is passed as a path variable in the URL.
     * @return The deleteUser method is returning a ResponseEntity<String> object.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {
        this.userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

}
