package de.congrace.blog4j.forms;

import java.util.List;

/**
 * @author Frank Asseg, frank.asseg@congrace.de
 */
public class FooterCommand extends IdCommand {
    private String name;
    private List<Long> articleIds;
    private boolean defaultFooter = false;

    public boolean isDefaultFooter() {
        return defaultFooter;
    }

    public void setDefaultFooter(boolean defaultFooter) {
        this.defaultFooter = defaultFooter;
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
     * @return the articleIds
     */
    public List<Long> getArticleIds() {
        return articleIds;
    }

    /**
     * @param articleIds the articleIds to set
     */
    public void setArticleIds(List<Long> articleIds) {
        this.articleIds = articleIds;
    }

}
