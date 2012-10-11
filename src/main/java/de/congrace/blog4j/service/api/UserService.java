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

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import de.congrace.blog4j.dao.api.UserDao;
import de.congrace.blog4j.entities.User;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public interface UserService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException;

    List<User> getAllUsers();

    void saveUser(User u);

    User getUserById(long id);

    void deleteUser(long id);

    int getUserCount();

    public void setRoleService(RoleService roleService);

    public void setUserDao(UserDao userDao);

    @Transactional
    public void addUser(User u, String pass);
}
