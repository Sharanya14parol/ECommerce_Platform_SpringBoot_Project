package com.ecommerce.platform.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	@Autowired
	private ProductRepository productrepository;
	
	public Product addProduct(Product product) {
		logger.info("Admin action: Attempting to add new product {}",product.getName());
		if(productrepository.findByName(product.getName()).isPresent()) {
			logger.warn("Admin action failed: Product {} already exists.",product.getName());
			throw new RuntimeException("Product already exitst");
		}
		logger.info("Admin action: Successfully added product {}",product.getName());
		return productrepository.save(product);			
	}
	public List<Product> getAllProducts(){
		 logger.info("Admin action: Retrieving all products");
		return productrepository.findAll();
	}
	public Product updateProduct(Product product, int id) {
		 logger.info("Admin action: Trying to update product {},",product.getName());
		product.setProduct_id(id);
		 logger.info("Admin action: Successfully updated product {},",product.getName());
		return productrepository.save(product);
		
	}
	
	public void deleteProduct(int id) {
		 logger.info("Admin action: Trying to delete an product with id{},",id);
		 if(productrepository.findById(id)==null) {
			 logger.warn("Admin action:Failed since Product does not exist");
		 throw new RuntimeException("Product with id not found"+id);
		 }
		productrepository.deleteById(id);
		logger.info("Admin action: Successfully deleted product with id {}",id);
	}
	
}
