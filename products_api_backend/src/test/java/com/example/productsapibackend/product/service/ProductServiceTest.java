package com.example.productsapibackend.product.service;

import com.example.productsapibackend.product.dto.ProductPatchRequest;
import com.example.productsapibackend.product.dto.ProductRequest;
import com.example.productsapibackend.product.dto.ProductResponse;
import com.example.productsapibackend.product.entity.Product;
import com.example.productsapibackend.product.exception.ProductNotFoundException;
import com.example.productsapibackend.product.mapper.ProductMapper;
import com.example.productsapibackend.product.repository.ProductRepository;
import com.example.productsapibackend.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private ProductMapper productMapper;
    
    @InjectMocks
    private ProductServiceImpl productService;
    
    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    
    @BeforeEach
    void setUp() {
        product = new Product("Laptop", new BigDecimal("999.99"), 10);
        product.setId(1L);
        
        productRequest = new ProductRequest("Laptop", new BigDecimal("999.99"), 10);
        
        productResponse = new ProductResponse(1L, "Laptop", new BigDecimal("999.99"), 10, null, null);
    }
    
    @Test
    void createProduct_Success() {
        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);
        
        ProductResponse result = productService.createProduct(productRequest);
        
        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(product);
    }
    
    @Test
    void getAllProducts_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product));
        
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);
        
        Page<ProductResponse> result = productService.getAllProducts(pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }
    
    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);
        
        ProductResponse result = productService.getProductById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).findById(1L);
    }
    
    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).findById(1L);
    }
    
    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);
        
        ProductResponse result = productService.updateProduct(1L, productRequest);
        
        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }
    
    @Test
    void patchProduct_Success() {
        ProductPatchRequest patchRequest = new ProductPatchRequest();
        patchRequest.setPrice(new BigDecimal("899.99"));
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);
        
        ProductResponse result = productService.patchProduct(1L, patchRequest);
        
        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }
    
    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        
        productService.deleteProduct(1L);
        
        verify(productRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void deleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).deleteById(1L);
    }
}
