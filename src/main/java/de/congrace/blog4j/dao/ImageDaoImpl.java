package de.congrace.blog4j.dao;

import org.springframework.stereotype.Repository;

import de.congrace.blog4j.dao.api.ImageDao;
import de.congrace.blog4j.entities.Image;

@Repository("imageDao")
public class ImageDaoImpl extends AbstractHibernateDaoImpl implements ImageDao {
    /* (non-Javadoc)
     * @see de.congrace.blog4j.dao.ImageDao#getImagebyId(long)
     */
    public Image getImagebyId(long id) {
        return (Image) this.getCurrentSession().get(Image.class, id);
    }

    /* (non-Javadoc)
     * @see de.congrace.blog4j.dao.ImageDao#saveImage(de.congrace.blog4j.entities.Image)
     */
    public void saveImage(Image i) {
        saveOrUpdate(i);
    }

    @Override
    public void deleteImage(Image i) {
        this.getCurrentSession().delete(i);
    }
}
