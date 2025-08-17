package com.ecommerce.platform.cart;

import com.ecommerce.platform.product.Product;
import com.ecommerce.platform.product.ProductRepository;
import com.ecommerce.platform.user.User;
import com.ecommerce.platform.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CartService {
	private static final Logger logger = LoggerFactory.getLogger(CartService.class);

	@Autowired
    private CartRepository cartRepository;
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private ProductRepository productRepository;

    
    public Cart addToCart(String username, String product_name, int quantity) {
    	
    	logger.info("User transaction: Attempting to add Product {} , quantity {}",product_name,quantity);
    	
    	Product product = productRepository.findByName(product_name)
                .orElseThrow(() -> {
                	 logger.warn("Add to cart failed: product not found: {}",product_name);
                	 return new IllegalArgumentException("Product not found");
                });
    	
    	User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                	logger.warn("Add to cart failed: User not found: {}",username);
                	return new UsernameNotFoundException("User not found");
                });
    	
    	if (quantity <= 0) {
            logger.warn("Add to cart failed: Invalid quantity {} for Product ID {}", quantity, product.getProduct_id());
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (product.getStockQuantity() < quantity) {
            logger.warn("Add to cart failed: Insufficient stock for Product ID {} (Available: {}, Requested: {})", product.getProduct_id(), product.getStockQuantity(), quantity);
            throw new RuntimeException("Not enough stock for product: " + product.getName());
        }
    	
    	Optional<Cart> existingCartOptional = cartRepository.findByUserUsername(username);
    	 
    	Cart cart;
    	if (existingCartOptional.isPresent()) {
            cart = existingCartOptional.get();
        } else {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
            cart = cartRepository.save(cart);
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProduct_id() == product.getProduct_id())
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.addItem(newItem);
        }
        logger.info("User transaction: Added Product {} to cart",product_name);
        return cartRepository.save(cart);
        
    }

    
    public List<CartItem> getUserCartItems(String username) {
        
        Optional<Cart> optionalCart = cartRepository.findByUserUsername(username);
        if (optionalCart.isPresent()) {
        	logger.info("User transaction: Retrieving items in Cart");
            return optionalCart.get().getItems();
        } else {
        	logger.warn("User transaction: Cart is empty");
            return List.of(); 
        }
    }

  
    public void removeFromCart(String username, String product_name) {
        logger.info("User transaction: Attempting to remove Product {}",product_name);
        
        Optional<Cart> optionalCart = cartRepository.findByUserUsername(username);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            
            
            Optional<CartItem> itemToRemove = cart.getItems().stream()
                    .filter(item -> item.getProduct().getName().equals(product_name))
                    .findFirst();

            if (itemToRemove.isPresent()) {
            	CartItem item = itemToRemove.get();
                item.setQuantity(item.getQuantity() - 1);
                if (item.getQuantity() <= 0) {
                    cart.getItems().remove(item);
                }
                cartRepository.save(cart);
            }
        }
    }
    
    
    public void clearCart(String username) {
    	logger.info("User transaction: Attempting to clear cart for user {}",username);
    	User user =userRepository.findByUsername(username)
    			.orElseThrow(() ->{
    				 logger.warn("User not found ");
    				return new RuntimeException("User not found");
    			});
        Optional<Cart> optionalCart = cartRepository.findByUserUsername(username);
        
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getItems().clear();
            logger.info("User transaction: Successfully cleared cart");
            cartRepository.save(cart);
        }else {
        	logger.warn("Cart already empty");
        }
    }
    
    public double calculateTotalPrice(Cart cart) {
        double total = 0.0;
        if (cart != null && cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }
        return total;
    }
}