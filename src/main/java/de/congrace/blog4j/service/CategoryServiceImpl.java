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

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.congrace.blog4j.dao.api.CategoryDao;
import de.congrace.blog4j.entities.Category;
import de.congrace.blog4j.service.api.CategoryService;
import de.congrace.tinybo.error.TinyboException;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Service("categoryService")
public class CategoryServiceImpl implements InitializingBean, CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Resource(name = "categoryDao")
    public CategoryDao categoryDao;

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Transactional
    public Category getDefaultCategory() {
        return categoryDao.getDefaultCategory();
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(categoryDao, "categoryDao can not be null");
    }

    @Transactional
    public Category getCategoryById(long id) {
        return categoryDao.getCategoryById(id);
    }

    @Transactional
    public Category getCategroyByName(String name) {
        return categoryDao.getCategoryByName(name);
    }

    @Transactional
    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    @Transactional
    public void saveCategory(Category category) throws TinyboException {
        categoryDao.saveCategory(category);
    }

    @Transactional
    public int getNextOrderValue(Category parent) {
        return categoryDao.getNextOrderValue(parent);
    }

    public Category getCategoryByOrder(Category parent, int order) {
        return categoryDao.getCategoryByOrder(parent, order);
    }

    @Transactional
    public void moveCategoryUp(long id) throws TinyboException {
        Category cMove = getCategoryById(id);
        Category cDependedant = getCategoryByOrder(cMove.getParentCategory(), cMove.getOrder() - 1);
        if (cDependedant == null) {
            return;
        }
        cDependedant.setOrder(cMove.getOrder());
        cMove.setOrder(cMove.getOrder() - 1);
        saveCategory(cMove);
        saveCategory(cDependedant);
    }

    @Transactional
    public void moveCategoryDown(long id) throws TinyboException {
        Category cMove = getCategoryById(id);
        Category cDependant = getCategoryByOrder(cMove.getParentCategory(), cMove.getOrder() + 1);
        if (cDependant == null) {
            return;
        }
        cDependant.setOrder(cMove.getOrder());
        cMove.setOrder(cMove.getOrder() + 1);
        saveCategory(cMove);
        saveCategory(cDependant);
    }

    @Transactional
    public void updateArticleCount() {
        categoryDao.updateArticleCount();
    }

    @Transactional
    public List<Category> getCategoriesById(List<String> ids) {
        return categoryDao.getCategoryByIds(ids);
    }

    @Override
    @Transactional
    public void deleteCategory(Category cat) throws TinyboException {
        if (cat.getArticleCount() > 0) {
            LOG.error("can not delete category " + cat.getName() + " since it has articles");
            throw new TinyboException("Unable to remove category. Reason: Category has still got articles");
        }
        if (categoryDao.getCategoriesByParent(cat).size() > 0) {
            LOG.error("can not delete category " + cat.getName() + " since it has child categories");
            throw new TinyboException("Unable to remove category. Reason: Category has got child categories");
        }
        categoryDao.deleteCategory(cat);
    }

    @Override
    @Transactional
    public List<Category> getAllRootCategories() {
        return categoryDao.getAllRootCategories();
    }

    @Transactional
    public void updateChildCategories(Category parent) {
        categoryDao.updateChildCategories(parent);
    }
}
