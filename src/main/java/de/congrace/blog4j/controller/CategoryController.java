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
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.controller.config.PageConfig;
import de.congrace.blog4j.entities.Article;
import de.congrace.blog4j.entities.Category;
import de.congrace.blog4j.entities.Footer;
import de.congrace.blog4j.entities.Tag;
import de.congrace.blog4j.forms.CategoryCommand;
import de.congrace.blog4j.forms.validator.CategoryValidator;
import de.congrace.blog4j.service.api.ArticleService;
import de.congrace.blog4j.service.api.CategoryService;
import de.congrace.blog4j.service.api.FooterService;
import de.congrace.blog4j.service.api.TagService;
import de.congrace.blog4j.tools.ModelUtil;
import de.congrace.tinybo.error.TinyboException;

/**
 * Spring-MVC {@link Controller} for {@link Category}s
 * @author frank asseg, frank.asseg@congrace.de
 */
@Controller
public class CategoryController {

    private static final int articlesPerPage = 4;

    @Resource(name = "categoryService")
    private CategoryService categoryService;
    @Resource(name = "articleService")
    private ArticleService articleService;
    @Resource(name = "tagService")
    private TagService tagService;
    @Resource(name = "footerService")
    private FooterService footerService;

    /**
     * Set the {@link FooterService} to be used
     * @param footerService the {@link FooterService}
     */
    public void setFooterService(FooterService footerService) {
        this.footerService = footerService;
    }

    /**
     * Set the {@link TagService} to be used
     * @param tagService the {@link TagService}
     */
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Set the {@link ArticleService} to be used
     * @param articleService the {@link ArticleService}
     */
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Set the {@link CategoryService} to be used
     * @param categoryService the {@link CategoryService}
     */
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get the add {@link Category} view name
     * @param model the model holding the page data
     * @return the edit {@link Category} view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/categories/add", method = RequestMethod.GET)
    public String addCategory(ModelMap model) {
        ModelUtil.addConfigToModel(model,PageConfig.getDefaultInstance());
        if (!model.containsAttribute("categoryCommand")){
            model.addAttribute("categoryCommand", new CategoryCommand());
        }
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return "admin/edit-category";
    }

    /**
     * Add a new {@link Category}
     * @param name the name of the {@link Category} to be added
     * @return the admin view name
     * @throws IOException
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(value = "/categories/add", method = RequestMethod.POST)
    public String addCategory(ModelMap model,HttpServletResponse resp, @ModelAttribute CategoryCommand categoryCommand,BindingResult result) throws IOException {
        new CategoryValidator().validate(categoryCommand,result);
        if (result.hasErrors()){
            model.addAttribute("categoryCommand",categoryCommand);
            return addCategory(model);
        }
        Category.Builder builder = new Category.Builder(categoryCommand.getName());
        if (categoryCommand.getParentCategoryId() != null && categoryCommand.getParentCategoryId() != -1) {
            builder.withParentCategory(categoryService.getCategoryById(categoryCommand.getParentCategoryId()));
        }
        Category cat = builder.build();
        cat.setOrder(categoryService.getNextOrderValue(cat.getParentCategory()));
        try {
            categoryService.saveCategory(cat);
        } catch (TinyboException e) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
            resp.flushBuffer();
            return null;
        }
        return "redirect:/blog/admin";
    }

    /**
     * Get the default {@link Category} view name
     * @param model the model holding the page data
     * @return the default {@link Category} view name
     */
    @RequestMapping(method = RequestMethod.GET, value = "/categories/default")
    public String getDefaultCategoryView(ModelMap model) {
        Category cat = categoryService.getDefaultCategory();
        return getCategoryView(cat.getId(), 1, model);
    }

    /**
     * Get the {@link Category} view name
     * @param id the id of the {@link Category} to be viewed
     * @param model the model holding the page data
     * @return the logical view name
     */
    @RequestMapping(method = RequestMethod.GET, value = "/categories/{id}")
    public String getCategoryView(@PathVariable long id, ModelMap model) {
        return getCategoryView(id, 1, model);
    }

