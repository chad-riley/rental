package com.libertymutual.goforcode.spark.app;

import static spark.Spark.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.app.controllers.HomeController;
import com.libertymutual.goforcode.spark.app.controllers.SessionController;
import com.libertymutual.goforcode.spark.app.controllers.UserController;
import com.libertymutual.goforcode.spark.app.filters.SecurityFilters;
import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;

public class Application {

	public static void main(String[] args) {

		String encryptedPassword = BCrypt.hashpw("chad", BCrypt.gensalt());

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			User chad = new User("chad@chad.com", encryptedPassword, "Chad", "Riley");
			chad.saveIt();

			Apartment.deleteAll();
			
			Apartment apartment = new Apartment(5000, 1, 0, 350, "123 Main St.", "San Francisco", "CA", "95125",0, true);
			chad.add(apartment);
			apartment.saveIt();
			
			apartment = new Apartment(1100, 5, 6, 2350, "123 Cowboy St.", "Houston", "TX", "77006",0, false);
			chad.add(apartment);
			apartment.saveIt();	
		}

		path("/apartments", () -> {
			before("/new", SecurityFilters.isAuthenticated);
			get("/new", ApartmentController.newForm);
			
			before("/mine", SecurityFilters.isAuthenticated);
			get("/mine", ApartmentController.index);
			get("/:id", ApartmentController.details);

			
			before("", SecurityFilters.isAuthenticated);
			post("", ApartmentController.create);
			
			before("/:id/like", SecurityFilters.isAuthenticated);
			post("/:id/like", ApartmentController.likes);
			
			before("/:id/activations", SecurityFilters.isAuthenticated);
			post("/:id/activations", ApartmentController.activate);
			
			before("/:id/deactvations", SecurityFilters.isAuthenticated);
			post("/:id/deactivations", ApartmentController.deactivate);
		});

		get("/apartments/:id", ApartmentController.details);
		get("/", HomeController.index);

		get("/login", SessionController.newForm);
		post("/login", SessionController.create);
		post("/logout", SessionController.destroy);
		
		post("/users", UserController.create);
		get("/users/new", UserController.newForm);
		

		path("/api", () -> {
			get("/apartments/:id", ApartmentApiController.details);
			post("/apartments", ApartmentApiController.create);

		});
	}
}
