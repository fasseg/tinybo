package de.congrace.blog4j.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.congrace.blog4j.dao.api.ImageDao;
import de.congrace.blog4j.entities.Image;
import de.congrace.blog4j.service.api.ImageService;

@Service("imageService")
public class ImageServiceImpl implements ImageService {
    @Resource(name = "imageDao")
    private ImageDao imageDao;

    /* (non-Javadoc)
     * @see de.congrace.blog4j.service.ImageService#setImageDao(de.congrace.blog4j.dao.api.ImageDao)
     */
    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    /* (non-Javadoc)
     * @see de.congrace.blog4j.service.ImageService#getImageById(long)
     */
    @Transactional
    public Image getImageById(long id) {
        return imageDao.getImagebyId(id);
    }

    /* (non-Javadoc)
     * @see de.congrace.blog4j.service.ImageService#saveImage(de.congrace.blog4j.entities.Image)
     */
    @Transactional
    public void saveImage(Image i) {
        imageDao.saveImage(i);
    }

    @Override
    @Transactional
    public void deleteImage(Image i) {
        imageDao.deleteImage(i);

    }
}
