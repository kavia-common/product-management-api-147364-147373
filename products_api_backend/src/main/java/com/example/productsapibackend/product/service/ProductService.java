package com.example.productsapibackend.product.service;

import com.example.productsapibackend.product.dto.ProductPatchRequest;
import com.example.productsapibackend.product.dto.ProductRequest;
import com.example.productsapibackend.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Product business operations.
 */
public interface ProductService {
    
    /**
     * Create a new product.
     * 
     * @param request the product creation request
     * @return the created product response
     */
    ProductResponse createProduct(ProductRequest request);
    
    /**
     * Get all products with pagination and sorting.
     * 
     * @param pageable pagination and sorting parameters
     * @return page of product responses
     */
    Page<ProductResponse> getAllProducts(Pageable pageable);
    
    /**
     * Get a product by ID.
     * 
     * @param id the product ID
     * @return the product response
     * @throws com.example.productsapibackend.product.exception.ProductNotFoundException if product not found
     */
    ProductResponse getProductById(Long id);
    
    /**
     * Update an existing product (full update).
     * 
     * @param id the product ID
     * @param request the product update request
     * @return the updated product response
     * @throws com.example.productsapibackend.product.exception.ProductNotFoundException if product not found
     */
    ProductResponse updateProduct(Long id, ProductRequest request);
    
    /**
     * Partially update an existing product.
     * 
     * @param id the product ID
     * @param patchRequest the partial update request
     * @return the updated product response
     * @throws com.example.productsapibackend.product.exception.ProductNotFoundException if product not found
     */
    ProductResponse patchProduct(Long id, ProductPatchRequest patchRequest);
    
    /**
     * Delete a product by ID.
     * 
     * @param id the product ID
     * @throws com.example.productsapibackend.product.exception.ProductNotFoundException if product not found
     */
    void deleteProduct(Long id);
}
