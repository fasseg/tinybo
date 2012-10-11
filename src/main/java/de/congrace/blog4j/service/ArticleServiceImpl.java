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

package de.congrace.blog4j.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.congrace.blog4j.dao.api.ArticleDao;
import de.congrace.blog4j.entities.Article;
import de.congrace.blog4j.entities.Attachment;
import de.congrace.blog4j.entities.Category;
import de.congrace.blog4j.entities.Image;
import de.congrace.blog4j.entities.User;
import de.congrace.blog4j.search.SearchResult;
import de.congrace.blog4j.service.api.ArticleService;
import de.congrace.blog4j.service.api.AttachmentService;
import de.congrace.blog4j.service.api.CategoryService;
import de.congrace.blog4j.service.api.ImageService;
import de.congrace.blog4j.service.api.TagService;
import de.congrace.blog4j.tools.Blog4jCodeUtil;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Service("articleService")
public class ArticleServiceImpl implements InitializingBean, ArticleService {
    @Resource(name = "articleDao")
    public ArticleDao articleDao;
    @Resource(name = "categoryService")
    public CategoryService categoryService;
    @Resource(name = "tagService")
    public TagService tagService;
    @Resource(name = "attachmentService")
    public AttachmentService attachmentService;
    @Resource(name = "imageService")
    public ImageService imageService;

    private static final Logger LOG = LoggerFactory.getLogger(ArticleServiceImpl.class);

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(articleDao, "articleDao can not be null");
        Assert.notNull(categoryService, "categoryService can not be null");
        Assert.notNull(attachmentService, "attachmentService cannot be null");
        Assert.notNull(tagService);
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    @Transactional
    public List<Article> getAllArticles() {
        return articleDao.getAllArticles();
    }

    @Transactional
    public Article getArticleById(long id) {
        return articleDao.getArticleById(id);
    }

    @Transactional
    public List<Article> getArticlesByCategoryId(long id,boolean onlyEnabledArticles) {
        return articleDao.getArticleByCategoryId(id,onlyEnabledArticles);
    }

    @Transactional
    public void saveArticle(Article a) {
        if (a.getCategories() == null) {
            a.setCategories(new HashSet<Category>());
        }
        articleDao.saveOrUpdate(a);
        tagService.updateArticleCount();
        categoryService.updateArticleCount();
    }

    @Transactional
    public List<Article> getArticlesByCategoryId(long id, int start, int count) {
        return articleDao.getArticleByCategoryId(id, start, count,true,true);
    }

    @Transactional
    public List<Article> getArticlesByTagId(long id) {
        return articleDao.getArticleByTagId(id);
    }

    @Transactional
    public List<Article> getArticlesByTagId(long id, int start, int count) {
        return articleDao.getArticleByTagId(id, start, count);
    }

    @Override
    @Transactional
    public void deleteArticle(long id) {
        Article a = getArticleById(id);
        LOG.debug("removing article: " + id);
        attachmentService.cleanUpAttachments();
        ;
        articleDao.deleteArticle(a);
    }

    @Override
    @Transactional
    public void removeAttachment(long articleId, long attachmentId) {
        LOG.debug("removing attachment" + attachmentId + " from  article " + articleId);
        Article a = articleDao.getArticleById(articleId);
        Attachment att = attachmentService.getAttachmentById(attachmentId);
        if (!a.getAttachments().remove(att)) {
            LOG.warn("Could not remove attachment from Article");
        }
        attachmentService.cleanUpAttachments();
        ;
        articleDao.saveOrUpdate(a);
    }

    @Override
    public void index() {
        articleDao.index();
    }

    @Override
    public List<SearchResult> searchArticles(String search) {
        List<SearchResult> result = new ArrayList<SearchResult>();
        for (Article a : articleDao.searchArticles(search)) {
            if (a.getMsg() == null)
                a.setMsg("");
            String text = Blog4jCodeUtil.stripTags(a.getMsg());
            SearchResult sr = new SearchResult(a.getTitle(), text.substring(0, (text.length() < 128 ? text.length() : 128)), "articles/" + a.getId());
            result.add(sr);
        }
        return result;
    }

    @Override
    @Transactional
    public int getArticleCountByUser(User u) {
        return articleDao.getArticleCountByUserId(u.getId());
    }

    @Transactional
    public void removeImageFromArticle(Article a) {
        Image i = a.getImage();
        if (i == null)
            return;
        a.setImage(null);
        saveArticle(a);
        imageService.deleteImage(i);
    }

}
