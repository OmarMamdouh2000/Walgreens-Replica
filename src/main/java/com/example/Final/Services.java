package com.example.Final;

import java.util.UUID;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.stereotype.Service;

import com.example.Commands.JwtDecoderService;

@Service
public class Services {
	@Autowired
    private JwtDecoderService jwtDecoderService;
	@Autowired
	private CartRepo cartRepo;

    @Autowired
    private UserUsedPromoRepo userUsedPromoRepo;

    @Autowired
    private PromoRepo promoRepo;

    public CartTable getUserCart(String user) throws Exception {
        UUID userId = UUID.fromString(user);
        try{
            CartTable userCart = cartRepo.getCart(userId);
            if(userCart != null){
                return userCart;
            }else{
                return new CartTable(UUID.randomUUID(),"",new ArrayList<CartItem>(), "online", new ArrayList<CartItem>(), 0.0, userId);
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
	
	
	

    public CartTable removeItemFromCart(String user,UUID itemId) throws Exception {
        UUID userId = UUID.fromString(user);
        try{
            CartTable userCart = cartRepo.getCart(userId);

            if(userCart != null){
                CartItem toBeDeleted = userCart.getItems().stream()
                        .filter(item -> item.getItemId().equals(itemId))
                        .findFirst()
                        .orElse(null);

                double priceRemove = toBeDeleted.getPurchasedPrice() * toBeDeleted.getItemCount();

                List<CartItem> updatedItems = userCart.getItems().stream()
                        .filter(item -> !item.getItemId().equals(itemId))
                        .collect(Collectors.toList());


                userCart.setItems(updatedItems);
                userCart.setTotalAmount(userCart.getTotalAmount() - priceRemove);
                return cartRepo.save(userCart);
            }else{
                throw new Exception("Cart not found");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    public void setOrderType(String user, String orderType, String stringItem) throws Exception {
        UUID userID = UUID.fromString(user);
        UUID itemID = UUID.fromString(stringItem);
        try{
            CartTable userCart = cartRepo.getCart(userID);

            if(userCart != null){
                if(orderType.equals("shipping")){
                    CartItem cartItem = userCart.getItems().stream()
                            .filter(item -> item.getItemId().equals(itemID)).
                            toList().get(0);
                    cartItem.setDeliveryType("shipping");
                    cartRepo.save(userCart);
                }else{
                    userCart.getItems().forEach(item -> item.setDeliveryType(orderType));
                    cartRepo.save(userCart);
                }
            }else{
                throw new Exception("Cart not found");
            }
        }catch (Exception e){
            throw new Exception("Server error try again");
        }
    }

    public CartTable applyPromo(String user, String promoCode) throws Exception {
        UUID userID = UUID.fromString(user);

        try {
            //get promo from promos --> if not found return invalid
            PromoCodeTable promo = promoRepo.getPromoCodeByCode(promoCode);

            if (promo == null) {
                throw new Exception("Promo not found");
            }else {

                CartTable userCart = cartRepo.getCart(userID);
                if (userCart == null) {
                    throw new Exception("Cart not found");
                }
                //check if promo is in userUsedPromos --> if so return invalid
                UserUsedPromo isUsed = userUsedPromoRepo.findUserPromo(userID, promoCode);

                if (isUsed != null) {
                    throw new Exception("Promo already used");
                } else if (!promo.isValid()) {
                    throw new Exception("Promo not valid");
                } else if (promo.getExpiryDate().isBefore(java.time.LocalDate.now())) {
                    throw new Exception("Promo expired");
                } else {
                    double totalAmount = userCart.getTotalAmount();
                    double promoAmount = promo.getDiscountValue();

                    //apply promo amount to total amount
                    double newAmount = totalAmount * (1 - promoAmount / 100);
                    userCart.setTotalAmount(newAmount);

                    //apply promo to cart
                    userCart.setAppliedPromoCodeId(promoCode);
                    userCart.setPromoCodeAmount(promo.getDiscountValue());

                    //add promo to userUsedPromos
                    userUsedPromoRepo.insertUserPromo(userID, promoCode);

                    return cartRepo.save(userCart);
                }
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public List<UserUsedPromo> getAllUsedPromo(){
		return userUsedPromoRepo.getAll();
	}
}
