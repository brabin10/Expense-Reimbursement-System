package com.revature.controllers;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.models.User;

import io.javalin.http.Handler;
import io.jsonwebtoken.Jwts;
import java.util.*;

import java.security.Key;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.revature.controllers.LoginResponse;

public class UserController {
	//we need an UserService object to use it's login method
	UserService us = new UserService();
	
	public static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	//we need a loginHandler to handle login requests (which come to app.post("/login", xxx)
	public Handler loginHandler = (ctx) -> {
		
		//with POST requests, we have some data coming in, which we access with ctx.body();
		//body??? it means the BODY of the request... which is just the data the user sent.
		String body = ctx.body();
		
		//create a new GSON object to make Java <-> JSON conversions.
		Gson gson = new Gson();
		
		//remember, fromJson() is the method that takes JSON and turns it into some Java object
		User user = gson.fromJson(body, User.class);
		
		//control flow to determine what happens in the event of successful/unsuccessful login
		User loginedUser = us.login(user.getUsername(), user.getPassword());
		if(loginedUser != null) {
			ctx.status(202); //202 stands for "accepted"

			Date now = new Date();
        	Date expiration = new Date(now.getTime() + 1000 * 60 * 30);
        	String jws = Jwts.builder()
                .setSubject(String.valueOf(loginedUser.getId()))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(this.key)
                .compact();
			
			LoginResponse res = new LoginResponse(loginedUser.getRole(), jws);

			ctx.result(gson.toJson(res));
			
		} else {
			ctx.status(401); //401 stands for "unauthorized"
			System.out.println("heyo login failed");
		}
	};

	public Handler logout = (ctx) -> {
		ctx.sessionAttribute("currentUser", "");
		ctx.status(202);
	};
}
