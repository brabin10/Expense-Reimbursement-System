package com.revature;

import java.sql.Connection;
import java.sql.SQLException;

import com.revature.controllers.ReimbursementController;
import com.revature.controllers.UserController;
import com.revature.utils.ConnectionUtil;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Launcher {
	public static void main(String[] args) {
		// In this try/catch, we're just testing whether our Connection (from the
		// ConnectionUtil Class) is successful
		// remember - the getConnection() method will return a Connection Object if you
		// connect successfully
		try (Connection conn = ConnectionUtil.getConnection()) {
			System.out.println("CONNECTION SUCCESSFUL :)");
		} catch (SQLException e) { 
			// if creating this connection fails... catch the exception and print the stack
			// trace
			System.out.println("connection failed... :(");
			e.printStackTrace();
		}

		ReimbursementController reimbursementController = new ReimbursementController();
		UserController userController = new UserController();

		Javalin app = Javalin.create(config -> {
			config.enableCorsForAllOrigins(); // this allows us to process JS requests from anywhere
		}).start(9898); // we need this to start our application on port 3000


		app.before(ctx -> ctx.header("Access-Control-Allow-Credentials", "true"));
		//We need to make some endpoint handlers, which will take in requests and send them where they need to go
		//Javalin endpoint handlers are like the traffic cop to your server. They take traffic and direct it.

		//handler ending in /employees that takes in GET requests - will return all employees
		//the app.get() method takes in a URL endpoint, and a place in the server to send the request to
		app.get("/reimbursements", reimbursementController.getAllReimbHandler);
		app.post("/reimbursements/create", reimbursementController.create);
		app.post("/reimbursements/approve_deny", reimbursementController.approve_deny);
		//handler ending in /login that takes in POST requests -will validate user login
		//the app.post() method takes in URL endpoint, add a place in the server to send the request to
		app.post("/login", userController.loginHandler);
		app.post("/logout", userController.logout);
		
	}
}
