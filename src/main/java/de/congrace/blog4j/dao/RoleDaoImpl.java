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

import org.springframework.stereotype.Repository;

import de.congrace.blog4j.dao.api.RoleDao;
import de.congrace.blog4j.entities.Role;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@SuppressWarnings("unchecked")
@Repository("roleDao")
public class RoleDaoImpl extends AbstractHibernateDaoImpl implements RoleDao {
    public Role getRoleById(long id) {
        return (Role) getCurrentSession().get(Role.class, id);
    }

    public List<Role> getAllRoles() {
        return getCurrentSession().createQuery("from Role r").list();
    }

    public Role getRoleByName(String name) {
        return (Role) getCurrentSession().createQuery("from Role r where r.authority=?").setString(0, name).uniqueResult();
    }
}
