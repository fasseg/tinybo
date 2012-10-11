/*
 *  Copyright 2010 frank asseg.
 *  frank.asseg@congrace.de
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package de.congrace.blog4j.controller;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.entities.Role;
import de.congrace.blog4j.entities.User;
import de.congrace.blog4j.forms.UserCommand;
import de.congrace.blog4j.service.api.RoleService;
import de.congrace.blog4j.service.api.UserService;

/**
 * Spring-MVC {@link Controller} for handling {@link User}s
 * @author frank asseg, frank.asseg@congrace.de
 */
@Controller
public class UserController {
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "roleService")
    private RoleService roleService;

    /**
     * Set the {@link RoleService} to be used
     * @param roleService the {@link RoleService}
     */
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Set the {@link UserService} to be used
     * @param userService the {@link UserService}
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get the view name for adding new {@link User}s
     * @param model the {@link ModelMap} holding the page data
     * @return the view name for adding {@link User}s
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/users/add", method = RequestMethod.GET)
    public String getAddUserView(ModelMap model) {
        model.addAttribute("userCommand", new UserCommand());
        model.addAttribute("newUser", true);
        return "admin/edit-user";
    }

    /**
     * Add a new {@link User}
     * @param command the {@link UserCommand} holding the {@link User}'s data
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute UserCommand command) {
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleService.getDefaultRole());
        if (command.isAdmin()) {
            roles.add(roleService.getAdminRole());
        }
        User u = new User.Builder(command.getUsername(), roles).withEmail(command.getEmail()).withEnabled(command.isEnabled()).build();
        userService.addUser(u, command.getPassword());
        return "redirect:/blog/admin";
    }

    /**
     * Get the edit view name for editing {@link User}s
     * @param model the {@link ModelMap} holding the page data
     * @param id the id of the {@link User} to be edited
     * @return the view name for editing the {@link User}
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public String getEditUserView(ModelMap model, @PathVariable long id) {
        User u = userService.getUserById(id);
        UserCommand c = new UserCommand();
        c.setUsername(u.getUsername());
        c.setEnabled(u.isEnabled());
        c.setEmail(u.getEmail());
        c.setPassword(u.getPassword());
        if (u.getRoles().contains(roleService.getAdminRole())) {
            c.setAdmin(true);
        }
        model.addAttribute("userCommand", c);
        return "admin/edit-user";
    }

    /**
     * Update a {@link User} Entity
     * @param userCommand the {@link UserCommand} to hold the {@link User}'s dta
     * @param id the id of the User to be edited
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    public String editUser(@ModelAttribute UserCommand userCommand, @PathVariable long id) {
        User u = userService.getUserById(id);
        Role adminRole = roleService.getAdminRole();
        u.setEmail(userCommand.getEmail());
        u.setEnabled(userCommand.isEnabled());
        u.setPassword(userCommand.getPassword());
        if (userCommand.isAdmin() && !u.getRoles().contains(adminRole)) {
            u.getRoles().add(adminRole);
        }
        if (!userCommand.isAdmin() && u.getRoles().contains(adminRole)) {
            u.getRoles().remove(adminRole);
        }
        userService.saveUser(u);
        return "redirect:/blog/admin";
    }

    /**
     * Delete a {@link User}
     * @param id the id of the {@link User} to be deleted
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/users/{id}/delete", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return "redirect:/blog/admin";
    }

    /**
     * Get the deletion view for {@link User}
     * @param model the {@link ModelMap} holding the page data
     * @param id the id of the {@link User} to be deleted
     * @return the confirmation view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/users/{id}/delete")
    public String getDeleteUserView(ModelMap model, @PathVariable long id) {
        model.addAttribute("message", "Really delete this user?");
        return "admin/confirmation";
    }
}
