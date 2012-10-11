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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Entity
@Table(name = "b4j_roles", uniqueConstraints = @UniqueConstraint(columnNames = "authority"))
public class Role implements GrantedAuthority {
    private static final long serialVersionUID = -5959868534901984665L;

    public enum Authority {
        ROLE_USER, ROLE_ADMIN
    }

    @Id
    @GeneratedValue
    private long id;
    private String authority;

    public Role(Builder builder) {
        super();
        this.id = builder.id;
        this.authority = builder.authority;
    }

    public Role() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public int compareTo(Object o) {
        Role that = (Role) o;
        if (this == that)
            return 0;
        return this.getAuthority().compareTo(that.getAuthority());
    }

    public static class Builder {
        private long id;
        private final String authority;

        public Builder(String authority) {
            super();
            this.authority = authority;
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Role build() {
            return new Role(this);
        }

    }
}
