package com.example.Final;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepo extends CassandraRepository<CartTable, UUID> {
//	INSERT INTO cart (id, user_id, items, savedforlateritems, total_amount, appliedPromoCodeId, ordertype, promoCodeAmount)
//	VALUES (uuid(), :userId, [],[], 0, [], 'online', 0);
	@Query("Select items from cart where user_id=:id ALLOW FILTERING")
	List<List<CartItem>> getCartItems(UUID id);
	@Query("update cart set items=:items ,total_amount=:newTotal where id=:cartId")
	void updateCartItems(List<CartItem> items,UUID cartId,double newTotal);
	@Query("select * from cart where user_id=:userId ALLOW FILTERING")
	CartTable getCart(UUID userId);
	@Query("update cart set items=:items,savedForLaterItems=:savedForLater,total_amount=:newTotal where id=:cartId")
	void updateCartItemsAndSaved(List<CartItem> items,List<CartItem> savedForLater,UUID cartId,double newTotal);

	@Query("update cart set appliedpromocodeid=:appliedPromoCodeId , promoCodeAmount=:promoamount, total_amount=:newTotal  where id=:cartId")
	void updateCartPromo(@Param("appliedPromoCodeId") String appliedPromoCodeId, @Param("promoamount") double promoamount,@Param("newTotal") double newTotal, @Param("cartId") UUID cartId);

	@Query("INSERT INTO cart (id, user_id, items, savedforlateritems, total_amount, appliedPromoCodeId, ordertype, promoCodeAmount) VALUES (uuid(), :userId, [],[], 0, '', 'online', 0)")
	void insertNewCart(@Param("userId") UUID userId);

	default CartTable createNewCart(UUID userId) {
		insertNewCart(userId);
		return getCart(userId);
	}
}
