package com.example.demo.cassandraModels;

import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
	@PrimaryKey
	public UUID id;
	public String name;
	public List<UUID> categories_id;
	
	
	
//	public Brand(UUID id, String name, ArrayList<UUID> categories_id) {
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
	public List<UUID> getCategories_id() {
		return categories_id;
	}
	public void setCategories_id(List<UUID> categories_id) {
		this.categories_id = categories_id;
	}
	
	
	
	
}