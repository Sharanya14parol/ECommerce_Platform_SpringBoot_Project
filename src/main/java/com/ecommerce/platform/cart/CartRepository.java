package com.ecommerce.platform.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{
	
	  Optional<Cart> findByUserUsername(String username);
}
