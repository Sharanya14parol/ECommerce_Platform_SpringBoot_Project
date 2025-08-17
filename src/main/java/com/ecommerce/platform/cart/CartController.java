package com.ecommerce.platform.cart;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
    private  CartService cartService;
	

    @PostMapping("/addItem")
    public Cart addToCart(@RequestParam String product_name,@RequestParam int quantity){
		String username=getAuthenticatedUsername();
    	return cartService.addToCart(username, product_name, quantity);  
    }
    
    @GetMapping("/getItems")
    public List<CartItem> getUserCartItems(){
    	String username=getAuthenticatedUsername();
    	return cartService.getUserCartItems(username);
    }
    
    @DeleteMapping("/removeItem")
    public String removeItem(String product_name) {
    	String username=getAuthenticatedUsername();
    	cartService.removeFromCart(username,product_name);
    	return "Item removed";
    }
    
    @DeleteMapping("/clear")
    public String clearCart() {
        String username = getAuthenticatedUsername();
        if (username != null) {
            cartService.clearCart(username);
        }
        return "Cart removed";
    }
    
    
    private String getAuthenticatedUsername() {
    	Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
    	if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    	
    }
}