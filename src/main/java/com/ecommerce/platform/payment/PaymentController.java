package com.ecommerce.platform.payment;

import com.razorpay.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	@Value("${razorpay.key_secret}")
    private String razorpayKeySecret;
	
	
	@Autowired
    private PaymentService paymentService;

  
    @PostMapping("/create")
    public String createOrder(@RequestParam int amount, @RequestParam String productName) throws Exception {
        String receiptId = "txn_" + System.currentTimeMillis();
        Order order = paymentService.createPaymentOrder(amount, "INR", receiptId);

        // You can store this orderId in your DB with order details
        return order.toString();
    }
    
    @PostMapping("/verify")
    public String verifyPayment(@RequestBody PaymentVerificationRequest request) {
        String razorpayOrderId = request.getRazorpayOrderId();
        String razorpayPaymentId = request.getRazorpayPaymentId();
        String razorpaySignature = request.getRazorpaySignature();

        if (razorpayOrderId == null || razorpayPaymentId == null || razorpaySignature == null) {
            throw new RuntimeException("Missing payment verification data");
        }

        boolean isVerified = paymentService.verifyPaymentSignature(razorpayOrderId, razorpayPaymentId, razorpaySignature, razorpayKeySecret);

        if (isVerified) {
            System.out.println("Payment successfully verified!");
            System.out.println("Razorpay Order ID: " + razorpayOrderId);
            System.out.println("Razorpay Payment ID: " + razorpayPaymentId);
            return "Payment verification successful";
        } else {
            System.err.println("Payment verification failed for Order ID: " + razorpayOrderId);
            return "Payment verification failed";
        }
    }
    
}
