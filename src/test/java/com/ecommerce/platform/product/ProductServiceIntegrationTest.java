package com.ecommerce.platform.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void addProduct_ShouldSaveProduct() {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(1200);
        product.setStockQuantity(10);

        Product saved = productService.addProduct(product);

        assertNotNull(saved.getProduct_id());
        assertEquals("Laptop", saved.getName());
        assertEquals(1200, saved.getPrice());
    }

    @Test
    void addProduct_DuplicateName_ShouldThrowException() {
        Product product = new Product();
        product.setName("Mouse");
        product.setPrice(50);
        product.setStockQuantity(100);

        productService.addProduct(product);

        Product duplicate = new Product();
        duplicate.setName("Mouse");
        duplicate.setPrice(60);
        duplicate.setStockQuantity(50);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.addProduct(duplicate));
        assertEquals("Product already exitst", ex.getMessage());
    }

    @Test
    void updateProduct_ShouldModifyProduct() {
        Product product = new Product();
        product.setName("Keyboard");
        product.setPrice(100);
        product.setStockQuantity(20);

        Product saved = productService.addProduct(product);

        saved.setPrice(120);
        saved.setStockQuantity(25);

        Product updated = productService.updateProduct(saved, saved.getProduct_id());

        assertEquals(120, updated.getPrice());
        assertEquals(25, updated.getStockQuantity());
    }

    @Test
    void deleteProduct_ShouldRemoveProduct() {
        Product product = new Product();
        product.setName("Monitor");
        product.setPrice(300);
        product.setStockQuantity(15);

        Product saved = productService.addProduct(product);

        productService.deleteProduct(saved.getProduct_id());

        assertFalse(productRepository.findById(saved.getProduct_id()).isPresent());
    }

    @Test
    void getAllProducts_ShouldReturnList() {
        Product p1 = new Product();
        p1.setName("Item1");
        p1.setPrice(10);
        p1.setStockQuantity(5);
        productService.addProduct(p1);

        Product p2 = new Product();
        p2.setName("Item2");
        p2.setPrice(20);
        p2.setStockQuantity(10);
        productService.addProduct(p2);

        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }
}
