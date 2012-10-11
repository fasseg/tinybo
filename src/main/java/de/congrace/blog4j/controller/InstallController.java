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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.entities.Footer;
import de.congrace.blog4j.entities.Role;
import de.congrace.blog4j.entities.User;
import de.congrace.blog4j.service.api.FooterService;
import de.congrace.blog4j.service.api.RoleService;
import de.congrace.blog4j.service.api.UserService;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Controller
@RequestMapping("/install")
public class InstallController {

    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "roleService")
    private RoleService roleService;
    @Resource(name = "footerService")
    private FooterService footerService;

    public void setFooterService(FooterService footerService) {
        this.footerService = footerService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getInstallView() {
        if (roleService.getAdminRole() == null && roleService.getDefaultRole() == null && userService.getUserCount() == 0) {
            return "admin/install";
        }
        return "redirect:/blog/categories/default";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String install() {
        if (roleService.getAdminRole() == null) {
            createAdminRole();
        }
        if (roleService.getDefaultRole() == null) {
            createDefaultRole();
        }
        if (userService.getUserCount() == 0) {
            createDefaultUser();
        }
        if (footerService.getDefaultFooter() == null) {
            createDefaultFooter();
        }
        return "redirect:/blog/admin";
    }

    private void createDefaultFooter() {
        Footer f = new Footer();
        f.setName("Default footer");
        f.setDefaultFooter(true);
        footerService.saveFooter(f);
    }

    private void createAdminRole() {
        Role r = new Role.Builder(Role.Authority.ROLE_ADMIN.toString()).build();
        roleService.saveRole(r);
    }

    private void createDefaultRole() {
        Role r = new Role.Builder(Role.Authority.ROLE_USER.toString()).build();
        roleService.saveRole(r);
    }

    private void createDefaultUser() {
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleService.getAdminRole());
        roles.add(roleService.getDefaultRole());
        User u = new User.Builder("admin", roles).build();
        userService.addUser(u, "admin");
    }
}
