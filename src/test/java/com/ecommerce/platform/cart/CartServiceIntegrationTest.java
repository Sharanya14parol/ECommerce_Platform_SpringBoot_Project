package com.ecommerce.platform.cart;

import com.ecommerce.platform.product.Product;
import com.ecommerce.platform.product.ProductRepository;
import com.ecommerce.platform.user.User;
import com.ecommerce.platform.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // rolls back DB changes after each test
class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void addToCart_ShouldAddCartItemSuccessfully() {
        User user = new User();
        user.setUsername("integrationUser");
        userRepository.save(user);

        Product product = new Product();
        product.setName("IntegrationLaptop");
        product.setPrice(1500);
        product.setStockQuantity(10);
        productRepository.save(product);

        Cart cart = cartService.addToCart("integrationUser", "IntegrationLaptop", 2);
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }
}
