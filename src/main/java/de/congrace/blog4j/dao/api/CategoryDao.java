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

import org.hibernate.criterion.Restrictions;

import de.congrace.blog4j.entities.Category;
import de.congrace.tinybo.error.TinyboException;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public interface CategoryDao extends BaseDao {

    List<Category> getAllCategories();

    Category getCategoryById(long id);

    Category getCategoryByName(String name);

    public int getNextOrderValue(Category parent);

    public Category getDefaultCategory();

    public Category getCategoryByOrder(Category parent, int order);

    public void updateArticleCount();

    public List<Category> getCategoryByIds(List<String> ids);

    public void deleteCategory(Category cat) throws TinyboException;

    public List<Category> getCategoriesByParent(Category parent);

    List<Category> getAllRootCategories();

    void saveCategory(Category category) throws TinyboException;

    public void updateChildCategories(Category cat);

}
