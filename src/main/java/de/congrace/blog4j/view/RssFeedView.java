package de.congrace.blog4j.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;

import de.congrace.blog4j.controller.RSSFeedController;
import de.congrace.blog4j.controller.config.PageConfig;
import de.congrace.blog4j.entities.Article;

public class RssFeedView extends AbstractRssFeedView {
    private static final Properties props = new Properties();
    private static final Logger LOG = LoggerFactory.getLogger(RSSFeedController.class);
    private static String title;
    private static String description;
    private static String link;

    // load the properties statically and set the corresponding fields
    static {
        try {
            props.load(RSSFeedController.class.getClassLoader().getResourceAsStream("tinybo.properties"));
            title = props.getProperty("blog4j.rss.title");
            description = props.getProperty("blog4j.rss.description");
            link = props.getProperty("blog4j.rss.description");
            if (title == null || description == null || link == null) {
                LOG.error("Not all RSS properties have been set: ");
                LOG.error("rss title=" + title);
                LOG.error("rss desc=" + description);
                LOG.error("rss link=" + link);
            }
        } catch (IOException e) {
            LOG.error("unable to load properties. This shouldn't happen:", e);
        }
    }

    @Override
    protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
        feed.setTitle(title);
        feed.setDescription(description);
        feed.setLink(link);
    }

    @Override
    protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Item> items = new ArrayList<Item>();
        PageConfig config=PageConfig.getDefaultInstance();
        List<Article> articles=(List<Article>) model.get("articles");
        for (Article a : articles) {
            Item i = new Item();
            i.setTitle(a.getTitle());
            i.setAuthor(a.getAuthor().getUsername());
            i.setPubDate(a.getCreationDate());
            i.setLink(req.getScheme() + "://" + req.getLocalName() + ":" + req.getLocalPort() + config.getBasePath() + "articles/" + a.getId());
            Description d = new Description();
            d.setType("text/html");
            d.setValue(a.getTeaser());
            i.setDescription(d);
            items.add(i);
        }
        return items;
    }

}
