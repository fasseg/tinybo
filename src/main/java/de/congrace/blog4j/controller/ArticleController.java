/*
 * Copyright 2010 frank asseg. frank.asseg@congrace.de Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License. under the License.
 */
package de.congrace.blog4j.controller;

import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.controller.config.PageConfig;
import de.congrace.blog4j.entities.Article;
import de.congrace.blog4j.entities.Attachment;
import de.congrace.blog4j.entities.Category;
import de.congrace.blog4j.entities.Footer;
import de.congrace.blog4j.entities.Image;
import de.congrace.blog4j.entities.Tag;
import de.congrace.blog4j.entities.User;
import de.congrace.blog4j.forms.ArticleCommand;
import de.congrace.blog4j.forms.AttachmentCommand;
import de.congrace.blog4j.forms.validator.ArticleValidator;
import de.congrace.blog4j.service.api.ArticleService;
import de.congrace.blog4j.service.api.AttachmentService;
import de.congrace.blog4j.service.api.CategoryService;
import de.congrace.blog4j.service.api.FooterService;
import de.congrace.blog4j.service.api.ImageService;
import de.congrace.blog4j.service.api.TagService;
import de.congrace.blog4j.tools.Blog4jCodeUtil;
import de.congrace.blog4j.tools.ModelUtil;

/**
 * Spring-MVC {@link Controller} for {@link Article}s
 * @author frank asseg, frank.asseg@congrace.de
 */
