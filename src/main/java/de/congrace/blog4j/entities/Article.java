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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import de.congrace.blog4j.tools.MailUtil;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Entity
@Table(name = "b4j_articles", uniqueConstraints = @UniqueConstraint(columnNames = "title"))
@Indexed
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article implements Serializable,Comparable<Article> {
    private static final long serialVersionUID = 531820950212469518L;

    public enum ImageAlignment {
        LEFT, RIGHT;
    }

    @Id
    @DocumentId
    @GeneratedValue
    private long id;
    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String title;
    @Type(type = "text")
    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String msg;
    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String teaser;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @Temporal(TemporalType.DATE)
    private Date editDate;
    @Column(name = "robot_index")
    private boolean robotIndex;
    @Column(name = "robot_follow")
    private boolean robotFollow;
    @Column(name = "enabled")
    private boolean enabled;
    @ManyToMany(targetEntity = Category.class)
    private Set<Category> categories;
    @ManyToMany(targetEntity = Tag.class)
    private Set<Tag> tags;
    @ManyToOne(optional = false)
    private User author;
    @ManyToMany(targetEntity = Attachment.class, cascade = CascadeType.ALL)
    private Set<Attachment> attachments;
    @ManyToOne(targetEntity = Image.class)
    private Image image;
    private ImageAlignment imageAlignment;

    public Article(Builder builder) {
        super();
        this.id = builder.id;
        this.author = builder.author;
        this.creationDate = builder.creationDate;
        this.editDate = builder.editDate;
        this.enabled = builder.enabled;
        this.msg = builder.msg;
        this.robotFollow = builder.robotFollow;
        this.robotIndex = builder.robotIndex;
        this.teaser = builder.teaser;
        this.title = builder.title;
        if (builder.image != null) {
            this.image = builder.image;
            this.imageAlignment = builder.imageAlignment;
        }
        if (builder.attachments != null)
            this.attachments = new HashSet<Attachment>(builder.attachments);
        if (builder.tags != null)
            this.tags = new HashSet<Tag>(builder.tags);
        if (builder.categories != null)
            this.categories = new HashSet<Category>(builder.categories);
    }

    public ImageAlignment getImageAlignment() {
        return imageAlignment;
    }

    public void setImageAlignment(ImageAlignment imageAlignment) {
        this.imageAlignment = imageAlignment;
    }

    public Article() {
        super();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * this is for scrambling the email to make a email collector's life harder
     * 
     * @return a scrambled email
     */
    public String getAuthorEmail() {
        if (author.getEmail() == null)
            return null;
        return MailUtil.scrambleEmail(author.getEmail());
    }

    public boolean isRobotFollow() {
        return robotFollow;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRobotFollow(boolean robotFollow) {
        this.robotFollow = robotFollow;
    }

    public boolean isRobotIndex() {
        return robotIndex;
    }

    public void setRobotIndex(boolean robotIndex) {
        this.robotIndex = robotIndex;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class Builder {
        private long id;
        private final String title;
        private final String msg;
        private final String teaser;
        private Date creationDate;
        private Date editDate;
        private boolean robotIndex;
        private boolean robotFollow;
        private boolean enabled;
        private Collection<Category> categories;
        private Collection<Tag> tags;
        private User author;
        private Collection<Attachment> attachments;
        private Image image;
        private ImageAlignment imageAlignment;

        public Builder(String title, String msg, String teaser) {
            super();
            this.title = title;
            this.msg = msg;
            this.teaser = teaser;
        }

        public Builder withAuthor(User author) {
            this.author = author;
            return this;
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withCreationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder withEditDate(Date editDate) {
            this.editDate = editDate;
            return this;
        }

        public Builder withRobotIndex(boolean robotIndex) {
            this.robotIndex = robotIndex;
            return this;
        }

        public Builder withRobotFollow(boolean robotFollow) {
            this.robotFollow = robotFollow;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withCategories(Collection<Category> categories) {
            this.categories = categories;
            return this;
        }

        public Builder withTags(Collection<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Builder withAttachments(Collection<Attachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public Builder withImage(Image image) {
            this.image = image;
            return this;
        }

        public Builder withImageAlignment(ImageAlignment imageAlignment) {
            this.imageAlignment = imageAlignment;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }

    @Override
    public int compareTo(Article o) {
        return o.getCreationDate().compareTo(this.getCreationDate());
    }
}
