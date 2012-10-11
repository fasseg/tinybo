package de.congrace.blog4j.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.congrace.blog4j.search.SearchResult;
import de.congrace.blog4j.service.api.ArticleService;
import de.congrace.blog4j.service.api.SearchService;

@Service("searchService")
public class SearchServiceImpl implements SearchService, InitializingBean {
    @Resource(name = "articleService")
    public ArticleService articleService;

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    @Transactional
    public List<SearchResult> getSearchResult(String search) {
        List<SearchResult> searchResults = new ArrayList<SearchResult>();
        searchResults.addAll(articleService.searchArticles(search));
        return searchResults;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(articleService);
    }

    @Override
    @Transactional
    public void indexAll() {
        articleService.index();
    }

}
