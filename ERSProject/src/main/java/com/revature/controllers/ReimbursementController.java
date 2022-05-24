package com.revature.controllers;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import com.revature.models.Reimbursement;
import com.revature.services.ReimbursementService;
import java.sql.Timestamp;

import io.javalin.http.Handler;

import com.revature.controllers.UserController;
import io.jsonwebtoken.Jwts;

public class ReimbursementController {

	ReimbursementService rs = new ReimbursementService();
	
	//this Handler will get the HTTP GET request for all employees, and send back the employees from the database
	public Handler getAllReimbHandler = (ctx) -> {
		try {
			String userId = Jwts.parserBuilder().setSigningKey(UserController.key).build().parseClaimsJws(ctx.header("Authorization")).getBody().getSubject();
		
			//we need an ArrayList of Reimbursement objects (which we'll get from the service layer)
			List<Reimbursement> reimbursements = rs.getAllReimb();

			//create a GSON object to convert our Java object into JSON (since we can only transfer JSON, not Java)
			Gson gson = new Gson();
		
			//use the JSON .toJson() method to turn our Java into JSON
			String JSONReimbursements = gson.toJson(reimbursements);
			//Give a HTTP response containing our JSON string back to the webpage (or wherever the HTTP request came from)
			ctx.result(JSONReimbursements); //.result() sends a response of data back
			ctx.status(200); //.status() sets the HTTP status code. 200 stands for "OK"
		}
		catch(Exception e) {
			ctx.status(401);
		}
	};

	public Handler create = (ctx) -> {
		try {
			String userId = Jwts.parserBuilder().setSigningKey(UserController.key).build().parseClaimsJws(ctx.header("Authorization")).getBody().getSubject();
			int currentUser = Integer.parseInt(userId);

			GsonBuilder gsonBuilder = new GsonBuilder();

			JsonDeserializer<Reimbursement> deserializer = new JsonDeserializer<Reimbursement>() {  
			    @Override
			    public Reimbursement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			        JsonObject jsonObject = json.getAsJsonObject();

			        Timestamp submitted = new Timestamp(jsonObject.get("submitted").getAsLong());

			        Timestamp resolved = jsonObject.get("resolved").isJsonNull() ? null : new Timestamp(jsonObject.get("resolved").getAsLong());

			        return new Reimbursement(
		                jsonObject.get("amount").getAsDouble(),
		                submitted,
		                resolved,
		                jsonObject.get("description").getAsString(),
		                currentUser,
		                jsonObject.get("resolverId").isJsonNull() ? 0 : jsonObject.get("resolverId").getAsInt(),
		                jsonObject.get("status").getAsInt(),
		                jsonObject.get("type").getAsInt()
			        );
			    }
			};
	 
			gsonBuilder.registerTypeAdapter(Reimbursement.class, deserializer);

			Gson customGson = gsonBuilder.create();  
			Reimbursement re = customGson.fromJson(ctx.body(), Reimbursement.class); 

			
			int result = rs.add(re);

			if (result != 0) {
				ctx.status(200);
			}
			else {
				ctx.status(400);
			}
		}
		catch(Exception e) {
			ctx.status(401);
		}
		
	};

	public Handler approve_deny = (ctx) -> {
		try {
			String userId = Jwts.parserBuilder().setSigningKey(UserController.key).build().parseClaimsJws(ctx.header("Authorization")).getBody().getSubject();
			int currentUser = Integer.parseInt(userId);

			Gson gson = new Gson();
		
			Reimbursement rb = gson.fromJson(ctx.body(), Reimbursement.class);
			if (rs.statusChange(rb, rb.getStatus(), currentUser)) {
				ctx.status(200);
			}
			else {
				ctx.status(400);
			}
		}
		catch(Exception e) {
			ctx.status(401);
		}
		
	};
}
