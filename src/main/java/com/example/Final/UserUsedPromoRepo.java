package com.example.Final;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserUsedPromoRepo extends CassandraRepository<UserUsedPromo, UUID> {
    // You can define custom query methods here if needed
	@Query("select * from UserUsedPromo")
	public List<UserUsedPromo> getAll();
}
