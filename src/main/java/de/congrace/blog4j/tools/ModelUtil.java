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
package de.congrace.blog4j.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.ui.ModelMap;

import de.congrace.blog4j.controller.config.PageConfig;
import de.congrace.blog4j.forms.SearchCommand;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public abstract class ModelUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ModelUtil.class);
    
    public static void addConfigToModel(ModelMap model, PageConfig config) {
        model.addAttribute("pageHead",config.getPageHead());
        model.addAttribute("pageSubHead", config.getPageSubHead());
        model.addAttribute("basePath", config.getBasePath());
        model.addAttribute("webmasterMail", MailUtil.scrambleEmail(config.getWebmasterMail()));
        model.addAttribute("keywords", config.getKeywords());
        model.addAttribute("pageTitle", config.getTitle());
        model.addAttribute("robots", config.getRobots());
        model.addAttribute("page", config.getPage());
        model.addAttribute("lastPage", config.isLastPage());
        model.addAttribute("contextPath", config.getContextPath());
        model.addAttribute("webmasterName", config.getWebmasterName());
        model.addAttribute("author", config.getAuthor());
        model.addAttribute("theme",config.getTheme());
        model.addAttribute("introduction",config.getIntroduction());
        SearchCommand sc = new SearchCommand();
        sc.setQuery("search");
        model.addAttribute("searchCommand", sc);
    }
}
