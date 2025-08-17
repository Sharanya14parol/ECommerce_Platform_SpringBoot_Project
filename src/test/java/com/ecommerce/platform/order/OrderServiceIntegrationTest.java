package com.ecommerce.platform.order;

import com.ecommerce.platform.cart.CartService;
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
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void createPendingOrder_ShouldSaveOrder() {
        User user = new User();
        user.setUsername("emma");
        user.setPassword("pass");
        userRepository.save(user);

        Product product = new Product();
        product.setName("Camera");
        product.setPrice(1000);
        product.setStockQuantity(5);
        productRepository.save(product);

        cartService.addToCart("emma", "Camera", 2);

        var cart = cartService.getUserCartItems("emma");
        assertFalse(cart.isEmpty());

        var order = orderService.createPendingOrder(user, user.getCart(), "razorpay_order_test");
        assertNotNull(order.getId());
        assertEquals("PENDING_PAYMENT", order.getStatus());
        assertEquals(2000, order.getTotalAmount());
    }
}
