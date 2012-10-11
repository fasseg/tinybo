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

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.controller.config.PageConfig;
import de.congrace.blog4j.service.api.ArticleService;
import de.congrace.blog4j.service.api.CategoryService;
import de.congrace.blog4j.service.api.FooterService;
import de.congrace.blog4j.service.api.RoleService;
import de.congrace.blog4j.service.api.SearchService;
import de.congrace.blog4j.service.api.UserService;
import de.congrace.blog4j.tools.ModelUtil;

/**
 * Spring MVC {@link Controller} for administration pages of tinybo
 * @author frank asseg, frank.asseg@congrace.de
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "roleService")
    private RoleService roleService;
    @Resource(name = "articleService")
    private ArticleService articleService;
    @Resource(name = "searchService")
    private SearchService searchService;
    @Resource(name = "footerService")
    private FooterService footerService;

    /**
     * Set the {@link FooterService} to be used
     * 
     * @param footerService the {@link FooterService} to be used
     */
    public void setFooterService(FooterService footerService) {
        this.footerService = footerService;
    }

    /**
     * Set the {@link SearchService} to be used
     * 
     * @param searchService the {@link SearchService} to be used
     */
    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Set the {@link ArticleService} to be used
     * 
     * @return the {@link ArticleService} to be used by this controller
     */
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Set the {@link RoleService} to be used
     * 
     * @param roleService the {@link RoleService} to be used
     */
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Set the {@link CategoryService} to be used
     * 
     * @param categoryService the {@link CategoryService} to be used
     */
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get the {@link UserService} to be used
     * 
     * @param userService the {@link UserService} to be used
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get the admin view name
     * 
     * @param model the {@link ModelMap} to be used
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET)
    public String getAdminView(ModelMap model) {
        ModelUtil.addConfigToModel(model,PageConfig.getDefaultInstance());
        model.addAttribute("pageTitle", "blog4j administration panel");
        model.addAttribute("categories", categoryService.getAllRootCategories());
        model.addAttribute("articles", articleService.getAllArticles());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("footers", footerService.getAllFooters());
        return "admin/admin";
    }

    /**
     * Get the reindex view name for tinybo
     * 
     * @return the reindex view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST, value = "/reindex")
    public String reindex() {
        LOG.debug("reindexing all objects");
        searchService.indexAll();
        return "redirect:/blog/admin";
    }
    
    @Secured("ROLE_ADMIN")
    @RequestMapping(method=RequestMethod.GET, value="/settings")
    public String getEditSettingsView(ModelMap map){
        map.addAttribute("pageConfig",PageConfig.getDefaultInstance());
        return "admin/edit-settings";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method=RequestMethod.POST, value="/settings")
    public String editSettings(@ModelAttribute PageConfig pageConfig){
        try{
            pageConfig.save();
        }catch(IOException e){
            LOG.error("Error while saving properties: ",e);
            return "error";
        }
        return "redirect:/blog/admin";
    }
    
}
