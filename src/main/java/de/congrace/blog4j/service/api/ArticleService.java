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

package de.congrace.blog4j.service.api;

import java.util.List;

import de.congrace.blog4j.dao.api.ArticleDao;
import de.congrace.blog4j.entities.Article;
import de.congrace.blog4j.entities.User;
import de.congrace.blog4j.search.SearchResult;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public interface ArticleService {

    List<Article> getAllArticles();

    Article getArticleById(long id);

    List<Article> getArticlesByCategoryId(long id,boolean onlyEnablesArticles);

    List<Article> getArticlesByCategoryId(long id, int start, int count);

    List<Article> getArticlesByTagId(long id);

    List<Article> getArticlesByTagId(long id, int start, int count);

    void saveArticle(Article a);

    public void deleteArticle(long id);

    void removeAttachment(long articleId, long attachmentId);

    List<SearchResult> searchArticles(String search);

    void index();

    public void setTagService(TagService tagService);

    public void setCategoryService(CategoryService categoryService);

    public void setArticleDao(ArticleDao articleDao);

    int getArticleCountByUser(User u);

    public void setAttachmentService(AttachmentService attachmentService);

}
