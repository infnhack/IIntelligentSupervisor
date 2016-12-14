package com.infinera.dao;

//import static org.junit.Assert.*;

import org.junit.Test;

import com.infinera.model.Admin;

public class AdminTest {

	@Test
	public void test() {
		Admin admin = new Admin();
		admin.setName("AAA");
		admin.setPassword("BBB");
		admin.setEmail("CCCC");
		  
		AdminDao dao = new AdminDao();
		dao.save(admin);
//		fail("Not yet implemented");
	}

}
