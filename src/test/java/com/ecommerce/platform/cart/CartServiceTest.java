package com.ecommerce.platform.cart;

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

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    private User user;
    private Product product;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        product = new Product();
        product.setProduct_id(1);
        product.setName("Laptop");
        product.setPrice(1000);
        product.setStockQuantity(10);
    }

    @Test
    void addToCart_ProductNotFound_ShouldThrowException() {
        when(productRepository.findByName("Laptop")).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> cartService.addToCart("testUser", "Laptop", 1));
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void addToCart_UserNotFound_ShouldThrowException() {
        when(productRepository.findByName("Laptop")).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cartService.addToCart("testUser", "Laptop", 1));
    }

    @Test
    void calculateTotalPrice_ShouldReturnCorrectTotal() {
        Cart cart = new Cart();
        cart.setItems(List.of(new CartItem(product, 2)));
        double total = cartService.calculateTotalPrice(cart);
        assertEquals(2000, total);
    }
}
