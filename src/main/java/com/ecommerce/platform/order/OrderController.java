package com.ecommerce.platform.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public PurchaseOrder createOrder() throws Exception {
        String username = getAuthenticatedUsername();
        return orderService.createOrder(username);
    }

    @GetMapping("/getOrders")
    public List<PurchaseOrder> getOrderOfAnUser() {
        String username = getAuthenticatedUsername();
        return orderService.getOrder(username);
    }

    @PutMapping("/updateStatus")
    public PurchaseOrder updateOrderStatus(@RequestParam int orderId, @RequestParam String newStatus) {
        return orderService.updateOrder(orderId, newStatus);
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
