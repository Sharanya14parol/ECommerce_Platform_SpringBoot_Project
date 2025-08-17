package com.ecommerce.platform.review;

import com.ecommerce.platform.product.Product;
import com.ecommerce.platform.product.ProductRepository;
import com.ecommerce.platform.user.User;
import com.ecommerce.platform.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReviewService {
	private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

	@Autowired
    private  ReviewRepository reviewRepository;
	
	@Autowired
    private  ProductRepository productRepository;
	
	@Autowired
    private  UserRepository userRepository;
	
	public Review addReview(String product_name, String username,int rating,String comment) {
		logger.info("User transaction: Attempting to add review for product {} by user {}",product_name,username);
		
		Optional<Product> optionalProduct= productRepository.findByName(product_name);
		if(optionalProduct.isEmpty()) {
			logger.warn("User transaction Failed:  Product {} not found",product_name);
			throw new RuntimeException("Product not found");
		}
		Product product=optionalProduct.get();
		
		Optional<User> optionalUser=userRepository.findByUsername(username);
		if(optionalUser.isEmpty()) {
			logger.warn("User transaction Failed: User {} not found",username);
			throw new RuntimeException("User not found");
		}
		
		User user=optionalUser.get();
		
		Review userReview= new Review();
		userReview.setProduct(product);
		userReview.setUser(user);
		userReview.setRating(rating);
		userReview.setComment(comment);
		
		logger.info("Review by User {} for Product {} added Successfully ",username,product_name);
		return reviewRepository.save(userReview);
	}

	public List<Review> getReviewsForProduct(String product_name) {
		logger.info("Attempting to retrieve reviews for product {}",product_name);
		
		Optional<Product> optionalProduct= productRepository.findByName(product_name);
		if(optionalProduct.isEmpty()) {
			logger.warn("User transaction Failed:  Product {} not found",product_name);
			throw new RuntimeException("Product not found");
		}
		Product product=optionalProduct.get();
		logger.info("Retrieved Reviews for product {} successfully",product_name);
		return reviewRepository.findByProductName(product.getName());
	}
	

    

	
    
}
