package com.revature.services;

import com.revature.daos.UserDAO;
import com.revature.models.User;

public class UserService {

	UserDAO ud = new UserDAO();

	// logs a user into a system
	public User login(String username, String password) {

		User u = ud.findByUsernameAndPassword(username, password);
		if (u != null) {
			if (u.getPassword().equals(password)) {
				return u;
			}
		}
		
		return null;
	}
}
