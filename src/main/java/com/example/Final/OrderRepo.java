package com.example.Final;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepo extends CassandraRepository<OrderTable, UUID> {
	@Query("select * from orders where user_id=:userId ALLOW FILTERING" )
	public List<OrderTable> getOrders(UUID userId);
	@Query("select * from orders where date_issued=:date ALLOW FILTERING")
	public OrderTable getOrderByDate(@Param("date") LocalDate  date);
	@Query("select * from orders where order_status='active' and user_id=:userId  ALLOW FILTERING")
	public List<OrderTable> getActiveOrders(UUID userId);

}
