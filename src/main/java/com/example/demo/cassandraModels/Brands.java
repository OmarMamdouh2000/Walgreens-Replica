package com.example.demo.cassandraModels;

import org.springframework.data.cassandra.core.mapping.Table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import java.util.List;
import java.util.UUID;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brands {
	@PrimaryKey
	public UUID id;
	public String name;
	public List<Pobject> brandProducts;
	
//	public Brands(UUID id, String name, ArrayList<UUID> categories_id) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.categories_id = categories_id;
//	}
//	
	
	
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
	public List<Pobject> getBrandProducts() {
		return brandProducts;
	}
	public void setBrandProducts(List<Pobject> brandProducts) {
		this.brandProducts = brandProducts;
	}
	
	@Override
    public String toString() {
        return "CartTable{" +
                "id=" + id +
                ", name=" + name +
                ", brandProducts='" + brandProducts + '\'' +
                '}';
    }
}