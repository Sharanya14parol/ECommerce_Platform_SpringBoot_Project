package com.ecommerce.platform.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder,Integer>{
	List<PurchaseOrder> findByUserUsername(String username);
	List<PurchaseOrder> findByUserUsernameAndStatus(String username, String status);
	Optional<PurchaseOrder> findByRazorpayOrderId(String razorpayOrderId);
}
