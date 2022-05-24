package com.revature.daos;

import java.util.List;

import com.revature.models.Reimbursement;

public interface ReimbursementDAOInterface {
	
	// gets reimbursement from database by ID
		Reimbursement getReimbursementByID(int id);
		
		// gets all reimbursements from database
		List<Reimbursement> retrieveAllReimbursements();
		
		// gets all reimbursements from database by a specific user
		List<Reimbursement> getReimbursementsByUser(int id);
		
		// adds a new reimbursement to the database
		int addReimbursement(Reimbursement rb);
		
		// approve or deny a specific reimbursement by ID and sets resolver
		boolean approveDeny(int status, int id, int resolverId);

}
		//Interface used to define the methods for handling reimbursements in database for easy transition between databases
 		