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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Entity
@Table(name = "b4j_tags", uniqueConstraints = @UniqueConstraint(columnNames = "tag_name"))
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tag implements Serializable {
    private static final long serialVersionUID = -6898745152287747634L;
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "tag_name")
    private String name;
    @Column(name = "article_count")
    private int articleCount;
    @ManyToMany(targetEntity = Article.class, mappedBy = "tags")
    @OrderBy("creationDate desc")
    private List<Article> articles;

    public Tag(Builder builder) {
        this.articleCount = builder.articleCount;
        this.id = builder.id;
        this.name = builder.name;
        if (builder.articles != null)
            this.articles = new ArrayList<Article>(builder.articles);
    }

    public Tag() {
        super();
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public static class Builder {
        private long id;
        private final String name;
        private int articleCount;
        private Collection<Article> articles;

        public Builder(String name) {
            super();
            this.name = name;
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withArticleCount(int count) {
            this.articleCount = count;
            return this;
        }

        public Builder withArticles(Collection<Article> articles) {
            this.articles = articles;
            return this;
        }

        public Tag build() {
            return new Tag(this);
        }

    }
}
