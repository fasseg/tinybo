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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Entity
@Table(name = "b4j_categories", uniqueConstraints = @UniqueConstraint(columnNames = { "category_name", "order_value" }))
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {
    private static final long serialVersionUID = 1703119096251866910L;
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "category_name")
    private String name;
    @Column(name = "order_value")
    private int order;
    @Column(name = "article_count")
    private int articleCount;
    @ManyToOne(targetEntity = Footer.class)
    private Footer footer;
    @ManyToMany(mappedBy = "categories")
    @OrderBy("creationDate DESC")
    private List<Article> articles;
    @ManyToOne(targetEntity = Category.class, optional = true)
    private Category parentCategory;
    @OneToMany(targetEntity = Category.class, cascade = CascadeType.ALL)
    @OrderBy("order ASC")
    private List<Category> childCategories;

    public Category(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.order = builder.order;
        this.articleCount = builder.articleCount;
        this.parentCategory = builder.parentCategory;
        this.childCategories = builder.childCategories;
        if (builder.articles != null)
            this.articles = builder.articles;
    }

    public Category() {
        super();
    }

    public List<Category> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Footer getFooter() {
        return footer;
    }

    public void setFooter(Footer footer) {
        this.footer = footer;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public static class Builder {
        private final String name;
        private long id;
        private List<Article> articles;
        private int articleCount;
        private int order;
        private Category parentCategory;
        private List<Category> childCategories;

        public Builder(String name) {
            super();
            this.name = name;
        }

        public Builder withParentCategory(Category c) {
            this.parentCategory = c;
            return this;
        }

        public Builder withChildCategories(List<Category> childCategories) {
            this.childCategories = childCategories;
            return this;
        }

        public Builder withArticles(List<Article> articles) {
            this.articles = articles;
            return this;
        }

        public Builder withArticleCount(int articleCount) {
            this.articleCount = articleCount;
            return this;
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withOrder(int order) {
            this.order = order;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }

}
