package com.infinera.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static SessionFactory sessionFactory = null;
    
    static {
        Configuration config = new Configuration();
        config.configure();
        
        sessionFactory = config.buildSessionFactory();
    }
    
    /** get the global sessionFactory
     * @return
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    /** get a session from factory
     * @return
     */
    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
