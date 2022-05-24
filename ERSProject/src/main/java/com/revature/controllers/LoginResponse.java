package com.revature.controllers;

public class LoginResponse {
	private int role_id;
	private String token;
	public LoginResponse(int role_id, String token) {
		this.role_id = role_id;
		this.token = token;
	}
}