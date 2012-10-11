package de.congrace.blog4j.service.api;

import de.congrace.blog4j.dao.api.ImageDao;
import de.congrace.blog4j.entities.Image;

public interface ImageService {

    public abstract void setImageDao(ImageDao imageDao);

    public abstract Image getImageById(long id);

    public abstract void saveImage(Image i);

    public abstract void deleteImage(Image i);

}