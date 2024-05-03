package com.example.Final;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
	@Autowired
	private JwtDecoderService jwtDecoderService;
	@Autowired
	public Services cartService;

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@GetMapping("/getCart")
	public ResponseEntity<Object> getCart(@RequestParam String token) throws Exception {
		Claims claims = jwtDecoderService.decodeJwtToken(token, "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		if (claims == null) {
			return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
		} else {
			try {
				CartTable cart = cartService.getUserCart((String) claims.get("userId"));
				return new ResponseEntity<>(cart, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PostMapping("/editItemCount")
	public String editItemCount(@RequestBody Map<String,Object> data) {
		return cartService.updateItemCount((String)data.get("itemId"), (String)data.get("token"), (int)data.get("itemCount"));
	}
	@PostMapping("/addItemToSavedLater")
	public String addItemToSavedLater(@RequestBody Map<String, Object> data) {
		return cartService.addToSavedForLater((String)data.get("itemId"), (String)data.get("token"));
	}
	@PostMapping("/returnItemFromSavedLater")
	public String returnItemFromSavedLater(@RequestBody Map<String, Object> data) {
		return cartService.returnFromSavedForLater((String)data.get("itemId"), (String)data.get("token"));
	}

	@PostMapping("/removeItem")
	public ResponseEntity<Object> removeItemFromCart(@RequestBody  Map<String, Object> data) throws Exception{
		Claims claims = jwtDecoderService.decodeJwtToken((String) data.get("token"), "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		String item = (String) data.get("itemId");
		UUID itemID = UUID.fromString(item);

		if (claims == null) {
			return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
		}
		else{
			try {
				CartTable cart = cartService.removeItemFromCart((String) claims.get("userId"), itemID);
				return new ResponseEntity<>(cart, HttpStatus.OK);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}

	}

	@PostMapping("/changeOrderType")
	public ResponseEntity<String> setOrderType(@RequestBody Map<String, Object> data) throws Exception {
		Claims claims = jwtDecoderService.decodeJwtToken((String) data.get("token"), "ziad1234aaaa&&&&&thisisasecretekeyaaa");

		if (claims == null) {
			return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
		}
		else{
			try {
				cartService.setOrderType((String) claims.get("userId"), (String) data.get("orderType"), (String) data.get("itemId"));
				return new ResponseEntity<>("Order type changed successfully", HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	@PostMapping("/applyPromo")
	public ResponseEntity<Object> applyPromo(@RequestBody Map<String, Object> data) {
		Claims claims = jwtDecoderService.decodeJwtToken((String) data.get("token"), "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		if (claims == null) {
			return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
		} else {
			try {
				CartTable cart = cartService.applyPromo((String) claims.get("userId"), (String) data.get("promoCode"));
				return new ResponseEntity<>(cart, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	@GetMapping("/getAllUsedPromo")
	public List<UserUsedPromo> getAllPromoUsed(){
		return cartService.getAllUsedPromo();
	}

}