    /**
     * Get a paged {@link Category} view name
     * @param id the id of the {@link Category} to be viewed
     * @param page the number of the page to be viewed e.g. 3
     * @param model the model holding the page data
     * @return the logical view name
     */
    @RequestMapping(method = RequestMethod.GET, value = "/categories/{id}/page/{page}")
    public String getCategoryView(@PathVariable long id, @PathVariable int page, ModelMap model) {
        int start = (page - 1) * articlesPerPage;
        List<Article> articles = articleService.getArticlesByCategoryId(id, start, articlesPerPage);
        if (page == 1 && articles.size() == 1) {
            return "redirect:/blog/articles/" + articles.get(0).getId();
        }
        model.addAttribute("articles", articles);
        Category activeCat = categoryService.getCategoryById(id);
        boolean lastPage = (page * articlesPerPage >= activeCat.getArticleCount());
        PageConfig config = new PageConfig().setPage(page).setLastPage(lastPage);
        ModelUtil.addConfigToModel(model, config);
        model.addAttribute("categories", categoryService.getAllRootCategories());
        model.addAttribute("activeCategory", activeCat);
        List<Tag> tags = tagService.getImportantTags(20);
        Collections.shuffle(tags);
        model.addAttribute("tags", tags);
        Footer footer;
        if (activeCat.getFooter() == null) {
            footer = footerService.getDefaultFooter();
        } else {
            footer = activeCat.getFooter();
        }
        model.addAttribute("footer", footer);
        return "category";
    }

    /**
     * Delete a {@link Category}
     * @param id the id of the {@link Category} to be deleted
     * @return the admin view name
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.DELETE, value = "/categories/{id}")
    public String deleteCategory(HttpServletResponse resp, @PathVariable long id) throws IOException {
        Category cat = categoryService.getCategoryById(id);
        try {
            categoryService.deleteCategory(cat);
        } catch (TinyboException e) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
            resp.flushBuffer();
            return null;
        }
        return "redirect:/blog/admin";
    }

    /**
     * Move a {@link Category} relativ to the other {@link Category}s up
     * @param id the id of the {@link Category} to be moved up
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.PUT, value = "/categories/{id}/moveUp")
    public String moveCategoryUp(HttpServletResponse resp, @PathVariable long id) throws IOException {
        try {
            categoryService.moveCategoryUp(id);
        } catch (TinyboException e) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
            resp.flushBuffer();
            return null;
        }
        return "redirect:/blog/admin";
    }

    /**
     * Move a {@link Category} relativ to the other {@link Category}s down
     * @param id the id of the {@link Category} to be moved down
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.PUT, value = "/categories/{id}/moveDown")
    public String moveCategoryDown(HttpServletResponse resp, @PathVariable long id) throws IOException {
        try {
            categoryService.moveCategoryDown(id);
        } catch (TinyboException e) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
            resp.flushBuffer();
            return null;
        }
        return "redirect:/blog/admin";
    }

    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET, value = "/categories/{id}/edit")
    public String getEditCategoryView(ModelMap model, @PathVariable long id) {
        Category c = categoryService.getCategoryById(id);
        CategoryCommand cmd;
        ModelUtil.addConfigToModel(model,PageConfig.getDefaultInstance());
        if (model.containsAttribute("categoryCommand")){
            cmd=(CategoryCommand) model.get("categoryCommand");
        }else {
            cmd= new CategoryCommand();
        }
        cmd.setId(c.getId());
        cmd.setName(c.getName());
        if (c.getParentCategory() != null) {
            cmd.setParentCategoryId(c.getParentCategory().getId());
        }
        model.addAttribute("categoryCommand", cmd);
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return "admin/edit-category";
    }

    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST, value = "/categories/{id}/edit")
    public String editCategory(HttpServletResponse resp, ModelMap model, @PathVariable long id, @ModelAttribute CategoryCommand categoryCommand,BindingResult result) throws IOException {
        new CategoryValidator().validate(categoryCommand, result);
        if (result.hasErrors()){
            model.addAttribute("categoryCommand",categoryCommand);
            return getEditCategoryView(model, id);
        }
        Category c = categoryService.getCategoryById(id);
        Category parent = c.getParentCategory();
        c.setName(categoryCommand.getName());
        c.setParentCategory(categoryService.getCategoryById(categoryCommand.getParentCategoryId()));
        try {
            categoryService.saveCategory(c);
            if (parent != null) {
                categoryService.updateChildCategories(parent);
            }
        } catch (TinyboException e) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
            resp.flushBuffer();
            return null;
        }
        return "redirect:/blog/admin";
    }
}
