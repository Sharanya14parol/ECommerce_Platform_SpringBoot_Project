package com.ecommerce.platform.review;

import com.ecommerce.platform.product.Product;
import com.ecommerce.platform.product.ProductRepository;
import com.ecommerce.platform.user.User;
import com.ecommerce.platform.user.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private Product product;
    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setName("Laptop");

        user = new User();
        user.setUsername("testUser");
    }

    @Test
    void addReview_ShouldReturnReviewSuccessfully() {
        when(productRepository.findByName("Laptop")).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Review review = reviewService.addReview("Laptop", "testUser", 5, "Excellent product!");

        assertNotNull(review);
        assertEquals(product, review.getProduct());
        assertEquals(user, review.getUser());
        assertEquals(5, review.getRating());
        assertEquals("Excellent product!", review.getComment());
    }

    @Test
    void addReview_ProductNotFound_ShouldThrowException() {
        when(productRepository.findByName("Laptop")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reviewService.addReview("Laptop", "testUser", 5, "Nice"));

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void addReview_UserNotFound_ShouldThrowException() {
        when(productRepository.findByName("Laptop")).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reviewService.addReview("Laptop", "testUser", 5, "Nice"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getReviewsForProduct_ShouldReturnList() {
        when(productRepository.findByName("Laptop")).thenReturn(Optional.of(product));
        when(reviewRepository.findByProductName("Laptop")).thenReturn(List.of(
                new Review(), new Review()
        ));

        List<Review> reviews = reviewService.getReviewsForProduct("Laptop");

        assertNotNull(reviews);
        assertEquals(2, reviews.size());
    }

    @Test
    void getReviewsForProduct_ProductNotFound_ShouldThrowException() {
        when(productRepository.findByName("Laptop")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reviewService.getReviewsForProduct("Laptop"));

        assertEquals("Product not found", exception.getMessage());
    }
}
