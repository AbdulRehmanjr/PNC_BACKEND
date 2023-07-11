package com.pnc.marketplace.service;

import java.util.List;

import com.pnc.marketplace.model.Role;

public interface RoleService {

    /**
     * The function creates a new role and returns it.
     * 
     * @param role The role parameter is an object of type Role. It represents the
     *             role that you want to
     *             create.
     * @return The method `createRole` returns an object of type `Role`.
     */
    Role createRole(Role role);

    /**
     * The function getByRoleName takes a roleName as input and returns a Role
     * object.
     * 
     * @param roleName A string representing the name of the role.
     * @return The method `getByRoleName` returns an object of type `Role`.
     */
    Role getByRoleName(String roleName);

    /**
     * The function getAllRoles() returns a list of all roles.
     * 
     * @return The method getAllRoles() returns a list of Role objects.
     */
    List<Role> getAllRoles();

}
