package com.ecommerce.platform.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setProduct_id(1);
        product.setName("Laptop");
        product.setPrice(1000);
    }

    @Test
    void addProduct_AlreadyExists_ShouldThrowException() {
        when(productRepository.findByName("Laptop")).thenReturn(Optional.of(product));
        Exception exception = assertThrows(RuntimeException.class, () -> productService.addProduct(product));
        assertEquals("Product already exitst", exception.getMessage());
    }

    @Test
    void addProduct_NewProduct_ShouldReturnSavedProduct() {
        when(productRepository.findByName("Laptop")).thenReturn(Optional.empty());
        when(productRepository.save(product)).thenReturn(product);
        Product saved = productService.addProduct(product);
        assertEquals(product, saved);
    }
}
