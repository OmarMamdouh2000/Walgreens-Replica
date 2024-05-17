package com.example.demo.cassandraModels;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;


@UserDefinedType("pobject")
public class Pobject {
	
	@CassandraType(type = CassandraType.Name.UUID)
	private UUID id;
	@CassandraType(type = CassandraType.Name.TEXT)
    private String name;
	@CassandraType(type = CassandraType.Name.TEXT)
    private String image;
	@CassandraType(type = CassandraType.Name.TEXT)
    private String brandName;
	@CassandraType(type = CassandraType.Name.DOUBLE)
    private double price;
	@CassandraType(type = CassandraType.Name.TEXT)
    private String discount;
    
	public Pobject()
	{
		
	}
	
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
	
	@Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name=" + name +
                ", image=" + image +
                ", brandName=" + brandName +
                ", price=" + price +
                ", discount='" + discount + '\'' +
                '}';
    }
}
