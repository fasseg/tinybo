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

import org.springframework.transaction.annotation.Transactional;

import de.congrace.blog4j.dao.api.CategoryDao;
import de.congrace.blog4j.entities.Category;
import de.congrace.tinybo.error.TinyboException;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public interface CategoryService {

    Category getDefaultCategory();

    Category getCategoryById(long id);

    Category getCategroyByName(String name);

    List<Category> getAllCategories();

    List<Category> getAllRootCategories();

    List<Category> getCategoriesById(List<String> ids);

    void saveCategory(Category category) throws TinyboException;

    public int getNextOrderValue(Category parent);

    public Category getCategoryByOrder(Category parent, int i);

    public void moveCategoryUp(long id) throws TinyboException;

    public void moveCategoryDown(long id) throws TinyboException;

    public void updateArticleCount();

    void deleteCategory(Category cat) throws TinyboException;

    public void setCategoryDao(CategoryDao categoryDao);

    @Transactional
    public void updateChildCategories(Category parent);
}
