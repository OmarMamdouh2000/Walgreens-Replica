package com.example.Final;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepo extends CassandraRepository<CartTable, UUID> {
	
	@Query("Select items from cart where user_id=:id ALLOW FILTERING")
	List<List<CartItem>> getCartItems(UUID id);
	@Query("update cart set items=:items where id=:cartId")
	void updateCartItems(List<CartItem> items,UUID cartId);
	@Query("select * from cart where user_id=:userId ALLOW FILTERING")
	CartTable getCart(UUID userId);
	@Query("update cart set items=:items,savedForLaterItems=:savedForLater where id=:cartId")
	void updateCartItemsAndSaved(List<CartItem> items,List<CartItem> savedForLater,UUID cartId);

	@Query("select * from promocodes where id=:promoId ALLOW FILTERING")
	PromoCodeTable getPromoCode(@Param("promoId") UUID promoId);

}
