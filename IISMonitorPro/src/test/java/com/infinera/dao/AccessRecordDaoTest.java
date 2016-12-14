package com.infinera.dao;


import org.junit.Test;

import com.infinera.model.AccessRecord;
import com.infinera.model.User;

public class AccessRecordDaoTest {

	@Test
	public void test() {
		AccessRecordDao accessRecordDao = new AccessRecordDao();
		AccessRecord accessRecord = accessRecordDao.getById(1);
		
		
		System.out.println(accessRecord.getPhotoPath());
//		User user = accessRecord.getUser();
//		System.out.println(user.getName());
//		UserDao userDao = new UserDao();
//		
//		User user = userDao.getById(1);
		
		AccessRecord accessRecord2 = new AccessRecord(1, "c:\\1.jpg", 1);
		accessRecordDao.create(accessRecord2);
	}

}
