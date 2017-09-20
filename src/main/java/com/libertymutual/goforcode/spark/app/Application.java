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

		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			new User("chad@coolguy.com", encryptedPassword, "Chad", "Riley").saveIt();

			Apartment.deleteAll();
			new Apartment(5000, 1, 0, 350, "123 Main St.", "San Francisco", "CA", "95125").saveIt();
			new Apartment(1100, 5, 6, 2350, "123 Cowboy St.", "Houston", "TX", "77006").saveIt();
			new Apartment(2500, 3, 3, 1050, "4820 Puget Blvd SW", "Seattle", "WA", "98106").saveIt();
		}

		path("/apartments", () -> {
			before("/new", SecurityFilters.isAuthenticated);

			get("/new", ApartmentController.newForm);
			get("/:id", ApartmentController.details);
			before("", SecurityFilters.isAuthenticated);
			post("", ApartmentController.create);
		});

		get("/apartments/:id", ApartmentController.details);
		get("/", HomeController.index);

		get("/login", SessionController.newForm);
		post("/login", SessionController.create);
		get("/logout", SessionController.destroy);
		get("/signUp", UserController.newForm);
		post("/signUp", UserController.create);

		path("/api", () -> {
			get("/apartments/:id", ApartmentApiController.details);
			post("/apartments", ApartmentApiController.create);

		});
	}
}
