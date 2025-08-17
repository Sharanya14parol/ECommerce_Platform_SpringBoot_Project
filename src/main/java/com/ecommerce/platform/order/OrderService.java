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
import com.razorpay.Order ;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    private final RazorpayClient razorpayClient;
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

 
    public OrderService(
        @Value("${razorpay.key_id}") String keyId,
        @Value("${razorpay.key_secret}") String keySecret
    ) throws Exception {
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
    }

    public PurchaseOrder createOrder(String username) throws Exception {
    	logger.info("User transaction: Attempting to create order for user {}",username);
    	
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
        	logger.warn(" Order Failed: user {} not found",username);
            throw new RuntimeException("User not found: " + username);
        }
        User user = userOptional.get();

        Optional<Cart> cartOptional = cartRepository.findByUserUsername(username);
        if (cartOptional.isEmpty()) {
        	logger.warn(" Order Failed: Cart not found for user {} ",username);
            throw new RuntimeException("Cart not found for user: " + username);
        }
        Cart cart = cartOptional.get();

        if (cart.getItems().isEmpty()) {
        	logger.warn(" Order Failed: Cart is empty");
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            if (product.getStockQuantity() < quantity) {
            	logger.warn(" Order Failed: Not enough stock for {}",product.getName());
                throw new RuntimeException("Not enough stock for " + product.getName());
            }

            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setProduct(product);
            newOrderItem.setQuantity(quantity);
            newOrderItem.setPrice(product.getPrice());
            orderItems.add(newOrderItem);

            totalAmount += product.getPrice() * quantity;
        }

        PurchaseOrder newOrder = new PurchaseOrder();
        newOrder.setUser(user);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus("PENDING_PAYMENT");
        newOrder.setItems(orderItems);
        newOrder.setTotalAmount(totalAmount);

        for (OrderItem item : orderItems) {
            item.setOrder(newOrder);
        }

        
        JSONObject options = new JSONObject();
        options.put("amount", (int)(totalAmount * 100)); 
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_" + System.currentTimeMillis());

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);
        newOrder.setRazorpayOrderId(razorpayOrder.get("id"));

        logger.info(" Order Created for user {}",username);
        return orderRepository.save(newOrder);
    }

    public PurchaseOrder createPendingOrder(User user, Cart cart, String razorpayOrderId) {
    	 logger.info("Attempting to create pending order for user {}",user.getUsername());
    			
        if (cart == null || cart.getItems().isEmpty()) {
        	 logger.warn("Pending order creation failed: Cart is empty ");
        	throw new RuntimeException("Cart is empty. Cannot create a pending order.");
        }
        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (CartItem cartItem : cart.getItems()) {
        	
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            
            if (product.getStockQuantity() < quantity) {
            	logger.warn("Pending order creation failed: Insufficient stock for product {}",product.getName());
            	throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setProduct(product);
            newOrderItem.setQuantity(quantity);
            newOrderItem.setPrice(product.getPrice());
            orderItems.add(newOrderItem);
            totalAmount += product.getPrice() * quantity;
        }
        PurchaseOrder newOrder = new PurchaseOrder();
        newOrder.setUser(user);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus("PENDING_PAYMENT");
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRazorpayOrderId(razorpayOrderId);
        for (OrderItem item : orderItems) item.setOrder(newOrder);
        logger.info("Created Pending order for User {} ",user.getUsername());
        return orderRepository.save(newOrder);
    }

    public void completeOrderPayment(String razorpayOrderId, String razorpayPaymentId, String username) {
    	logger.info("User transaction: Attempting to complete payment for Id {}", razorpayOrderId);
    	
        Optional<PurchaseOrder> orderOptional = orderRepository.findByRazorpayOrderId(razorpayOrderId);
        
        if (orderOptional.isEmpty()) {
        	logger.warn("Payment completion failed: Internal order not found for Razorpay Order ID {}",razorpayOrderId);
        	throw new RuntimeException("Internal order not found for Razorpay Order ID: " + razorpayOrderId);
        }
        PurchaseOrder order = orderOptional.get();
        order.setStatus("PAID");
        order.setRazorpayPaymentId(razorpayPaymentId);
        orderRepository.save(order);
        logger.info("User transaction: Payment Done");
        
        for (OrderItem orderItem : order.getItems()) {
            Product product = orderItem.getProduct();
            if (product.getStockQuantity() >= orderItem.getQuantity()) {
                product.setStockQuantity(product.getStockQuantity() - orderItem.getQuantity());
                productRepository.save(product);
            }
        }
        cartService.clearCart(username);
        logger.info("Cart cleared");
    }

    public void failOrderPayment(String razorpayOrderId) {
        Optional<PurchaseOrder> orderOptional = orderRepository.findByRazorpayOrderId(razorpayOrderId);
        if (orderOptional.isEmpty()) {
        	logger.warn("Payment completion failed: Internal order not found for Razorpay Order ID {}",razorpayOrderId);
        	throw new RuntimeException("Internal order not found for Razorpay Order ID: " + razorpayOrderId);
        }
        PurchaseOrder order = orderOptional.get();
        order.setStatus("PAYMENT_FAILED");
        logger.warn("Payment completion failed  for Razorpay Order ID {}",razorpayOrderId);
        orderRepository.save(order);
    }

    public List<PurchaseOrder> getOrder(String username) {
    	logger.info("Attempting to retrieve orders for user: {}",username);
    	logger.info("Retrieved {} orders for user: {}",username);
        return orderRepository.findByUserUsername(username);
    }

    public PurchaseOrder updateOrder(int orderId, String newStatus) {
    	logger.info("Attempting to update orders for Id: {}",orderId);
    	
        Optional<PurchaseOrder> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            PurchaseOrder order = orderOptional.get();
            order.setStatus(newStatus);
            logger.info("Updated orders for OrderId: {}",orderId);
            return orderRepository.save(order);
        }
        logger.warn("Order Updation failed for OrderId {}",orderId);
        return null;
    }
}
