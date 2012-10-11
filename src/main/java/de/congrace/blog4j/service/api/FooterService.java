package de.congrace.blog4j.service.api;

import java.util.List;

import de.congrace.blog4j.entities.Footer;

/**
 * @author Frank Asseg, frank.asseg@congrace.de
 */
public interface FooterService {

    public abstract List<Footer> getAllFooters();

    public abstract Footer getFooterById(long id);

    public abstract Footer getFooterByName(String name);

    public abstract void saveFooter(Footer f);

    public abstract Footer getDefaultFooter();

}