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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.congrace.blog4j.dao.api.AttachmentDao;
import de.congrace.blog4j.entities.Attachment;
import de.congrace.blog4j.entities.FileAttachment;
import de.congrace.blog4j.entities.URIAttachment;
import de.congrace.blog4j.service.api.AttachmentService;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@Service("attachmentService")
public class AttachmentServiceImpl implements AttachmentService, InitializingBean {

    @Resource(name = "attachmentDao")
    public AttachmentDao attachmentDao;

    public void setAttachmentDao(AttachmentDao attachmentDao) {
        this.attachmentDao = attachmentDao;
    }

    @Transactional
    public Attachment getAttachmentById(long id) {
        return attachmentDao.getAttachmentById(id);
    }

    @Transactional
    public void saveAttachment(Attachment a) {
        attachmentDao.saveOrUpdate(a);
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(attachmentDao);
    }

    @Override
    @Transactional
    public void cleanUpAttachments() {
        attachmentDao.removeOrphans();
    }

    @Transactional
    public List<FileAttachment> getFileAttachmentsByArticleId(long id) {
        return attachmentDao.getFileAttachmentsByArticleId(id);
    }

    @Transactional
    public List<URIAttachment> getURIAttachmentsByArticleId(long id) {
        return attachmentDao.getURIAttachmentsByArticleId(id);
    }
}
