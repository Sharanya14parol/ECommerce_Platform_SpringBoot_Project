package com.ecommerce.platform.review;

import com.ecommerce.platform.product.Product;
import com.ecommerce.platform.product.ProductRepository;
import com.ecommerce.platform.user.User;
import com.ecommerce.platform.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewServiceIntegrationTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void addReview_ShouldSaveReview() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("pass");
        userRepository.save(user);

        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(1000);
        productRepository.save(product);

        Review review = reviewService.addReview("Laptop", "alice", 5, "Excellent!");
        assertNotNull(review.getId());
        assertEquals(5, review.getRating());
        assertEquals("Excellent!", review.getComment());
    }

    @Test
    void getReviewsForProduct_ShouldReturnList() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword("pass");
        userRepository.save(user);

        Product product = new Product();
        product.setName("Phone");
        product.setPrice(500);
        productRepository.save(product);

        reviewService.addReview("Phone", "bob", 4, "Good phone");

        List<Review> reviews = reviewService.getReviewsForProduct("Phone");
        assertEquals(1, reviews.size());
        assertEquals(4, reviews.get(0).getRating());
    }
}
