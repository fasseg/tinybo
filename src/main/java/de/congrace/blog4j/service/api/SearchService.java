package de.congrace.blog4j.service.api;

import java.util.List;

import de.congrace.blog4j.search.SearchResult;

public interface SearchService {
    public List<SearchResult> getSearchResult(String search);

    public void indexAll();

    public void setArticleService(ArticleService articleService);
}
