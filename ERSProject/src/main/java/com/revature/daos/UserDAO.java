package com.revature.daos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

public class UserDAO implements UserDAOInterface {
	
	// grabs a user from a result set returned by running a SQL statement
		private User getUserFromResultSet(ResultSet rs) throws SQLException{
			
			int id = rs.getInt("ers_users_id");
			String username = rs.getString("ers_username");
			String password = rs.getString("ers_password");
			String fname = rs.getString("user_first_name");
			String lname = rs.getString("user_last_name");
			String email = rs.getString("user_email");
			int roleId = rs.getInt("user_role_id");
		
			return new User(id,username,password,fname,lname,email,roleId);
		}

	// returns a user from the database where id is defined
	@Override
	public User getUserbyId(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			
			String sql = "SELECT * FROM ers_users WHERE ers_users_id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return getUserFromResultSet(rs);
			}	
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	// gets all users from database and return them in arrayList
	@Override
	public List<User> retrieveAllUsers() {
		
		List<User> users = new ArrayList<>();
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			String sql = "SELECT * FROM ers_users;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("ers_users_id");
				String username = rs.getString("ers_username");
				String password = rs.getString("ers_password");
				String fname = rs.getString("user_first_name");
				String lname = rs.getString("user_last_name");
				String email = rs.getString("user_email");
				int roleId = rs.getInt("user_role_id");
				
				users.add(new User(id,username,password,fname,lname,email,roleId));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	// adds a user to the database and returns the ID of the new user
	@Override
	public int addUser(User u) {
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			String sql = "INSERT INTO ers_users (ers_username,ers_password,user_first_name,user_last_name,user_email,user_role_id)"
					+ "VALUES (?,?,?,?,?,?);";
			
			String[] keyNames = {"ers_users_id"};
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getFirstName());
			ps.setString(4, u.getLastName());
			ps.setString(5, u.getEmail());
			ps.setInt(6, u.getRole());
			
			ps.executeQuery();
			
			// grabs the ID from the query and returns it to the database
			ResultSet keys = ps.getGeneratedKeys();
			if(keys.next()) {
				System.out.println("successfully added the user");
				return keys.getInt(1);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// finds a user by username and password and returns that user from the database
	@Override
	public User findByUsernameAndPassword(String username, String password) {
	
		try(Connection conn = ConnectionUtil.getConnection()){
			
			String sql = "SELECT * FROM ers_users WHERE ers_username = ? AND ers_password = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				User u = new User();
				u.setUsername(username);
				u.setPassword(password);
				u.setEmail(rs.getString("user_email"));
				u.setFirstName(rs.getString("user_first_name"));
				u.setLastName(rs.getString("user_last_name"));
				u.setId(rs.getInt("ers_users_id"));
				u.setRole(rs.getInt("user_role_id"));
				
				System.out.println(u);
				return u;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
