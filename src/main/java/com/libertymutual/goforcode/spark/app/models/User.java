package com.libertymutual.goforcode.spark.app.models;

import org.javalite.activejdbc.Model;

public class User extends Model{

	public User() {}

	public User(String email, String password, String first_name, String last_name) {
		setEmail(email);
		setPassword(password);
		setFirstName(first_name);
		setLastName(last_name);
	}

	public String getEmail() {
		return getString("email"); //method from the Model class, inherited
	}

	public void setEmail(String email) {
		set("email", email);
	}

	public String getPassword() {
		return getString("password");
	}

	public void setPassword(String password) {
		set("password", password);
	}

	public String getFirstName() {
		return getString("first_name");
	}

	public void setFirstName(String firstName) {
		set("first_name", firstName);
	}

	public String getLastName() {
		return getString("last_name");
	}

	public void setLastName(String lastName) {
		set("last_name", lastName);
	}

}
