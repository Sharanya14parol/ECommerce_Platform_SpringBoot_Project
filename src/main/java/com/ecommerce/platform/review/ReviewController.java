package com.ecommerce.platform.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

	@Autowired
    private  ReviewService reviewService;

   
    @PostMapping("/addReview")
    public Review addReview(@RequestParam String product_name,@RequestParam int rating, @RequestParam String comment){   
    	String username=getAuthenticatedUsername();
        return reviewService.addReview(product_name, username, rating, comment);
    }

    @GetMapping("/product/{productName}")
    public List<Review> getReviews(@PathVariable String productName) {
        return reviewService.getReviewsForProduct(productName);
    }
    
    
    private String getAuthenticatedUsername() {
    	Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
    	if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    	
    }
}
