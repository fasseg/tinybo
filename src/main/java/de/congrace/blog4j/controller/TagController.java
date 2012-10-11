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

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import de.congrace.blog4j.controller.config.PageConfig;
import de.congrace.blog4j.entities.Article;
import de.congrace.blog4j.entities.Footer;
import de.congrace.blog4j.entities.Tag;
import de.congrace.blog4j.service.api.ArticleService;
import de.congrace.blog4j.service.api.CategoryService;
import de.congrace.blog4j.service.api.FooterService;
import de.congrace.blog4j.service.api.TagService;
import de.congrace.blog4j.tools.ModelUtil;

/**
 * Spring-MVC {@link Controller} for {@link Tag}s
 * @author frank asseg, frank.asseg@congrace.de
 */
@Controller
public class TagController {

    private static final int articlesPerPage = 4;
    @Resource(name = "tagService")
    private TagService tagService;
    @Resource(name = "articleService")
    private ArticleService articleService;
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    @Resource(name = "footerService")
    private FooterService footerService;

    /**
     * Set the {@link CategoryService} to be used
     * @param categoryService the {@link CategoryService}
     */
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
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
     * Get the view name for a {@link Tag}'s view
     * @param model the {@link ModelMap} holding the page data
     * @param id the Id of the {@link Tag} to be viewed
     * @return the view name for the {@link Tag}
     */
    @RequestMapping(value = "/tags/{id}")
    public String getTagView(ModelMap model, @PathVariable long id) {
        return getTagView(model, id, 1);
    }

    /**
     * Get the paged view name for a {@link Tag}'s view
     * @param model the {@link ModelMap} holding the page dat
     * @param id the id of the {@link Tag} to be viewed
     * @param page the number of the page to be viewed e.g. 2
     * @return the view name for the {@link Tag}
     */
    @RequestMapping(value = "/tags/{id}/page/{page}")
    public String getTagView(ModelMap model, @PathVariable long id, @PathVariable int page) {
        Tag activeTag = tagService.getTagById(id);
        int start = (page - 1) * articlesPerPage;
        List<Article> articles = articleService.getArticlesByTagId(id, start, articlesPerPage);
        if (page == 1 && articles.size() == 1) {
            return "redirect:/blog/articles/" + articles.get(0).getId();
        }
        model.addAttribute("articles", articles);
        boolean lastPage = (page * articlesPerPage >= activeTag.getArticleCount());
        PageConfig config = new PageConfig().setPage(page).setLastPage(lastPage);
        ModelUtil.addConfigToModel(model, config);
        model.addAttribute("categories", categoryService.getAllRootCategories());
        List<Tag> tags = tagService.getImportantTags(20);
        Collections.shuffle(tags);
        model.addAttribute("tags", tags);
        model.addAttribute("activeTag", activeTag);
        Footer footer = footerService.getDefaultFooter();
        model.addAttribute("footer", footer);
        return "tag";
    }
}
