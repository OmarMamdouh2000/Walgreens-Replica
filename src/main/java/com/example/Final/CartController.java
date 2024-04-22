package com.example.Final;

import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
	@Autowired
	private JwtDecoderService jwtDecoderService;
	@Autowired
	public Services cartService;

	@GetMapping("/error")
	public String error() {
		return "error";
	}

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@GetMapping("/getCart")
	public CartTable getCart(@RequestParam String token) throws Exception {
		Claims claims = jwtDecoderService.decodeJwtToken(token, "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		if (claims == null) {
			throw new Exception("Invalid token");
		} else {
			try {
				return cartService.getUserCart((String) claims.get("userId"));
			} catch (Exception e) {
				throw new Exception(e.getMessage());
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
	public CartTable removeItemFromCart(@RequestBody  Map<String, Object> data) throws Exception{
		Claims claims = jwtDecoderService.decodeJwtToken((String) data.get("token"), "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		String item = (String) data.get("itemId");
		UUID itemID = UUID.fromString(item);

		if (claims == null) {
			throw new Exception("Invalid token");
		}
		else{
			try {
				return cartService.removeItemFromCart((String) claims.get("userId"), itemID);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}

	}

	@PostMapping("/changeOrderType")
	public void setOrderType(@RequestBody Map<String, Object> data) throws Exception {
		Claims claims = jwtDecoderService.decodeJwtToken((String) data.get("token"), "ziad1234aaaa&&&&&thisisasecretekeyaaa");

		if (claims == null) {
			throw new Exception("Invalid token");
		}
		else{
			try {
				cartService.setOrderType((String) claims.get("userId"), (String) data.get("orderType"), (String) data.get("itemId"));
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
	}
	@PostMapping("/applyPromo")
	public CartTable applyPromo(@RequestBody Map<String, Object> data) throws Exception{
		Claims claims = jwtDecoderService.decodeJwtToken((String) data.get("token"), "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		if (claims == null) {
			throw new Exception("Invalid token");
		}
		else{
			try {
				return cartService.applyPromo((String) claims.get("userId"), (String) data.get("promoId"));
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
	}
}
