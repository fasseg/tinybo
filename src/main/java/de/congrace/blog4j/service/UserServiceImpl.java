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
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.congrace.blog4j.dao.api.UserDao;
import de.congrace.blog4j.entities.Role;
import de.congrace.blog4j.entities.User;
import de.congrace.blog4j.service.api.ArticleService;
import de.congrace.blog4j.service.api.RoleService;
import de.congrace.blog4j.service.api.UserService;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Service("userService")
public class UserServiceImpl implements UserDetailsService, InitializingBean, UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource(name = "roleService")
    public RoleService roleService;
    @Resource(name = "userDao")
    public UserDao userDao;
    @Resource(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder;
    @Resource(name = "articleService")
    public ArticleService articleService;

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User u = userDao.getUserByName(username);
        if (u == null) {
            throw new UsernameNotFoundException("User '" + username + "' was not found");
        }
        //        LOG.debug("admin would be:" + passwordEncoder.encodePassword("admin", new Long(83)));
        return u;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(roleService);
        Assert.notNull(userDao);
    }

    @Transactional
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional
    public void saveUser(User u) {
        userDao.saveOrUpdate(u);
    }

    @Transactional
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Transactional
    public void addUser(User u, String pass) {
        userDao.saveOrUpdate(u);
        u.setPassword(passwordEncoder.encodePassword(pass, u.getId()));
        userDao.saveOrUpdate(u);
    }

    @Transactional
    public void deleteUser(long id) {
        User u = userDao.getUserById(id);
        Role adminRole = roleService.getAdminRole();
        int articleCount = articleService.getArticleCountByUser(u);
        int adminCount = userDao.getAdminUserCount();
        LOG.debug("trying to delete user " + u.getUsername());
        LOG.debug("global admin count: " + adminCount);
        LOG.debug("is admin user: " + u.getRoles().contains(adminRole));
        LOG.debug("number articles associated: " + articleCount);
        if (u.getAuthorities().contains(adminRole) && adminCount == 1) {
            LOG.error("cant delete last admin user");
            return;
        }
        if (articleCount > 0) {
            LOG.error("User still has articles associated");
            return;
        }
        LOG.debug("deleting user " + u.getUsername());
        userDao.deleteUser(u);
    }

    @Transactional
    public int getUserCount() {
        return userDao.getUserCount();
    }
}
