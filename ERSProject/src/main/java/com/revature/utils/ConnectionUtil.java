package com.revature.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	//This method will eventually return an object of type Connection, which we'll use to connect to our databse
	public static Connection getConnection() throws SQLException {
				
		//For compatibility with other technologies/frameworks, we'll need to register our PostgreSQL driver
        //This process makes the application aware of what Driver class we're using
        try {
            Class.forName("org.postgresql.Driver");
            //searching for the postgres driver, which we have as a dependency
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); //This tells in the console us what went wrong
            System.out.println("problem occurred locating driver");
        }

        //I'm going to put the credentials in Strings, and use those strings in a method that gets connections
        //String url = "jdbc:postgresql://localhost:5432/revature";
		String url = "jdbc:postgresql://demodb.cnqrywieybnf.us-east-1.rds.amazonaws.com/postgres?currentSchema=ers_one";

        String username = "postgres";
        String password = "password";

        //This return statement is what returns out actual database Connection object
        //Note how this getConnection() method has a return type of Connection
        return DriverManager.getConnection(url, username, password);
	}
}

