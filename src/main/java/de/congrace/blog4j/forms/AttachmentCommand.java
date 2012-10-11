package de.congrace.blog4j.forms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class AttachmentCommand extends IdCommand {
    private long articleId; // for adding new attachment directly to an article
    private String url;
    private String description;
    private String title;
    private CommonsMultipartFile file;
    private String type;
    private String fileName;
    private String mimeType;
    private static Map<String, String> types;

    static {
        types = new HashMap<String, String>();
        types.put("uri", "Uri Attachment");
        types.put("file", "File Attachment");
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

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public Map<String, String> getTypes() {
        return types;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CommonsMultipartFile getFile() {
        return file;
    }

    public void setFile(CommonsMultipartFile file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
