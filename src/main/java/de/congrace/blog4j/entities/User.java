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

package de.congrace.blog4j.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Entity
@Table(name = "b4j_users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements UserDetails, SaltSource {
    private static final long serialVersionUID = -7958910308567586072L;
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User() {
        super();
    }

    public User(Builder b) {
        this.accountNonExpired = b.accountNonExpired;
        this.accountNonLocked = b.accountNonLocked;
        this.credentialsNonExpired = b.credentialsNonExpired;
        this.email = b.email;
        this.enabled = b.enabled;
        this.id = b.id;
        this.password = b.password;
        this.username = b.username;
        if (b.roles != null)
            this.roles = new HashSet<Role>(b.roles);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>(getRoles().size());
        auths.addAll(getRoles());
        return auths;
    }

    @Override
    public Object getSalt(UserDetails user) {
        return new Long(this.id);
    }

    public static class Builder {
        private long id;
        private final String username;
        private String password;
        private boolean accountNonLocked = true;
        private boolean accountNonExpired = true;
        private boolean credentialsNonExpired = true;
        private boolean enabled = true;
        private final Collection<Role> roles;
        private String email;

        public Builder(String username, String password, Collection<Role> roles) {
            super();
            this.username = username;
            this.password = password;
            this.roles = roles;
        }

        public Builder(String username, Collection<Role> roles) {
            super();
            this.username = username;
            this.roles = roles;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withAccountNonLocked(boolean b) {
            this.accountNonLocked = b;
            return this;
        }

        public Builder withAccountNonExpired(boolean b) {
            this.accountNonExpired = b;
            return this;
        }

        public Builder withCredentialsNonExpired(boolean b) {
            this.credentialsNonExpired = b;
            return this;
        }

        public Builder withEnabled(boolean b) {
            this.enabled = b;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
