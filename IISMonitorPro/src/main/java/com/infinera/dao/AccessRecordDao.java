package com.infinera.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.sql.Timestamp;
import com.infinera.model.AccessRecord;

public class AccessRecordDao extends AbstractDao {
	public AccessRecordDao() {
		super();
	}

	public void create(AccessRecord ar) {
		super.saveOrUpdate(ar);
	}

	public void update(AccessRecord ar) {
		super.saveOrUpdate(ar);
	}

	public void delete(AccessRecord ar) {
		super.delete(ar);
	}

	public AccessRecord getById(int id) {
		return (AccessRecord) super.getById(AccessRecord.class, id);
	}

	public List<AccessRecord> findAll() {
		return (List<AccessRecord>) super.findAll(AccessRecord.class);
	}

	public List<AccessRecord> findByCriteria(int userId, String legal, Date begin, Date end) {
		Session session = HibernateUtils.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			// Method 2: orientation object
			Criteria criteria = session.createCriteria(AccessRecord.class);

			if (userId != -1) {
				criteria.add(Restrictions.eq("userId", userId));
			}

			if (legal.equals("Yes")) {
				criteria.add(Restrictions.eq("isLegal", 1));
			} else if (legal.equals("No")) {
				criteria.add(Restrictions.eq("isLegal", 0));
			}

			if (begin != null && end != null) {
				if (begin.before(end)) {
					criteria.add(Restrictions.between("timestamp", new Timestamp(begin.getTime()),
							new Timestamp(end.getTime())));
				} else {
					criteria.add(Restrictions.between("timestamp", new Timestamp(end.getTime()),
							new Timestamp(begin.getTime())));
//					criteria.add(Restrictions.between("timestamp", new Timestamp(begin.getTime()),
//							new Timestamp(end.getTime())));
				}
			} else if (begin != null) {
				criteria.add(Restrictions.ge("timestamp", new Timestamp(begin.getTime())));
			} else if (end != null) {
				criteria.add(Restrictions.le("timestamp", new Timestamp(end.getTime())));
			}
//			criteria.add(Restrictions.eq("id", 5));
			// criteria.add(Restrictions.between(propertyName, lo, hi))
			criteria.addOrder(Order.asc("id"));

			List<AccessRecord> list = (List<AccessRecord>) criteria.list();

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
}
