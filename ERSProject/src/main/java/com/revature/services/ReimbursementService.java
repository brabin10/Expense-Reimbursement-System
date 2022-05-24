package com.revature.services;
import java.util.List;

import com.revature.daos.ReimbursementDAO;
import com.revature.models.Reimbursement;

public class ReimbursementService {
	
	ReimbursementDAO rd = new ReimbursementDAO();
	
	// service used to add reimbursements to the database
	// calls the DAO method for adding
	public int add(Reimbursement r) {
		return rd.addReimbursement(r);
	}
	
	// service used to change the status of a reimbursement,
	// calls the DAO method approveDeny
	public boolean statusChange(Reimbursement r, int status, int resolverId) {
		return rd.approveDeny(status, r.getId(), resolverId);
	}

	// service to return all the reimbursements in the database
	// calls the DAO method retrieveAllReimbursements
	public List<Reimbursement> getAllReimb() {
		return rd.retrieveAllReimbursements();
	}
	
	// service to return the reimbursements submitted by a single user
	// calls the DAO method getReimbursementsByUser
	public List<Reimbursement> getByUser(int id) {
		return rd.getReimbursementsByUser(id);
	}
}
