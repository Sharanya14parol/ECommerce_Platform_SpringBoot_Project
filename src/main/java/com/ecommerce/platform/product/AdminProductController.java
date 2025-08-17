package com.ecommerce.platform.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

	@Autowired
	private ProductService productservice;
	
	@PostMapping
	public Product addProduct(@RequestBody Product p) {
		return productservice.addProduct(p);
	}
	
	
	@PutMapping("/{id}")
	public Product updateProduct(@RequestBody Product p,@PathVariable int id) {
		return productservice.updateProduct(p,id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable int id) {
		productservice.deleteProduct(id);
	}
	
	
}
