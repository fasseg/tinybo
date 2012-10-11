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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.congrace.blog4j.dao.api.RoleDao;
import de.congrace.blog4j.entities.Role;
import de.congrace.blog4j.service.api.RoleService;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService, InitializingBean {

    @Resource(name = "roleDao")
    public RoleDao roleDao;

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional
    public void saveRole(Role r) {
        roleDao.saveOrUpdate(r);
    }

    @Transactional
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Transactional
    public Role getRoleById(long id) {
        return roleDao.getRoleById(id);
    }

    @Transactional
    public Role getRoleByAuthority(String authority) {
        return roleDao.getRoleByName(authority);
    }

    @Transactional
    public Role getAdminRole() {
        Role r = getRoleByAuthority(Role.Authority.ROLE_ADMIN.toString());
        return r;
    }

    @Transactional
    public Role getDefaultRole() {
        Role r = getRoleByAuthority(Role.Authority.ROLE_USER.toString());
        return r;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(roleDao);
    }
}
