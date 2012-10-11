package de.congrace.blog4j.search;

public class SearchResult {
    private String title;
    private String text;
    private String uri;

    public SearchResult(String title, String text, String uri) {
        super();
        this.title = title;
        this.text = text;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getUri() {
        return uri;
    }
}
