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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import de.congrace.blog4j.dao.api.BaseDao;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public abstract class AbstractHibernateDaoImpl implements InitializingBean, BaseDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(sessionFactory, "SessionFactory can not be null");
    }

    public void saveOrUpdate(Object o) {
        getCurrentSession().saveOrUpdate(o);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public FullTextSession getCurrentSearchSession() {
        return Search.getFullTextSession(getCurrentSession());
    }

    protected void deleteObject(Object o) {
        this.getCurrentSession().delete(o);
    }
}
