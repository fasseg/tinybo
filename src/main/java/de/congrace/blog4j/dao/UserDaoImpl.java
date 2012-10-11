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

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import de.congrace.blog4j.dao.api.UserDao;
import de.congrace.blog4j.entities.User;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@SuppressWarnings("unchecked")
@Repository("userDao")
public class UserDaoImpl extends AbstractHibernateDaoImpl implements UserDao {
    public User getUserById(long id) {
        return (User) getCurrentSession().get(User.class, id);
    }

    public User getUserByName(String name) {
        return (User) getCurrentSession().createQuery("from User u where u.username=?").setString(0, name).uniqueResult();
    }

    public List<User> getAllUsers() {
        return getCurrentSession().createQuery("from User u").list();
    }

    public void deleteUser(User user) {
        getCurrentSession().delete(user);
    }

    public int getUserCount() {
        return (Integer) getCurrentSession().createCriteria(User.class).setProjection(Projections.rowCount()).uniqueResult();
    }

    @Override
    public int getAdminUserCount() {
        return (Integer) getCurrentSession().createCriteria(User.class).createCriteria("roles").add(Restrictions.eq("authority", "ROLE_ADMIN")).setProjection(Projections.rowCount()).uniqueResult();
    }
}
