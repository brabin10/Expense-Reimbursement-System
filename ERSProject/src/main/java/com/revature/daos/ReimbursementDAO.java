package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.utils.ConnectionUtil;

public class ReimbursementDAO implements ReimbursementDAOInterface {
	
	// method used to retrieve a reimbursement from the result set
		private Reimbursement getReimbursementFromResultSet(ResultSet rs) throws SQLException {
			
			// gets all fields from the table
			int id = rs.getInt("reimb_id");
			double amount = rs.getDouble("reimb_amount");
			Timestamp submitted = rs.getTimestamp("reimb_submitted");
			Timestamp resolved = rs.getTimestamp("reimb_resolved");
			String description = rs.getString("reimb_description");
			int author = rs.getInt("reimb_author");
			int resolver = rs.getInt("reimb_resolver");
			int statusId = rs.getInt("reimb_status_id");
			int typeId = rs.getInt("reimb_type_id");
			
			return new Reimbursement(id, amount, submitted, resolved, description, author, resolver, statusId, typeId);
		}
		
		@Override
		public List<Reimbursement> getReimbursementsByUser(int id) {
			
			List<Reimbursement> userReimbursements = new ArrayList<>();
			try(Connection conn = ConnectionUtil.getConnection()){
				
				String sql = "SELECT * FROM ers_reimbursement WHERE reimb_author = ? ORDER BY reimb_id;";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					int rId = rs.getInt("reimb_id");
					double amount = rs.getDouble("reimb_amount");
					Timestamp submitted = rs.getTimestamp("reimb_submitted");
					Timestamp resolved = rs.getTimestamp("reimb_resolved");
					String description = rs.getString("reimb_description");
					int authorId = rs.getInt("reimb_author");
					int resolverId = rs.getInt("reimb_resolver");
					int status = rs.getInt("reimb_status_id");
					int type = rs.getInt("reimb_type_id");
					
					userReimbursements.add(new Reimbursement(rId, amount, submitted, resolved, description, authorId, resolverId, status, type));
				}	
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
			return userReimbursements;
		}
			
	// method used to retrieve a reimbursement by ID
	@Override
	public Reimbursement getReimbursementByID(int id) {
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			String sql = "SELECT * FROM ers_reimbursement WHERE reimb_id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return getReimbursementFromResultSet(rs);
			}	
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	// method used to grab all of the reimbursements from the database
	@Override
	public List<Reimbursement> retrieveAllReimbursements() {
		

		List<Reimbursement> reimbursements = new ArrayList<>();
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			String sql = "SELECT * FROM ers_reimbursement ORDER BY reimb_id;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
						
			while(rs.next()) {
				int id = rs.getInt("reimb_id");
				double amount = rs.getDouble("reimb_amount");
				Timestamp submitted = rs.getTimestamp("reimb_submitted");
				Timestamp resolved = rs.getTimestamp("reimb_resolved");
				String description = rs.getString("reimb_description");
				int authorId = rs.getInt("reimb_author");
				int resolverId = rs.getInt("reimb_resolver");
				int status = rs.getInt("reimb_status_id");
				int type = rs.getInt("reimb_type_id");
				
				reimbursements.add(new Reimbursement(id, amount, submitted, resolved, description, authorId, resolverId, status, type));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return reimbursements;
	}
		
	// method used to add a reimbursement to the database
	@Override
	public int addReimbursement(Reimbursement rb) {
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			String sql = "INSERT INTO ers_reimbursement (reimb_amount, reimb_submitted, reimb_resolved, reimb_description,reimb_author,reimb_status_id,reimb_type_id)"
					+ "VALUES (?,?,?,?,?,?,?);";
			
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setDouble(1, rb.getAmount());
			ps.setTimestamp(2, rb.getSubmitted());
			ps.setTimestamp(3, rb.getResolved());
			ps.setString(4, rb.getDescription());
			if (rb.getAuthorId() == 0) {
				ps.setString(5, "");
			}
			else {
				ps.setInt(5, rb.getAuthorId());
			}
			ps.setInt(6, rb.getStatus());
			ps.setInt(7, rb.getType());
			
			ps.execute();
			
			return 1;
			// key used to return the new ID of the reimbursement after adding
//			ResultSet keys = ps.getGeneratedKeys();
//			if(keys.next()) {
//				System.out.println("successfully added the reimbursement");
//				return keys.getInt(1);
//			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
		
	
	// method used to change the status of the reimbursement after approval or denial
	@Override
	public boolean approveDeny(int status, int id, int resolverId) {

		try(Connection conn = ConnectionUtil.getConnection()) {
			
			String sql = "UPDATE ers_reimbursement SET reimb_status_id = ?, reimb_resolved = ?, reimb_resolver = ? WHERE reimb_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(4, id);
			
			ps.setInt(1, status);
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			ps.setInt(3, resolverId);

			ps.execute();
			return true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	
}