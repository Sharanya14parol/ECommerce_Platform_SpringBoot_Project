package com.ecommerce.platform.order;

import com.ecommerce.platform.cart.Cart;
import com.ecommerce.platform.cart.CartItem;
import com.ecommerce.platform.cart.CartRepository;
import com.ecommerce.platform.cart.CartService;
import com.ecommerce.platform.product.Product;
import com.ecommerce.platform.product.ProductRepository;
import com.ecommerce.platform.user.User;
import com.ecommerce.platform.user.UserRepository;
import com.razorpay.RazorpayClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock private UserRepository userRepository;
    @Mock private CartRepository cartRepository;
    @Mock  CartService cartService;
    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private RazorpayClient razorpayClient;

    private User user;
    private Product product;
    private Cart cart;
    
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }


    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testUser");

        product = new Product();
        product.setName("Laptop");
        product.setPrice(1000);
        product.setStockQuantity(10);
        product.setProduct_id(1);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setUser(user);
        cart.setItems(List.of(cartItem));
    }

    @Test
    void createPendingOrder_ShouldReturnOrderSuccessfully() {
        when(orderRepository.save(any(PurchaseOrder.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        PurchaseOrder order = orderService.createPendingOrder(user, cart, "razorpay_order_123");

        assertNotNull(order);
        assertEquals(2000, order.getTotalAmount());
        assertEquals(1, order.getItems().size());
        assertEquals("PENDING_PAYMENT", order.getStatus());
    }

    @Test
    void createPendingOrder_EmptyCart_ShouldThrowException() {
        Cart emptyCart = new Cart();
        emptyCart.setUser(user);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.createPendingOrder(user, emptyCart, "razorpay_order_123"));

        assertEquals("Cart is empty. Cannot create a pending order.", exception.getMessage());
    }
    

    @Test
    void completeOrderPayment_ShouldSetStatusPaidAndReduceStock() {
        PurchaseOrder order = new PurchaseOrder();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setPrice(product.getPrice());

        order.setItems(List.of(orderItem));
        order.setStatus("PENDING_PAYMENT");
        order.setRazorpayOrderId("razorpay_123");

        when(orderRepository.findByRazorpayOrderId("razorpay_123"))
                .thenReturn(Optional.of(order));
        when(orderRepository.save(any(PurchaseOrder.class))).thenAnswer(i -> i.getArguments()[0]);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        cartService = mock(CartService.class);
        orderService.setCartService(cartService);


        orderService.completeOrderPayment("razorpay_123", "payment_123", "testUser");

        assertEquals("PAID", order.getStatus());
        assertEquals(8, product.getStockQuantity());
        verify(orderRepository, times(1)).save(order);
        verify(productRepository, times(1)).save(product);
        verify(cartService, times(1)).clearCart("testUser");
    }
}
