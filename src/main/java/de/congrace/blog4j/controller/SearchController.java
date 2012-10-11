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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.controller.config.PageConfig;
import de.congrace.blog4j.entities.Tag;
import de.congrace.blog4j.forms.SearchCommand;
import de.congrace.blog4j.search.SearchResult;
import de.congrace.blog4j.service.api.CategoryService;
import de.congrace.blog4j.service.api.SearchService;
import de.congrace.blog4j.service.api.TagService;
import de.congrace.blog4j.tools.ModelUtil;

/**
 * Spring-MVC {@link Controller} for searches
 * @author frank asseg, frank.asseg@congrace.de
 */
@Controller
@RequestMapping(value = "/search")
public class SearchController {

    @Resource(name = "searchService")
    private SearchService searchService;
    @Resource(name = "tagService")
    private TagService tagService;
    @Resource(name = "categoryService")
    private CategoryService categoryService;

    /**
     * Set the {@link SearchService} to be used
     * @param searchService the {@link SearchService}
     */
    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

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
     * Get the Search view
     * @param model the {@link ModelMap} holding the page data
     * @param searchCommand the {@link SearchCommand} holding the search query
     * @return the search result view name
     */
    @RequestMapping(method = RequestMethod.POST, value = "/search")
    public String getSearchView(ModelMap model, @ModelAttribute SearchCommand searchCommand) {
        ModelUtil.addConfigToModel(model, new PageConfig().setTitle("Search results").setRobots("noindex,nofollow"));
        List<SearchResult> result = searchService.getSearchResult(searchCommand.getQuery());
        List<Tag> tags = tagService.getImportantTags(20);
        Collections.shuffle(tags);
        model.addAttribute("tags", tags);
        model.addAttribute("categories", categoryService.getAllRootCategories());
        model.addAttribute("searchResult", result);
        model.addAttribute("previousSearchCommand", searchCommand);
        model.addAttribute("searchTerm", searchCommand.getQuery());
        model.addAttribute("searchResultCount", result.size());
        return "search";
    }
}
