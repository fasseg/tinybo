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
import java.net.URISyntaxException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.entities.Article;
import de.congrace.blog4j.entities.Attachment;
import de.congrace.blog4j.entities.FileAttachment;
import de.congrace.blog4j.entities.URIAttachment;
import de.congrace.blog4j.forms.AttachmentCommand;
import de.congrace.blog4j.service.api.ArticleService;
import de.congrace.blog4j.service.api.AttachmentService;

/**
 * Spring-MVC {@link Controller} for {@link Attachment}s
 * 
 * @author frank asseg, frank.asseg@congrace.de
 */
@Controller
@RequestMapping(value = "/attachments")
public class AttachmentController {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentController.class);
    @Resource(name = "attachmentService")
    private AttachmentService attachmentService;
    @Resource(name = "articleService")
    private ArticleService articleService;

    /**
     * set the {@link AttachmentService} to be used
     * 
     * @param attachmentService the {@link AttachmentService}
     */
    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * Set the {@link ArticleService} to be used
     * 
     * @param articleService the {@link ArticleService}
     */
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * View an {@link Attachment}
     * 
     * @param id the id of the {@link Attachment} to be viewed
     * @return the view name for the {@link Attachment}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String viewAttachment(@PathVariable long id) {
        LOG.debug("view attachment " + id);
        // TODO: double redirection this is bad on db load
        Attachment a = attachmentService.getAttachmentById(id);
        if (a instanceof FileAttachment) {
            return "redirect:file/" + id;
        } else if (a instanceof URIAttachment) {
            return viewUriAttachment((URIAttachment) a);
        }
        return null;
    }

    /**
     * View an {@link URIAttachment}
     * 
     * @param a the {@link URIAttachment} to be viewed
     * @return the view for {@link URIAttachment}
     */
    private String viewUriAttachment(URIAttachment a) {
        return "redirect:" + a.getUri();
    }

    /**
     * View a {@link FileAttachment}
     * 
     * @param id the id of the {@link FileAttachment} to be viewed
     * @param rp the {@link HttpServletResponse} for the user's http request, into which the response will be written
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/file/{id}")
    public void viewFileAttachment(@PathVariable long id, HttpServletResponse rp) throws IOException {
        LOG.debug("viewing file attachment " + id);
        // TODO: double redirection this is bad on db load
        Attachment a = attachmentService.getAttachmentById(id);
        FileAttachment fa = (FileAttachment) a;
        rp.setHeader("Title", fa.getFileName());
        rp.setHeader("Content-Type", fa.getMimeType());
        rp.setHeader("Content-Disposition", "attachment;filename=" + fa.getFileName());
        if (fa.getContent() != null) {
            for (byte b : fa.getContent()) {
                rp.getOutputStream().write(b);
            }
        }
        rp.getOutputStream().flush();
        rp.getOutputStream().close();
    }

    /**
     * Get the edit {@link Attachment} view
     * 
     * @param model the model holding the page data
     * @param id the id of the {@link Attachment} to edit
     * @return the logical view name for editing {@link Attachment}s
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/edit")
    public String getEditAttachmentView(Model model, @PathVariable("id") long id) {
        LOG.debug("showing edit attachment view for attachment " + id);
        // TODO: file/URLattachments
        Attachment a = attachmentService.getAttachmentById(id);
        AttachmentCommand command = new AttachmentCommand();
        command.setId(a.getId());
        command.setTitle(a.getTitle());
        command.setDescription(a.getDescription());
        model.addAttribute("attachmentCommand", command);
        return "admin/edit-attachment";
    }

    /**
     * Edit an {@link Attachment}
     * 
     * @param att the {@link AttachmentCommand} holding the data
     * @param id the id of the Attachment to be updated
     * @return
     */
    @Secured(value = "ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/edit")
    public String editAttachment(@ModelAttribute Attachment att, @PathVariable("id") long id) {
        LOG.debug("modifying attachment " + id);
        Attachment a = attachmentService.getAttachmentById(id);
        a.setTitle(att.getTitle());
        a.setDescription(att.getDescription());
        attachmentService.saveAttachment(a);
        return "redirect:/blog/admin";
    }

    /**
     * Add an {@link Attachment} and bind it to an {@link Article}
     * @param attachmentCommand the {@link AttachmentCommand} holding the data
     * @return the {@link Article}'s edit view name
     * @throws URISyntaxException
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public String addAttachment(@ModelAttribute AttachmentCommand attachmentCommand) throws URISyntaxException {
        LOG.debug("adding new attachment");
        if (attachmentCommand.getType().equals("file")) {
            FileAttachment a = new FileAttachment();
            a.setTitle(attachmentCommand.getTitle());
            a.setContent(attachmentCommand.getFile().getBytes());
            a.setFileName(attachmentCommand.getFile().getOriginalFilename());
            a.setMimeType(attachmentCommand.getMimeType());
            attachmentService.saveAttachment(a);
            Article art = articleService.getArticleById(attachmentCommand.getArticleId());
            art.getAttachments().add(a);
            articleService.saveArticle(art);
        } else if (attachmentCommand.getType().equals("uri")) {
            URIAttachment a = new URIAttachment();
            a.setTitle(attachmentCommand.getTitle());
            a.setUri(attachmentCommand.getUrl());
            attachmentService.saveAttachment(a);
            Article art = articleService.getArticleById(attachmentCommand.getArticleId());
            art.getAttachments().add(a);
            articleService.saveArticle(art);
        }
        return "redirect:/blog/articles/" + attachmentCommand.getArticleId() + "/edit";
    }

}
