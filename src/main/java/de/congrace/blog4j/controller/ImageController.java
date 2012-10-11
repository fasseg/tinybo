package de.congrace.blog4j.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.congrace.blog4j.entities.Image;
import de.congrace.blog4j.service.api.ImageService;

@Controller
public class ImageController {
    @Resource(name = "imageService")
    private ImageService imageService;

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
    public void viewImage(HttpServletResponse resp, @PathVariable long id) throws IOException {
        Image img = imageService.getImageById(id);
        resp.getOutputStream().write(img.getData());
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }

}
