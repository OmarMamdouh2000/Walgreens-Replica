package com.example.Commands;

import com.example.Final.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ApplyPromo implements Command{
    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    private PromoRepo promoRepo;

    private UserUsedPromoRepo userUsedPromoRepo;

    @Autowired
    public ApplyPromo(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo) {
        this.cartRepo=cartRepo;
        this.jwtDecoderService=jwtDecoderService;
        this.promoRepo=promoRepo;
        this.userUsedPromoRepo=userUsedPromoRepo;
    }

    @Override
    public Object execute(Map<String,Object> data) throws Exception {
        String user = (String)data.get("User");
        UUID userID = UUID.fromString(user);

        String promoCode = (String) ((Map<String, Object>)data.get("Data")).get("promoCode");

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

//                    cartRepo.updateCartItems(userCart.getItems(), userCart.getId(), userCart.getTotalAmount());
//                    return cartRepo.save(userCart);
                    return new ResponseEntity<>(userCart, HttpStatus.OK);
                }
            }

    }


}
