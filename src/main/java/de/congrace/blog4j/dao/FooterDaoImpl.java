package de.congrace.blog4j.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import de.congrace.blog4j.dao.api.FooterDao;
import de.congrace.blog4j.entities.Footer;

/**
 * @author Frank Asseg, frank.asseg@congrace.de
 */
@SuppressWarnings("unchecked")
@Repository("footerDao")
public class FooterDaoImpl extends AbstractHibernateDaoImpl implements FooterDao {
    public List<Footer> getAllFooters() {
        return this.getCurrentSession().createCriteria(Footer.class).list();
    }

    public Footer getFooterById(long id) {
        return (Footer) this.getCurrentSession().get(Footer.class, id);
    }

    public Footer getFooterByName(String name) {
        return (Footer) this.getCurrentSession().createCriteria(Footer.class).add(Restrictions.eq("name", name)).uniqueResult();
    }

    public void saveFooter(Footer f) {
        saveOrUpdate(f);
    }

    public void resetDefaultFooter() {
        this.getCurrentSession().createSQLQuery("update b4j_footers set footer_default=false").executeUpdate();
    }

    public Footer getDefaultFooter() {
        return (Footer) this.getCurrentSearchSession().createCriteria(Footer.class).add(Restrictions.eq("defaultFooter", true)).uniqueResult();
    }
}
