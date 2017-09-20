package com.libertymutual.goforcode.spark.app.models;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

public class Apartment extends Model {

	public Apartment() {
	}

	public Apartment(int rent, int numberOfBedrooms, double numberOfbathrooms, int squareFootage, String address,
			String city, String state, String zipCode) {
		setRent(rent);
		setNumberOfBedrooms(numberOfBedrooms);
		setNumberOfBathrooms(numberOfbathrooms);
		setSquareFootage(squareFootage);
		setAddress(address);
		setCity(city);
		setState(state);
		setZipCode(zipCode);
	}

	public int getNumberOfBedrooms() {
		return getInteger("number_of_bedrooms");
	}

	public double getNumberOfBathrooms() {
		return getInteger("number_of_bathrooms");
	}

	public void setNumberOfBathrooms(int numberOfBathrooms) {
		set("number_of_bathrooms", numberOfBathrooms);
	}

	public int getSquareFootage() {
		return getInteger("square_footage");
	}

	public void setSquareFootage(int squareFootage) {
		set("square_footage", squareFootage);
	}

	public String getAddress() {
		return getString("address");
	}

	public void setAddress(String address) {
		set("address", address);
	}

	public String getCity() {
		return getString("city");
	}

	public void setCity(String city) {
		set("city", city);
	}

	public String getState() {
		return getString("state");
	}

	public void setState(String state) {
		set("state", state);
	}

	public String getZipCode() {
		return getString("zip_code");
	}

	public void setZipCode(String zipCode) {
		set("zip_code", zipCode);
	}

	public int getRent() {
		return getInteger("rent");
	}

	public void setRent(int rent) {
		set("rent", rent);
	}

	public void setNumberOfBedrooms(int numberOfBedrooms) {
		set("number_of_bedrooms", numberOfBedrooms);
	}

	public void setNumberOfBathrooms(double numberOfBathrooms) {
		set("number_of_bathrooms", numberOfBathrooms);
	}
}