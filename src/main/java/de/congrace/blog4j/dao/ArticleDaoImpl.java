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

package de.congrace.blog4j.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import de.congrace.blog4j.dao.api.ArticleDao;
import de.congrace.blog4j.dao.api.CategoryDao;
import de.congrace.blog4j.dao.api.TagDao;
import de.congrace.blog4j.entities.Article;
import de.congrace.blog4j.entities.Category;
import de.congrace.blog4j.entities.Tag;
import de.congrace.blog4j.tools.LuceneUtil;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@SuppressWarnings("unchecked")
@Repository("articleDao")
public class ArticleDaoImpl extends AbstractHibernateDaoImpl implements ArticleDao {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleDaoImpl.class);
    @Resource(name = "categoryDao")
    private CategoryDao categoryDao;
    @Resource(name = "tagDao")
    private TagDao tagDao;

    public void setCategoryDao(CategoryDao categroyDao) {
        this.categoryDao = categroyDao;
    }

    public Article getArticleById(long id) {
        return (Article) getCurrentSession().createQuery("from Article a where a.id=?").setLong(0, id).uniqueResult();
    }

    public Article getArticleByTitle(String title) {
        return (Article) getCurrentSession().createQuery("from Article a where a.title=?").setString(0, title).uniqueResult();

    }

    public List<Article> getAllArticles() {
        return getCurrentSession().createQuery("from Article a order by a.creationDate desc").list();
    }

    public List<Article> getAllEnabledArticles() {
        return getCurrentSession().createQuery("from Article a where a.enabled=? order by a.creationDate desc").setBoolean(0, true).list();
    }

    public List<Article> getArticleByCategoryId(long id, int start, int count) {
        return getArticleByCategoryId(id, start, count, true, true);
    }

    public List<Article> getArticleByCategoryId(long id, int start, int count, boolean onlyEnabledArticles, boolean includeSubCategories) {
        List<Article> articles=new ArrayList<Article>();
        Category c = categoryDao.getCategoryById(id);
        if (onlyEnabledArticles) {
            articles.addAll(this.getCurrentSession().createFilter(c.getArticles(), "where enabled=true order by creationdate desc").list());
        } else {
            articles.addAll(c.getArticles());
        }
        if (includeSubCategories){
            for (Category sub:c.getChildCategories()){
                articles.addAll(getArticleByCategoryId(sub.getId(),onlyEnabledArticles));
            }
        }
        Collections.sort(articles);
        return articles.subList(start, (start + count > articles.size()) ? articles.size() : start + count);
    }

    public List<Article> getArticleByCategoryId(long id,boolean onlyEnabledArticles) {
        if (onlyEnabledArticles){
            return getCurrentSession().createCriteria(Article.class).add(Restrictions.eq("enabled", true)).createCriteria("categories").add(Restrictions.eq("id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        }else{
            return getCurrentSession().createCriteria(Article.class).createCriteria("categories").add(Restrictions.eq("id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        }
    }

    public List<Article> getArticleByTagId(long id) {
        //TODO:WTH?!?!? did i create this? do i need this?
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Article> getArticleByTagId(long id, int start, int count) {
        Tag t = tagDao.getTagById(id);
        return t.getArticles().subList(start, (start + count > t.getArticles().size()) ? t.getArticles().size() : start + count);
    }

    @Override
    public List<Article> searchArticles(String search) {
        String[] fields = { "title", "teaser", "msg" };
        try {
            Query q = LuceneUtil.createLuceneQuery(search, fields);
            LOG.debug("querying index: " + q.toString());
            return getCurrentSearchSession().createFullTextQuery(q, Article.class).list();
        } catch (ParseException e) {
            LOG.error("Exception while searching", e);
            return null;
        }
    }

    @Override
    public void index() {
        for (Article a : getAllEnabledArticles()) {
            getCurrentSearchSession().index(a);
        }
    }

    @Override
    public void deleteArticle(Article a) {
        deleteObject(a);
    }

    @Override
    public int getArticleCountByUserId(long id) {
        return (Integer) getCurrentSession().createCriteria(Article.class).createCriteria("author").add(Restrictions.eq("id", id)).setProjection(Projections.rowCount()).uniqueResult();
    }

}
