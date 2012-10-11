package de.congrace.blog4j.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

@Entity
public class FileAttachment extends Attachment {
    private String fileName;
    private String mimeType;
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;

    public FileAttachment(Builder builder) {
        super();
        this.content = builder.content;
        this.description = builder.description;
        this.fileName = builder.fileName;
        this.id = builder.id;
        this.mimeType = builder.mimeType;
        this.title = builder.title;
        if (builder.articles != null)
            this.articles = new ArrayList<Article>(builder.articles);
    }

    public FileAttachment() {
        super();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public static class Builder {
        private long id;
        private final String title;
        private String description;
        private Collection<Article> articles;
        private String fileName;
        private String mimeType;
        private byte[] content;

        public Builder(String title) {
            super();
            this.title = title;
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder with(Collection<Article> articles) {
            this.articles = articles;
            return this;
        }

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder withContent(byte[] content) {
            this.content = content;
            return this;
        }

        public FileAttachment build() {
            return new FileAttachment(this);
        }
    }

}
