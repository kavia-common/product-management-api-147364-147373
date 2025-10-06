package com.example.productsapibackend.product.controller;

import com.example.productsapibackend.product.dto.ProductRequest;
import com.example.productsapibackend.product.dto.ProductResponse;
import com.example.productsapibackend.product.exception.ProductNotFoundException;
import com.example.productsapibackend.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private ProductService productService;
    
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    
    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest("Laptop", new BigDecimal("999.99"), 10);
        productResponse = new ProductResponse(1L, "Laptop", new BigDecimal("999.99"), 10, null, null);
    }
    
    @Test
    void createProduct_Success() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);
        
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.quantity").value(10));
    }
    
    @Test
    void createProduct_InvalidRequest() throws Exception {
        ProductRequest invalidRequest = new ProductRequest("", new BigDecimal("-10"), -5);
        
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void getAllProducts_Success() throws Exception {
        Page<ProductResponse> page = new PageImpl<>(Arrays.asList(productResponse), PageRequest.of(0, 20), 1);
        when(productService.getAllProducts(any())).thenReturn(page);
        
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Laptop"));
    }
    
    @Test
    void getProductById_Success() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productResponse);
        
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }
    
    @Test
    void getProductById_NotFound() throws Exception {
        when(productService.getProductById(1L)).thenThrow(new ProductNotFoundException(1L));
        
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void updateProduct_Success() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(productResponse);
        
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }
    
    @Test
    void deleteProduct_Success() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}
