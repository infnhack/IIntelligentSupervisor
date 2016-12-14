package com.infinera.dao;

import java.util.List;

import com.infinera.model.AccessRecord;
import com.infinera.model.User;

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
}
