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
package de.congrace.blog4j.controller.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration holder for tinybo pages
 * @author frank asseg, frank.asseg@congrace.de
 */
public class PageConfig {
    private static PageConfig instance;
    
    private static final Logger LOG = LoggerFactory.getLogger(PageConfig.class);
    private static Properties props = null;
    private int page = 1;
    private boolean lastPage = false;
    private String title;
    private String robots;
    private String keywords;
    private String basePath;
    private String webmasterMail;
    private String webmasterName;
    private String contextPath;
    private String author;
    private String pageHead;
    private String pageSubHead;
    private final String hibernateDriver;
    private final String hibernateDialect;
    private final String hibernateShowSql;
    private final String hibernateHbm2Ddl;
    private final String hibernateIndex;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPass;
    private final int dbPoolMinSize;
    private final int dbPoolMaxSize;
    private final long dbPoolTimeout;
    private final boolean dbPoolCheckConnection;
    private final String theme;
    private final String introduction;

    /**
     * Create a new {@link PageConfig} from the configuration file "tinybo.properties" which is expected in the classpath
     */
    public PageConfig() {
        if (props == null) {
            props = new Properties();
            try {
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("tinybo.properties");
                props.load(in);
            } catch (IOException ex) {
                LOG.error("Properties could not be read", ex);
            }
        }
        this.title=props.getProperty("blog4j.global.pagetitle");
        this.basePath=props.getProperty("blog4j.global.basepath");
        this.robots=props.getProperty("blog4j.global.robots");
        this.keywords=props.getProperty("blog4j.global.keywords");
        this.webmasterMail=props.getProperty("blog4j.global.webmaster.mail");
        this.webmasterName=props.getProperty("blog4j.global.webmaster.name");
        this.contextPath=props.getProperty("blog4j.global.context");
        this.author=props.getProperty("blog4j.global.author");
        this.pageHead=props.getProperty("blog4j.global.head");
        this.pageSubHead=props.getProperty("blog4j.global.subhead");
        this.hibernateDriver=props.getProperty("blog4j.hibernate.driver");
        this.hibernateDialect=props.getProperty("blog4j.hibernate.dialect");
        this.hibernateHbm2Ddl = props.getProperty("blog4j.hibernate.hbm2ddl");
        this.hibernateShowSql=props.getProperty("blog4j.hibernate.showsql");
        this.hibernateIndex=props.getProperty("blog4j.hibernate.search.indexBase");
        this.dbUrl=props.getProperty("blog4j.hibernate.db.url");
        this.dbUser=props.getProperty("blog4j.hibernate.db.user");
        this.dbPass=props.getProperty("blog4j.hibernate.db.passwd");
        this.dbPoolMinSize=Integer.parseInt(props.getProperty("blog4j.hibernate.pool.size.min"));
        this.dbPoolMaxSize=Integer.parseInt(props.getProperty("blog4j.hibernate.pool.size.max"));
        this.dbPoolTimeout=Integer.parseInt(props.getProperty("blog4j.hibernate.pool.timeout"));
        this.dbPoolCheckConnection=Boolean.parseBoolean(props.getProperty("blog4j.hibernate.pool.checkconnection"));
        this.theme=props.getProperty("blog4j.global.theme");
        this.introduction=props.getProperty("blog4j.global.introduction");
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getTheme() {
        return theme;
    }

    public String getPageHead() {
        return pageHead;
    }

    public String getPageSubHead() {
        return pageSubHead;
    }

    public String getAuthor() {
        return author;
    }

    public String getWebmasterName() {
        return webmasterName;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getKeywords() {
        return keywords;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public int getPage() {
        return page;
    }

    public String getRobots() {
        return robots;
    }

    public String getTitle() {
        return title;
    }

    public String getWebmasterMail() {
        return webmasterMail;
    }

    public String getHibernateDriver() {
        return hibernateDriver;
    }

    public String getHibernateDialect() {
        return hibernateDialect;
    }

    public String getHibernateShowSql() {
        return hibernateShowSql;
    }

    public String getHibernateHbm2Ddl() {
        return hibernateHbm2Ddl;
    }

    public String getHibernateIndex() {
        return hibernateIndex;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public int getDbPoolMinSize() {
        return dbPoolMinSize;
    }

    public int getDbPoolMaxSize() {
        return dbPoolMaxSize;
    }

    public long getDbPoolTimeout() {
        return dbPoolTimeout;
    }

    public boolean isDbPoolCheckConnection() {
        return dbPoolCheckConnection;
    }

    public PageConfig setPage(int page) {
        this.page=page;
        return this;
    }

    public PageConfig setLastPage(boolean lastPage) {
        this.lastPage=lastPage;
        return this;
    }

    public PageConfig setTitle(String title) {
        this.title=title;
        return this;
    }

    public PageConfig setRobots(String robots) {
        this.robots=robots;
        return this;
    }

    public PageConfig setKeywords(String keywords) {
        this.keywords=keywords;
        return this;
    }

    public PageConfig setBasePath(String basePath) {
        this.basePath=basePath;
        return this;
    }

    public PageConfig setWebmasterMail(String webmasterMail) {
        this.webmasterMail=webmasterMail;
        return this;
    }

    public PageConfig setWebmasterName(String webmasterName) {
        this.webmasterName=webmasterName;
        return this;
    }

    public PageConfig setContextPath(String contextPath) {
        this.contextPath=contextPath;
        return this;
    }

    public PageConfig setAuthor(String author) {
        this.author=author;
        return this;
    }

    public PageConfig setPageHead(String pageHead) {
        this.pageHead=pageHead;
        return this;
    }

    public PageConfig setPageSubHead(String pageSubHead) {
        this.pageSubHead=pageSubHead;
        return this;
    }

    /**
     * save the configuration to the properties file
     */
    public void save() throws IOException {
        props.setProperty("blog4j.global.pagetitle", this.title);
        props.setProperty("blog4j.global.basepath", this.basePath);
        props.setProperty("blog4j.global.robots", this.robots);
        props.setProperty("blog4j.global.keywords", this.keywords);
        props.setProperty("blog4j.global.webmaster.mail", this.webmasterMail);
        props.setProperty("blog4j.global.webmaster.name", this.webmasterName);
        props.setProperty("blog4j.global.context", this.contextPath);
        props.setProperty("blog4j.global.author", this.author);
        props.setProperty("blog4j.global.head", this.pageHead);
        props.setProperty("blog4j.global.subhead", this.pageSubHead);
        props.setProperty("blog4j.hibernate.driver", this.hibernateDriver);
        props.setProperty("blog4j.hibernate.dialect", this.hibernateDialect);
        props.setProperty("blog4j.hibernate.hbm2ddl", this.hibernateHbm2Ddl);
        props.setProperty("blog4j.hibernate.showsql", this.hibernateShowSql);
        props.setProperty("blog4j.hibernate.search.indexBase", this.hibernateIndex);
        props.setProperty("blog4j.hibernate.db.url", this.dbUrl);
        props.setProperty("blog4j.hibernate.db.user", this.dbUser);
        props.setProperty("blog4j.hibernate.db.passwd", this.dbPass);
        props.setProperty("blog4j.hibernate.pool.size.min", String.valueOf(this.dbPoolMinSize));
        props.setProperty("blog4j.hibernate.pool.size.max", String.valueOf(this.dbPoolMaxSize));
        props.setProperty("blog4j.hibernate.pool.timeout", String.valueOf(this.dbPoolTimeout));
        props.setProperty("blog4j.hibernate.pool.checkconnection", String.valueOf(this.dbPoolCheckConnection));
        props.setProperty("blog4j.global.theme", theme);
        props.setProperty("blog4j.global.introduction", this.introduction);
        File f = new File("webapps/tinybo/WEB-INF/classes/tinybo.properties");
        LOG.debug("saving settings to: " + f.getAbsolutePath());
        props.store(new FileOutputStream(f), "last change: " + new SimpleDateFormat("d.M.y hh:mm").format(new Date()));
    }
    public static PageConfig getDefaultInstance(){
        if (instance==null){
            instance=new PageConfig();
        }
        return instance;
    }
}
