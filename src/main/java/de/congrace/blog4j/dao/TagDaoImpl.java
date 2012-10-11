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

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import de.congrace.blog4j.dao.api.TagDao;
import de.congrace.blog4j.entities.Tag;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@SuppressWarnings("unchecked")
@Repository("tagDao")
public class TagDaoImpl extends AbstractHibernateDaoImpl implements TagDao {

    public Tag getTagById(long id) {
        return (Tag) getCurrentSession().createQuery("from Tag t where t.id=?").setLong(0, id).uniqueResult();
    }

    public Tag getTagByName(String name) {
        return (Tag) getCurrentSession().createQuery("from Tag t where t.name=?").setString(0, name).uniqueResult();
    }

    public List<Tag> getAllTags() {
        return (List<Tag>) getCurrentSession().createQuery("from Tag t").list();
    }

    public void updateArticleCount() {
        //TODO: get rid of this by using filtering
        // lots of tags might get updatet lets do this natively
        getCurrentSession().createSQLQuery("update b4j_tags set article_count=(select count(articles_id) from b4j_articles_b4j_tags where b4j_articles_b4j_tags.tags_id=b4j_tags.id)").executeUpdate();
        //        for (Tag t : getAllTags()) {
        //            getCurrentSession().createQuery("update Tag t set t.articleCount=? where t.id=?").setInteger(0, t.getArticles().size()).setLong(1, t.getId()).executeUpdate();
        //        }
    }

    public List<Tag> getTagByArticleCount(int count) {

        return (List<Tag>) getCurrentSession().createCriteria(Tag.class).add(Restrictions.gt("articleCount", 0)).addOrder(Order.desc("articleCount")).setMaxResults(count).list();
    }
}
