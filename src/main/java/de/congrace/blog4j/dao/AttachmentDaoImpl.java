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
package de.congrace.blog4j.dao;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import de.congrace.blog4j.dao.api.AttachmentDao;
import de.congrace.blog4j.entities.Attachment;
import de.congrace.blog4j.entities.FileAttachment;
import de.congrace.blog4j.entities.URIAttachment;

/**
 * @author frank asseg, frank.asseg@congrace.de
 */
@SuppressWarnings("unchecked")
@Repository("attachmentDao")
public class AttachmentDaoImpl extends AbstractHibernateDaoImpl implements AttachmentDao {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentDaoImpl.class);

    public List<Attachment> getAllAttachments() {
        return this.getCurrentSession().createQuery("from Attachment a").list();
    }

    public List<Attachment> getAttachmentsByArticleId(long id) {
        return (List<Attachment>) this.getCurrentSession().createCriteria(Attachment.class).createCriteria("articles").add(Restrictions.eq("id", id)).list();
    }

    public Attachment getAttachmentById(long id) {
        return (Attachment) this.getCurrentSession().get(Attachment.class, id);
    }

    @Override
    public void removeOrphans() {
        for (Attachment a : getAllAttachments()) {
            long articleCount = (Long) this.getCurrentSession().createFilter(a.getArticles(), "select count(*)").uniqueResult();
            LOG.debug("attachment " + a.getId() + " has " + articleCount + " articles");
            if (articleCount == 0) {
                swipeOrphan(a);
            }
        }
    }

    private void swipeOrphan(Attachment a) {
        deleteObject(a);
    }

    public List<FileAttachment> getFileAttachmentsByArticleId(long id) {
        return (List<FileAttachment>) this.getCurrentSession().createCriteria(FileAttachment.class).createAlias("articles", "a").add(Restrictions.eq("a.id", id)).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
        //    	return (List<FileAttachment>) this.getCurrentSession().createCriteria(FileAttachment.class).createCriteria("articles").add(Restrictions.eq("id", id)).list();
        //    	return (List<FileAttachment>) this.getCurrentSession().createQuery("from FileAttachment f join f.articles a where a.id=?").setLong(0, id).list();
    }

    public List<URIAttachment> getURIAttachmentsByArticleId(long id) {
        return (List<URIAttachment>) this.getCurrentSession().createCriteria(URIAttachment.class).createAlias("articles", "a").add(Restrictions.eq("a.id", id)).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
        //    	return (List<URIAttachment>) this.getCurrentSession().createCriteria(URIAttachment.class).createCriteria("articles").add(Restrictions.eq("id", id)).list();
        //    	return (List<URIAttachment>) this.getCurrentSession().createQuery("from URIAttachment u join u.articles a where a.id=?").setLong(0, id).list();
    }
}
