package com.infinera.dao;

import java.util.List;

import org.hibernate.*;
import com.infinera.model.*;

public class AdminDao {
    public static void save(Admin Admin) {
        Session session = HibernateUtils.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.save(Admin);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();

                throw e;
            }
        } finally {
            session.close();
        }
        
    }

    public void update(Admin Admin) {
        Session session = HibernateUtils.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.update(Admin);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();

                throw e;
            }
        } finally {
            session.close();
        }
    }

    public static void delete(int id) {
        Session session = HibernateUtils.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.delete(session.get(Admin.class, id));

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();

                throw e;
            }
        } finally {
            session.close();
        }
    }

    public Admin getById(int id) {
        Session session = HibernateUtils.getSession();
        Transaction tx = null;
        Admin Admin = null;

        try {
            tx = session.beginTransaction();

            Admin = (Admin) session.get(Admin.class, id);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();

                throw e;
            }
        } finally {
            session.close();
        }

        return Admin;
    }
    
    public boolean isValidUser(String user, String password) {
    	List<Admin> list = findAll();
    	
    	for (Admin admin : list) {
    		if (admin.getName().equals(user) && admin.getPassword().equals(password)) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    @SuppressWarnings("unchecked")
    public List<Admin> findAll() {
        Session session = HibernateUtils.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            //Method 1:
            //List<Admin> list = (List<Admin>) session.createQuery("FROM Admin").list();

            
            //Method 2: orientation object
            Criteria criteria = session.createCriteria(Admin.class);
            
            //add filter
            //criteria.add(Restrictions.eq("id", 5));
            //criteria.addOrder(Order.asc("id"));
            
            List<Admin> list = (List<Admin>)criteria.list();
            
            tx.commit();

            return list;
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();

                throw e;
            }
        } finally {
            session.close();
        }
        
        return null;
    }

    @SuppressWarnings("unchecked")
    public QueryResult<Admin> findAll(int firstResult, int maxResults) {
        Session session = HibernateUtils.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            
            Query query = session.createQuery("FROM Admin");
            
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);

            List<Admin> list = (List<Admin>) query.list();
            
            Long count = (Long)session.createQuery("SELECT COUNT(*) FROM Admin").uniqueResult();

            tx.commit();

            return new QueryResult<Admin>(count.intValue(), list);
            
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();

                throw e;
            }
        } finally {
            session.close();
        }
        
        return null;
    }
}
