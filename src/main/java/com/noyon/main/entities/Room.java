package com.noyon.main.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String image;
	private double price;
	private int adultNo;
	private int childNo;
	
	@ManyToOne(fetch =FetchType.EAGER)
	@JoinColumn(name="hotelId")
	private Hotel hotel;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAdultNo() {
		return adultNo;
	}

	public void setAdultNo(int adultNo) {
		this.adultNo = adultNo;
	}

	public int getChildNo() {
		return childNo;
	}

	public void setChildNo(int childNo) {
		this.childNo = childNo;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Room(int id, String name, String image, double price, int adultNo, int childNo, Hotel hotel) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.price = price;
		this.adultNo = adultNo;
		this.childNo = childNo;
		this.hotel = hotel;
	}

	public Room() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
