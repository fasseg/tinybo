package de.congrace.blog4j.controller;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.entities.Article;
import de.congrace.blog4j.entities.Footer;
import de.congrace.blog4j.forms.FooterCommand;
import de.congrace.blog4j.forms.IdCommand;
import de.congrace.blog4j.service.api.ArticleService;
import de.congrace.blog4j.service.api.FooterService;

/**
 * Spring-MCV {@link Controller} for {@link Footer}s
 * @author Frank Asseg, frank.asseg@congrace.de
 */
@Controller
@RequestMapping("/footers")
public class FooterController {

    @Resource(name = "footerService")
    private FooterService footerService;

    @Resource(name = "articleService")
    private ArticleService articleService;

    /**
     * Set the {@link ArticleService} to be used
     * @param articleService the {@link ArticleService}
     */
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Set the {@link FooterService} to be used
     * @param footerService the {@link FooterService}
     */
    public void setFooterService(FooterService footerService) {
        this.footerService = footerService;
    }

    /**
     * Get the add {@link Footer} view name
     * @param model the {@link ModelMap} holding the pagedata
     * @return the edit {@link Footer} view name
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAddFooterView(ModelMap model) {
        FooterCommand cmd = new FooterCommand();
        model.addAttribute("footerCommand", cmd);
        model.addAttribute("availableArticles", articleService.getAllArticles());
        model.addAttribute("newFooter", true);
        return "admin/edit-footer";
    }

    /**
     * Get the edit {@link Footer} view name
     * @param model the {@link ModelMap} holding the page data
     * @param id the id of the {@link Footer} to be edited
     * @return the edit view name
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String getEditFooterView(ModelMap model, @PathVariable long id) {
        Footer f = footerService.getFooterById(id);
        FooterCommand cmd = new FooterCommand();
        cmd.setId(f.getId());
        cmd.setName(f.getName());
        cmd.setArticleIds(new ArrayList<Long>());
        cmd.setDefaultFooter(f.isDefaultFooter());
        for (Article a : f.getArticles()) {
            cmd.getArticleIds().add(a.getId());
        }
        model.addAttribute("footerCommand", cmd);
        model.addAttribute("availableArticles", articleService.getAllArticles());
        model.addAttribute("footerArticles", f.getArticles());
        model.addAttribute("articleIdCommand", new IdCommand());
        return "admin/edit-footer";
    }

    /**
     * Add a new {@link Footer}
     * @param footerCommand the {@link FooterCommand} holding the {@link Footer}'s data
     * @return the admin view name
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addFooter(@ModelAttribute FooterCommand footerCommand) {
        Footer f = new Footer();
        f.setArticles(new ArrayList<Article>());
        if (footerCommand.getArticleIds() != null) {
            for (long id : footerCommand.getArticleIds()) {
                f.getArticles().add(articleService.getArticleById(id));
            }
        }
        f.setName(footerCommand.getName());
        footerService.saveFooter(f);
        return "redirect:/blog/admin";
    }

    /**
     * Edit an existing {@link Footer}
     * @param footerCommand the {@link FooterCommand} holding the {@link Footer}'s data
     * @param id the id of the Footer to be edited
     * @return the admin view name
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String editFooter(@ModelAttribute FooterCommand footerCommand, @PathVariable long id) {
        Footer f = footerService.getFooterById(id);
        f.setName(footerCommand.getName());
        f.setDefaultFooter(footerCommand.isDefaultFooter());
        if (f.getArticles() == null) {
            f.setArticles(new ArrayList<Article>());
        }
        if (footerCommand.getArticleIds() != null) {
            for (long articleId : footerCommand.getArticleIds()) {
                f.getArticles().add(articleService.getArticleById(articleId));
            }
        }
        footerService.saveFooter(f);
        return "redirect:/blog/admin";
    }

    /**
     * Add a {@link Article} to {@link Footer}
     * @param id the {@link Footer}'s id
     * @param articleIdCommand the {@link Article}'s id to be added
     * @return the {@link Footer}'s edit view name
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{id}/addArticle", method = RequestMethod.POST)
    public String addArticleToFooter(@PathVariable long id, @ModelAttribute IdCommand articleIdCommand) {
        Footer f = footerService.getFooterById(id);
        if (f.getArticles() == null) {
            f.setArticles(new ArrayList<Article>());
        }
        Article a = articleService.getArticleById(articleIdCommand.getId());
        f.getArticles().add(a);
        footerService.saveFooter(f);
        return "redirect:/blog/footers/" + f.getId() + "/edit";
    }

    /**
     * Remove an {@link Article} from a {@link Footer}
     * @param id the id of the {@link Footer}
     * @param articleId the {@link Article}'s id to be removed from the {@link Footer}
     * @return the {@link Footer}'s edit view name
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{id}/removeArticle/{articleId}", method = RequestMethod.DELETE)
    public String removeArticleFromFooter(@PathVariable long id, @PathVariable long articleId) {
        Footer f = footerService.getFooterById(id);
        if (f.getArticles() == null) {
            f.setArticles(new ArrayList<Article>());
        }
        f.getArticles().remove(articleService.getArticleById(articleId));
        footerService.saveFooter(f);
        return "redirect:/blog/footers/" + f.getId() + "/edit";
    }

    /**
     * Get the remove "{@link Article} from {@link Footer}" view
     * @param model the {@link ModelMap} holding the page data
     * @return the confirmation view name
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{id}/removeArticle/{articleId}", method = RequestMethod.GET)
    public String getRemoveArticleFromFooterView(ModelMap model) {
        model.addAttribute("message", "Really remove this article from the footer?");
        return "admin/confirmation";
    }
}
