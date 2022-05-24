package com.revature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;
import com.revature.daos.ReimbursementDAO;
import com.revature.daos.UserDAO;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

public class ErsTest {

	ReimbursementDAO rDao = new ReimbursementDAO();
	UserDAO uDao = new UserDAO();

	@Test
	public void testAdd() {
		String str = "Junit is working fine";
		assertEquals("Junit is working fine", str);
	}

	// test database connection
	@Test
	public void testConnectionUtil() {
		try {
			Connection conn = ConnectionUtil.getConnection();
			assertNotNull(conn);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getAllUsers() {

		List<User> u = uDao.retrieveAllUsers();

		assertNotNull(u);

	}
	
	@Test
	public void getByUsernamePassword() {
		User u = uDao.findByUsernameAndPassword("rbasnet", "password");
		assertNotNull(u);
	}

	@Test
	public void getUser() {
		// assertNotNull(uDao.getUserbyId(1));

		User u = uDao.getUserbyId(1);
		assertNotNull(u);

	}

	@Test
	public void getReimbursement() {

		Reimbursement r = rDao.getReimbursementByID(1);
		assertNotNull(r);

	}

	@Test
	public void getReimbByUser() {
		List<Reimbursement> r = rDao.getReimbursementsByUser(2);
		assertNotNull(r);
	}

	@Test
	public void getAllReimb() {
		List<Reimbursement> r = rDao.retrieveAllReimbursements();
		assertNotNull(r);
	}
	
	@Test
	public void approveOrDeny() {
		
		boolean ad = rDao.approveDeny(1, 2, 1);
		assertNotNull(ad);
		return;
	}
	
	
}
