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
package de.congrace.blog4j.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.congrace.blog4j.dao.api.TagDao;
import de.congrace.blog4j.entities.Tag;
import de.congrace.blog4j.service.api.TagService;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Service("tagService")
public class TagServiceImpl implements TagService {

    @Resource(name = "tagDao")
    public TagDao tagDao;

    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Transactional
    public Tag getTagById(long id) {
        return tagDao.getTagById(id);
    }

    @Transactional
    public Tag getTagByName(String name) {
        return getTagByName(name, false);
    }

    @Transactional
    public Tag getTagByName(String name, boolean create) {
        Tag t = tagDao.getTagByName(name);
        if (create && t == null) {
            t = new Tag();
            t.setName(name);
            saveTag(t);
        }
        return t;
    }

    @Transactional
    public List<Tag> getAllTags() {
        return tagDao.getAllTags();
    }

    @Transactional
    public void saveTag(Tag t) {
        tagDao.saveOrUpdate(t);
    }

    @Transactional()
    public void updateArticleCount() {
        tagDao.updateArticleCount();
    }

    @Transactional
    public List<Tag> getImportantTags(int count) {
        return tagDao.getTagByArticleCount(count);
    }

}
