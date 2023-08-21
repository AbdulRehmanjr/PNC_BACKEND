package com.pnc.marketplace.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnc.marketplace.model.Role;
import com.pnc.marketplace.service.user.RoleService;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * This function handles a POST request to create a new role and returns the
     * created role in the
     * response body.
     * 
     * @param role The role parameter is an object of the Role class, which is being
     *             passed in the
     *             request body.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/create")
    ResponseEntity<?> saveRole(@RequestBody Role role) {

        Role response = this.roleService.createRole(role);

        if (response == null) {

            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(response);

    }

    /**
     * This function is a GET request handler that retrieves all roles and returns a
     * response entity
     * with the roles if they exist, or a 404 status code if they don't.
     * 
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/all")
    ResponseEntity<?> getRoles() {

        List<Role> response = this.roleService.getAllRoles();

        if (response == null) {

            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(response);

    }
}
