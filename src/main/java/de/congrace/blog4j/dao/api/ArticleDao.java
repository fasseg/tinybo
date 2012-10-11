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

package de.congrace.blog4j.dao.api;

import java.util.List;

import de.congrace.blog4j.entities.Article;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public interface ArticleDao extends BaseDao {

    List<Article> getAllArticles();

    Article getArticleById(long id);

    Article getArticleByTitle(String title);

    public List<Article> getArticleByCategoryId(long id, int start, int count,boolean onlyEnabledArticles,boolean includeSubCategories);

    public List<Article> getArticleByCategoryId(long id,boolean onlyEnabledArticles);

    public List<Article> getArticleByTagId(long id);

    public List<Article> getArticleByTagId(long id, int start, int count);

    public List<Article> searchArticles(String search);

    public List<Article> getAllEnabledArticles();

    void index();

    void deleteArticle(Article a);

    int getArticleCountByUserId(long id);
}
