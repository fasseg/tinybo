package de.congrace.blog4j.listener;

import java.beans.Introspector;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * listens for context destroyed event and unloads the JDBCDrivers to prevent a memory leak in tomcat written by Guillaume Poirier
 */
public class CleanupListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(CleanupListener.class);

    public void contextInitialized(ServletContextEvent event) {
    }

    public void contextDestroyed(ServletContextEvent event) {
        try {
            Introspector.flushCaches();
            for (Enumeration e = DriverManager.getDrivers(); e.hasMoreElements();) {
                Driver driver = (Driver) e.nextElement();
                if (driver.getClass().getClassLoader() == getClass().getClassLoader()) {
                    DriverManager.deregisterDriver(driver);
                    LOG.debug("deregistering driver: " + driver.getClass().getName());
                }
            }
        } catch (Throwable e) {
            LOG.error("Failled to cleanup ClassLoader for webapp", e);
        }
    }
}