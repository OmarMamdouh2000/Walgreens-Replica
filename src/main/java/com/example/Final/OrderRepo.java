package com.example.Final;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepo extends CassandraRepository<OrderTable, UUID> {
    @Query("SELECT * FROM orders WHERE user_id = :user_id ALLOW FILTERING")
    public List<OrderTable> getUserOrders(@Param("user_id") UUID userId);
    @Query("Select * from orders where user_id = :userId and date_issued = :date and order_status = :orderstatus ALLOW FILTERING")
    List<OrderTable> filterDateAndStatus(@Param("userId") UUID userId,
                                         @Param("date") LocalDate dateIssued,
                                         @Param("orderstatus") String orderStatus);
    @Query("Select * from orders where user_id = :userId and date_issued = :date ALLOW FILTERING")
    List<OrderTable> filterByDate(@Param("userId") UUID userId, @Param("date") LocalDate dateIssued);

    @Query("Select * from orders where user_id = :userId and order_status = :orderstatus ALLOW FILTERING")
    List<OrderTable> filterByStatus(@Param("userId")UUID userId,@Param("orderstatus") String orderStatus);
	@Query("select * from orders where user_id=:userId ALLOW FILTERING" )
	public List<OrderTable> getOrders(UUID userId);
	@Query("select * from orders where date_issued=:date ALLOW FILTERING")
	public OrderTable getOrderByDate(@Param("date") LocalDate  date);
	@Query("select * from orders where order_status='active' and user_id=:userId  ALLOW FILTERING")
	public List<OrderTable> getActiveOrders(UUID userId);

}
