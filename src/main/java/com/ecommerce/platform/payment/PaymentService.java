package com.ecommerce.platform.payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;

    public PaymentService(
            @Value("${razorpay.key_id}") String keyId,
            @Value("${razorpay.key_secret}") String keySecret) throws Exception {
    	System.out.println("✅ Razorpay Key ID = " + keyId); 
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
    }

    public Order createPaymentOrder(int amount, String currency, String receiptId) throws Exception {
        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // amount in paise
        options.put("currency", currency);
        options.put("receipt", receiptId);
        options.put("payment_capture", 1); // auto capture

        return razorpayClient.orders.create(options);
    }
    
    public boolean verifyPaymentSignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature, String injectedRazorpayKeySecret) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            // Use the injectedRazorpayKeySecret (which should be razorpay.key.secret)
            return Utils.verifyPaymentSignature(options, injectedRazorpayKeySecret);
        } catch (RazorpayException e) {
            System.err.println("Error verifying payment signature: " + e.getMessage());
            return false;
        }
    }
}
