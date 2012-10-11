/*
 *  Copyright 2010 frank asseg.
 *  frank.asseg@congrace.de
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package de.congrace.blog4j.forms;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
public class ArticleCommand extends IdCommand {

    private String title;
    private String msg;
    private String teaser;
    private List<String> categoryIds;
    private String tags;
    private boolean robotIndex;
    private boolean robotFollow;
    private boolean enabled;
    private CommonsMultipartFile image;
    private String existingImageUrl;
    private String imageAlignment;

    public String getImageAlignment() {
        return imageAlignment;
    }

    public void setImageAlignment(String imageAlignment) {
        this.imageAlignment = imageAlignment;
    }

    public String getExistingImageUrl() {
        return existingImageUrl;
    }

    public void setExistingImageUrl(String existingImageUrl) {
        this.existingImageUrl = existingImageUrl;
    }

    public CommonsMultipartFile getImage() {
        return image;
    }

    public void setImage(CommonsMultipartFile image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isRobotFollow() {
        return robotFollow;
    }

    public void setRobotFollow(boolean robotFollow) {
        this.robotFollow = robotFollow;
    }

    public boolean isRobotIndex() {
        return robotIndex;
    }

    public void setRobotIndex(boolean robotIndex) {
        this.robotIndex = robotIndex;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
