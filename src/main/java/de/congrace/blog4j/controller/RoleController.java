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

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.entities.Role;
import de.congrace.blog4j.forms.RoleCommand;
import de.congrace.blog4j.service.api.RoleService;

/**
 * Spring-MVC {@link Controller} for {@link Role}s
 * @author frank asseg, frank.asseg@congrace.de
 */
@Controller
public class RoleController {
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
     * Get the view name for adding new {@link Role}s
     * @param model the {@link ModelMap} holding the page data
     * @return the edit {@link Role} view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/roles/add", method = RequestMethod.GET)
    public String getNewRoleView(ModelMap model) {
        model.addAttribute("roleCommand", new RoleCommand());
        return "admin/edit-role";
    }

    /**
     * Add a new {@link Role}
     * @param roleCommand the {@link RoleCommand} holding the {@link Role}'s data
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/roles/add", method = RequestMethod.POST)
    public String addNewRoleView(@ModelAttribute RoleCommand roleCommand) {
        Role r = new Role();
        r.setAuthority(roleCommand.getAuthority());
        roleService.saveRole(r);
        return "redirect:/blog/admin";
    }
}
