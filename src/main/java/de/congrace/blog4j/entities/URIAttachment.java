package de.congrace.blog4j.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class URIAttachment extends Attachment {
    @Column(name = "uri")
    private String uri;

    public URIAttachment() {
        super();
    }

    public URIAttachment(Builder b) {
        this.description = b.description;
        this.id = b.id;
        this.title = b.title;
        this.uri = b.uri;
        if (b.articles != null)
            this.articles = new ArrayList<Article>(b.articles);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public static class Builder {
        private long id;
        private final String title;
        private String description;
        private Collection<Article> articles;
        private String uri;

        public Builder(String title) {
            super();
            this.title = title;
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withDescription(String desc) {
            this.description = desc;
            return this;
        }

        public Builder withArticles(Collection<Article> articles) {
            this.articles = articles;
            return this;
        }

        public Builder withUri(String uri) {
            this.uri = uri;
            return this;
        }

        public URIAttachment build() {
            return new URIAttachment(this);
        }
    }
}
