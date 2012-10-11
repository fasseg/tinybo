package de.congrace.blog4j.dao.api;

import de.congrace.blog4j.entities.Image;

public interface ImageDao {

    public abstract Image getImagebyId(long id);

    public abstract void saveImage(Image i);

    public abstract void deleteImage(Image i);

}