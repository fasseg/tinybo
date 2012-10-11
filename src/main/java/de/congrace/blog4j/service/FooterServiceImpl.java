package de.congrace.blog4j.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.congrace.blog4j.dao.api.FooterDao;
import de.congrace.blog4j.entities.Footer;
import de.congrace.blog4j.service.api.FooterService;

/**
 * @author Frank Asseg, frank.asseg@congrace.de
 */
@Service("footerService")
public class FooterServiceImpl implements FooterService {

    @Resource(name = "footerDao")
    private FooterDao footerDao;

    @Transactional
    public List<Footer> getAllFooters() {
        return footerDao.getAllFooters();
    }

    @Transactional
    public Footer getFooterById(long id) {
        return footerDao.getFooterById(id);
    }

    @Transactional
    public Footer getFooterByName(String name) {
        return footerDao.getFooterByName(name);
    }

    @Transactional
    public void saveFooter(Footer f) {
        if (f.isDefaultFooter()) {
            footerDao.resetDefaultFooter();
        }
        footerDao.saveFooter(f);
    }

    @Override
    @Transactional
    public Footer getDefaultFooter() {
        return footerDao.getDefaultFooter();
    }
}
