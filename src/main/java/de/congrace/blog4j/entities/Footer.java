package de.congrace.blog4j.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Frank Asseg, frank.asseg@congrace.de
 */
@Entity
@Table(name = "b4j_footers")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Footer {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "footer_name")
    private String name;
    @Column(name = "footer_default")
    private boolean defaultFooter;
    @ManyToMany(targetEntity = Article.class)
    private List<Article> articles;

    public boolean isDefaultFooter() {
        return defaultFooter;
    }

    public void setDefaultFooter(boolean defaultFooter) {
        this.defaultFooter = defaultFooter;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the articles
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * @param articles the articles to set
     */
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
