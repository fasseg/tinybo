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
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import de.congrace.blog4j.dao.api.CategoryDao;
import de.congrace.blog4j.entities.Article;
import de.congrace.blog4j.entities.Category;
import de.congrace.tinybo.error.TinyboException;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@SuppressWarnings("unchecked")
@Repository("categoryDao")
public class CategoryDaoImpl extends AbstractHibernateDaoImpl implements CategoryDao {
    public static final Logger LOG = LoggerFactory.getLogger(CategoryDaoImpl.class);

    public Category getCategoryById(long id) {
        return (Category) getCurrentSession().get(Category.class, id);
    }

    public Category getCategoryByName(String name) {
        return (Category) getCurrentSession().createQuery("from Category c where c.name=?").setString(0, name).uniqueResult();
    }

    public List<Category> getAllCategories() {
        return getCurrentSession().createQuery("from Category c order by c.order").list();
    }

    public int getNextOrderValue(Category parent) {
        Criteria crit = getCurrentSession().createCriteria(Category.class);
        if (parent == null) {
            crit.add(Restrictions.isNull("parentCategory"));
        } else {
            crit.createCriteria("parentCategory").add(Restrictions.idEq(parent.getId()));
        }
        crit.setProjection(Projections.rowCount());
        return ((Integer) crit.uniqueResult()).intValue();
    }

    public Category getDefaultCategory() {
        return (Category) getCurrentSession().createQuery("from Category c where c.parentCategory is null order by c.order").setMaxResults(1).uniqueResult();
    }

    public Category getCategoryByOrder(Category parent, int order) {
        Criteria crit = this.getCurrentSession().createCriteria(Category.class).add(Restrictions.eq("order", order));
        if (parent == null) {
            crit.add(Restrictions.isNull("parentCategory"));
        } else {
            crit.createCriteria("parentCategory").add(Restrictions.idEq(parent.getId()));
        }
        return (Category) crit.uniqueResult();
    }

    public void updateArticleCount() {
        List<Category> categories = this.getAllCategories();
        try {
            for (Category c : categories) {
                c.setArticleCount(getArticleCount(c));
                saveCategory(c);
            }
        } catch (TinyboException e) {
            LOG.error("Exception while updating articel counts", e);
        }
    }

    public int getArticleCount(Category c) {
        int count = (Integer) this.getCurrentSession().createCriteria(Article.class).createCriteria("categories").add(Restrictions.eq("id", c.getId())).setProjection(Projections.rowCount()).uniqueResult();
        for (Category child : c.getChildCategories()) {
            count += getArticleCount(child);
        }
        return count;
    }

    public void fixCategoryOrder(Category parent) {
        List<Category> cats = null;
        if (parent == null) {
            cats = getAllRootCategories();
        } else {
            cats = getCategoriesByParent(parent);
        }
        for (int order = 0; order < cats.size(); order++) {
            Category c = cats.get(order);
            c.setOrder(order);
            saveOrUpdate(c);
        }
    }

    public List<Category> getCategoryByIds(List<String> ids) {
        List<Long> longIds = new ArrayList<Long>();
        for (String s : ids) {
            try {
                long id = Long.parseLong(s);
                longIds.add(id);
            } catch (NumberFormatException e) {
                LOG.warn("could not translate id " + s + " to a long");
            }

        }
        return getCurrentSession().createQuery("from Category c where c.id in (:ids)").setParameterList("ids", longIds).list();
    }

    public void deleteCategory(Category cat) throws TinyboException {
        if (cat.getParentCategory() != null) {
            cat.getParentCategory().getChildCategories().remove(cat);
            saveCategory(cat.getParentCategory());
        }
        deleteObject(cat);
        fixCategoryOrder(cat.getParentCategory());
    }

    public void saveCategory(Category cat) throws TinyboException {
        if (cat.getParentCategory() != null && cat.getParentCategory().getParentCategory() != null) {
            throw new TinyboException("Only 2 Levels of categories are supported");
        }
        saveOrUpdate(cat);
        if (cat.getParentCategory() != null) {
            updateChildCategories(cat.getParentCategory());
        }
    }

    public void updateChildCategories(Category cat) {
        cat.setChildCategories(getCategoriesByParent(cat));
        if (LOG.isDebugEnabled()) {
            LOG.debug("setting " + cat.getChildCategories().size() + " children for category " + cat.getName());
        }
        saveOrUpdate(cat);
    }

    public List<Category> getCategoriesByParent(Category parent) {
        return (List<Category>) getCurrentSession().createCriteria(Category.class).createCriteria("parentCategory").add(Restrictions.idEq(parent.getId())).addOrder(Order.asc("order")).list();
    }

    @Override
    public List<Category> getAllRootCategories() {
        return (List<Category>) this.getCurrentSession().createCriteria(Category.class).add(Restrictions.isNull("parentCategory")).addOrder(Order.asc("order")).list();
    }

}