@Controller
@RequestMapping(value = "/articles")
public class ArticleController {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);
    @Resource(name = "articleService")
    private ArticleService articleService;
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    @Resource(name = "tagService")
    private TagService tagService;
    @Resource(name = "footerService")
    private FooterService footerService;
    @Resource(name = "attachmentService")
    private AttachmentService attachmentService;
    @Resource(name = "imageService")
    private ImageService imageService;

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * Set the {@link FooterService} to be used
     * @param footerService the {@link FooterService} to be used
     */
    public void setFooterService(FooterService footerService) {
        this.footerService = footerService;
    }

    /**
     * Set the {@link TagService} to be used
     * @param tagService the {@link TagService} to be used
     */
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Set the {@link ArticleService} to be used
     * @param articleService the {@link ArticleService} to be used
     */
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Set the {@link CategoryService} to be used
     * @param categoryService the {@link CategoryService} to be used
     */
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get the {@link Article} view name
     * @param id the id of the article to be viewed
     * @param model the {@link ModelMap} to write the attributes into
     * @return the view name
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String viewArticle(@PathVariable("id") long id, ModelMap model) {
        LOG.debug("showing article " + id);
        Article a = articleService.getArticleById(id);
        String robots = (a.isRobotIndex() ? "index" : "noindex") + (a.isRobotFollow() ? ",follow" : ",nofollow");
        PageConfig config = new PageConfig().setTitle(a.getTitle()).setRobots(robots);
        ModelUtil.addConfigToModel(model, config);
        model.addAttribute("articleMsg", Blog4jCodeUtil.translate(a.getMsg()));
        model.addAttribute("article", a);
        model.addAttribute("fileAttachments", attachmentService.getFileAttachmentsByArticleId(id));
        model.addAttribute("uriAttachments", attachmentService.getURIAttachmentsByArticleId(id));
        model.addAttribute("categories", categoryService.getAllRootCategories());
        Footer footer = footerService.getDefaultFooter();
        model.addAttribute("footer", footer);
        List<Tag> tags = tagService.getImportantTags(20);
        Collections.shuffle(tags);
        model.addAttribute("tags", tags);
        return "article";
    }

    /**
     * Update a {@link Article} object through a POST request
     * @param id the {@link Article}'s id to edit
     * @param articleCommand the {@link ArticleCommand} holding the data
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/edit")
    public String editArticle(ModelMap model, @PathVariable long id, @ModelAttribute ArticleCommand articleCommand, BindingResult result) {
        LOG.debug("modifying article " + id);
        // validate
        new ArticleValidator().validate(articleCommand, result);
        if (result.hasErrors()) {
            model.addAttribute("articleCommand", articleCommand);
            return getEditArticleView(id, model);
        }
        Article a = articleService.getArticleById(id);
        a.setEditDate(new Date());
        a.setTitle(StringEscapeUtils.escapeHtml(articleCommand.getTitle()));
        a.setTeaser(StringEscapeUtils.escapeHtml(articleCommand.getTeaser()));
        a.setMsg(StringEscapeUtils.escapeHtml(articleCommand.getMsg()));
        a.getCategories().clear();
        if (articleCommand.getImage() != null && !articleCommand.getImage().isEmpty()) {
            Image img = new Image();
            img.setData(articleCommand.getImage().getBytes());
            img.setTitle(articleCommand.getImage().getOriginalFilename());
            imageService.saveImage(img);
            a.setImage(img);
        }
        if (articleCommand.getImageAlignment() != null) {
            if (articleCommand.getImageAlignment().equals("left")) {
                a.setImageAlignment(Article.ImageAlignment.LEFT);
            } else if (articleCommand.getImageAlignment().equals("right")) {
                a.setImageAlignment(Article.ImageAlignment.RIGHT);
            }
        }
        if (articleCommand.getCategoryIds() != null) {
            for (String catId : articleCommand.getCategoryIds()) {
                a.getCategories().add(categoryService.getCategoryById(Long.parseLong(catId)));
            }
        }
        if (articleCommand.getTags() != null) {
            String[] tagNames = articleCommand.getTags().split(",");
            Set<Tag> tags = new HashSet<Tag>();
            for (String tagName : tagNames) {
                tags.add(tagService.getTagByName(tagName.toLowerCase().trim(), true));
            }
            a.setTags(tags);
        }
        a.setRobotFollow(articleCommand.isRobotFollow());
        a.setRobotIndex(articleCommand.isRobotIndex());
        a.setEnabled(articleCommand.isEnabled());
        articleService.saveArticle(a);
        categoryService.updateArticleCount();
        return "redirect:/blog/admin";
    }

    /**
     * Get the edit view for an {@link Article}
     * @param id the is of the {@link Article} to be edited
     * @param model the {@link ModelMap} to hold the formdata
     * @return the logical view name for editing {@link Article}s
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/edit")
    public String getEditArticleView(@PathVariable("id") long id, ModelMap model) {
        LOG.debug("showing edit article " + id + " view");
        ModelUtil.addConfigToModel(model,PageConfig.getDefaultInstance());
        Article a = articleService.getArticleById(id);
        ArticleCommand c;
        if (model.containsAttribute("articleCommand")){
            c=(ArticleCommand) model.get("articleCommand");
        }else{
            c=new ArticleCommand();
        }
        c.setTitle(StringEscapeUtils.unescapeHtml(a.getTitle()));
        c.setTeaser(StringEscapeUtils.unescapeHtml(a.getTeaser()));
        c.setMsg(StringEscapeUtils.unescapeHtml(a.getMsg()));
        c.setRobotFollow(a.isRobotFollow());
        c.setRobotIndex(a.isRobotIndex());
        c.setEnabled(a.isEnabled());
        c.setId(a.getId());
        if (a.getImage() != null) {
            c.setExistingImageUrl("/tinybo/blog/images/" + a.getImage().getId());
            if (a.getImageAlignment() == null) {
                a.setImageAlignment(Article.ImageAlignment.LEFT);
            }
            c.setImageAlignment(a.getImageAlignment().toString().toLowerCase());
        }
        List<String> categoryIds = new ArrayList<String>();
        for (Category category : a.getCategories()) {
            categoryIds.add(String.valueOf(category.getId()));
        }
        c.setCategoryIds(categoryIds);
        StringBuilder tagString = new StringBuilder();
        for (Tag tag : a.getTags()) {
            tagString.append(", " + tag.getName());
        }
        if (tagString.length() > 2) {
            c.setTags(tagString.substring(2));
        }
        model.addAttribute("articleCommand", c);
        model.addAttribute("newArticle", false);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("attachments", a.getAttachments());
        return "admin/edit-article";
    }

    /**
     * Get the view responsible for adding {@link Article}s
     * @param model the {@link ModelMap} to write the formdata into
     * @return the edit article view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET, value = "/add")
    public String addArticle(ModelMap model) {
        ModelUtil.addConfigToModel(model,PageConfig.getDefaultInstance());
        LOG.debug("showing add article view");
        if (!model.containsAttribute("articleCommand")) {
            model.addAttribute("articleCommand", new ArticleCommand());
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/edit-article";
    }

    /**
     * Add a new {@link Article}
     * @param model the model holding the form data
     * @param articleCommand the new {@link Article}'s data
     * @param result The Spring-MVC {@link BindingResult}
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public String addArticle(ModelMap model, @ModelAttribute ArticleCommand articleCommand, BindingResult result) {
        LOG.debug("adding new article");
        // validate
        new ArticleValidator().validate(articleCommand, result);
        if (result.hasErrors()) {
            model.addAttribute("articleCommand", articleCommand);
            return addArticle(model);
        }
        // create and save the new article
        Article.Builder builder = new Article.Builder(StringEscapeUtils.escapeHtml(articleCommand.getTitle()), StringEscapeUtils.escapeHtml(articleCommand.getMsg()), StringEscapeUtils.escapeHtml(articleCommand.getTeaser()));
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (articleCommand.getTags() != null) {
            String[] tagNames = articleCommand.getTags().split(",");
            Set<Tag> tags = new HashSet<Tag>();
            for (String tagName : tagNames) {
                tags.add(tagService.getTagByName(tagName.toLowerCase().trim(), true));
            }
            builder.withTags(tags);
        }
        builder.withCategories(categoryService.getCategoriesById(articleCommand.getCategoryIds())).withAuthor(u).withCreationDate(new Date()).withEditDate(new Date()).withEnabled(articleCommand.isEnabled()).withRobotFollow(articleCommand.isRobotFollow()).withRobotIndex(articleCommand.isRobotIndex());
        // set the image if there is on in the command object
        if (articleCommand.getImage() != null && !articleCommand.getImage().isEmpty()) {
            Image img = new Image();
            img.setData(articleCommand.getImage().getBytes());
            img.setTitle(articleCommand.getImage().getOriginalFilename());
            imageService.saveImage(img);
            builder.withImage(img);
        }
        // set the image alignment if theres one in the command object
        if (articleCommand.getImageAlignment() != null) {
            if (articleCommand.getImageAlignment().equals("left")) {
                builder.withImageAlignment(Article.ImageAlignment.LEFT);
            } else if (articleCommand.getImageAlignment().equals("right")) {
                builder.withImageAlignment(Article.ImageAlignment.RIGHT);
            }
        }
        Article a = builder.build();
        articleService.saveArticle(a);
        categoryService.updateArticleCount();
        return "redirect:/blog/admin";
    }

    /**
     * Delete an {@link Article}
     * @param id the ids of the {@link Article} to be deleted
     * @return the admin view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/delete")
    public String deleteArticle(@PathVariable long id) {
        LOG.debug("deleteing article " + id);
        articleService.deleteArticle(id);
        return "redirect:/blog/admin";
    }

    /**
     * get the delete {@link Article} view name
     * @param model the model holding the page data
     * @param id the id of the {@link Article} which is to be deleted
     * @return the confirmation view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/delete")
    public String getDeleteArticleView(ModelMap model, @PathVariable long id) {
        LOG.debug("showing delete article " + id + "view");
        model.addAttribute("message", "Really delete this article?");
        return "admin/confirmation";
    }

    /**
     * Get the add {@link Attachment} view to add {@link Attachment}s
     * @param model the model holding the page data
     * @param id the id of the {@link Attachment} to add
     * @return the edit attachment view
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/attachments/add")
    public String getAddAttachmentView(ModelMap model, @PathVariable long id) {
        LOG.debug("showing add attchment view for article " + id);
        AttachmentCommand command = new AttachmentCommand();
        command.setArticleId(id);
        model.addAttribute("attachmentCommand", command);
        return "admin/edit-attachment";
    }

    /**
     * Get the remove {@link Attachment} view to remove {@link Attachment}s from
     * {@link Article}s
     * @param model the model holding the page data
     * @param id the id of the {@link Article} from which the {@link Attachment}
     *            is to be removed
     * @param attachmentId the id of the {@link Attachment} to be removed
     * @return the confirmation view
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/attachments/{attachmentId}/delete")
    public String getRemoveAttachmentView(ModelMap model, @PathVariable long id, @PathVariable long attachmentId) {
        LOG.debug("showing delete attachment view for article " + id + " and attachment " + attachmentId);
        model.addAttribute("message", "Remove this attachment?");
        return "admin/confirmation";
    }

    /**
     * Remove an {@link Attachment} from an {@link Article}
     * @param id the id of the {@link Article}
     * @param attachmentId the id of the {@link Attachment} to be removed
     * @return the {@link Article}'s edit view name
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/attachments/{attachmentId}/delete")
    public String removeAttachment(@PathVariable long id, @PathVariable long attachmentId) {
        LOG.debug("removing attachment " + attachmentId + " from article " + id);
        articleService.removeAttachment(id, attachmentId);
        return "redirect:/blog/articles/" + id + "/edit";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/image/delete")
    public String getRemoveImageView(ModelMap model, @PathVariable long id) {
        model.addAttribute("message", "Delete this image?");
        return "admin/confirmation";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/image/delete")
    public String removeImage(@PathVariable long id) {
        Article a = articleService.getArticleById(id);
        Image i = a.getImage();
        a.setImage(null);
        articleService.saveArticle(a);
        imageService.deleteImage(i);
        return "redirect:/blog/articles/" + id + "/edit";
    }
}
