package com.libertymutual.goforcode.spark.app.controllers;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import static spark.Spark.notFound;

import java.util.Map;

import org.javalite.common.JsonHelper;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserApiController {

	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("/signUp", null);
		};
	
	
	public static Route create = (Request req, Response res)->{
		String json = req.body();
		Map map = JsonHelper.toMap(json);
		User user = new User();
		user.fromMap(map);
		
		try(AutoCloseableDb db = new AutoCloseableDb()){
			user.saveIt();
			res.status(201);
			return user.toJson(true);
		}
		
	};
}
