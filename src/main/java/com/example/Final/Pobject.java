package com.example.Final;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Frozen;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import org.springframework.stereotype.Component;

@Component
public class Pobject {
	
	private UUID id;
    private String name;
    private String image;
    private String brandName;
    private double price;
    private String discount;
    
    public Pobject(UUID id, String name, String image, String brandName, double price, String discount) {
      this.id = id;
      this.name = name;
      this.image = image;
      this.brandName= brandName;
      this.price = price;
      this.discount = discount;
  }

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
