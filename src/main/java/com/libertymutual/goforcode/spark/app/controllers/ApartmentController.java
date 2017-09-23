package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.ApartmentsUsers;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentController {

	public static final Route details = (Request req, Response res) -> {
		String idAsString = req.params("id");
		int id = Integer.parseInt(idAsString);
		boolean currentUserIsLister = false;
		boolean currentUserIsLiker = false;

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(id);
			User currentUser = req.session().attribute("currentUser");
			List<Apartment> apartments = ApartmentsUsers.where("apartment_id = ?", id);
			User owner = apartment.parent(User.class);
						
			if (currentUser != null) {
				List<User> likers = apartment.get(User.class, "user_id = ?", currentUser.getId());
				if (currentUser.getId().equals(owner.getId())) {
					currentUserIsLister = true;
				}
				if(likers.size()==0) {
					currentUserIsLiker = false;
				} else {
					currentUserIsLiker = true;
				}
			}
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			model.put("apartment", apartment);
			model.put("owner", currentUserIsLister);
			model.put("numOfLikes", apartments.size());
			model.put("notOwner", currentUserIsLister == false);
			model.put("liker", currentUserIsLiker);
			model.put("notLiker", !currentUserIsLiker);
			model.put("apartmentIsActive", apartment.getIsActive());
			model.put("apartmentIsInactive", !apartment.getIsActive());
			return MustacheRenderer.getInstance().render("apartment/detail.html", model);
		}
	};
	public static final Route newForm = (Request req, Response res)->{
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser")==null);
		model.put("returnPath", req.queryParams("returnPath"));
		return MustacheRenderer.getInstance().render("apartment/newForm.html", model);
	};
	public static final Route create = (Request req, Response res)->{
//		String encryptedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
		try (AutoCloseableDb db = new AutoCloseableDb()) {
		Apartment apartment = new Apartment(
				Integer.parseInt(req.queryParams("rent")),
				Integer.parseInt(req.queryParams("number_of_bedrooms")),
				Double.parseDouble(req.queryParams("number_of_bathrooms")),
				Integer.parseInt(req.queryParams("square_footage")),
				req.queryParams("address"),
				req.queryParams("city"),
				req.queryParams("state"),	
				req.queryParams("zip_code"),
				Integer.parseInt(req.queryParams("number_of_likes")),
				Boolean.parseBoolean("is_active")
				
			);
			apartment.set("is_active", true);
			apartment.saveIt();
			User user = req.session().attribute("currentUser");
			user.add(apartment);
			res.redirect("/");
			return "";
		}
	};

	public static final Route index = (Request req, Response res)->{
		User currentUser = req.session().attribute("currentUser");
		long id = (long) currentUser.getId();
		 try (AutoCloseableDb db = new AutoCloseableDb()){
			 
			 List<Apartment> activeApartment = Apartment.where("is_active = ? and user_id = ?", true, id);
			 List<Apartment> inactiveApartment = Apartment.where("is_active = ? and user_id = ?", false, id);

			 Map<String, Object> model = new HashMap<String, Object>();
			 model.put("currentUser", req.session().attribute("currentUser"));
			 model.put("activeApartments", activeApartment);
			 model.put("inactiveApartments", inactiveApartment);

			 return MustacheRenderer.getInstance().render("apartment/index.html", model);
		 }
	};
	
	public static final Route likes = (Request req, Response res) -> {
		String idAsString = req.params("id");
		int id = Integer.parseInt(idAsString);
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(id);
			User currentUser = req.session().attribute("currentUser");
			apartment.add(currentUser);
			res.redirect("/apartments/" + id);
			return "";
		}
	};
	
	public static final Route activate = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			int id = Integer.parseInt(req.params("id"));
			User currentUser = req.session().attribute("currentUser");
			Apartment apartment = Apartment.findById(id);
			apartment.set("is_active", true);
			apartment.saveIt();
			res.redirect("/apartments/" + id);
			return "";
		}
	};

	public static final Route deactivate = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			int id = Integer.parseInt(req.params("id"));
			User currentUser = req.session().attribute("currentUser");
			Apartment apartment = Apartment.findById(id);
			apartment.set("is_active", false);
			apartment.saveIt();
			res.redirect("/apartments/" + id);
			return "";
		}
	};
}
