package de.congrace.blog4j.dao.api;

import java.util.List;

import de.congrace.blog4j.entities.Footer;

/**
 * @author Frank Asseg, frank.asseg@congrace.de
 */
public interface FooterDao extends BaseDao {

    public abstract List<Footer> getAllFooters();

    public abstract Footer getFooterById(long id);

    public abstract Footer getFooterByName(String name);

    public abstract void saveFooter(Footer f);

    public abstract Footer getDefaultFooter();

    public void resetDefaultFooter();
}