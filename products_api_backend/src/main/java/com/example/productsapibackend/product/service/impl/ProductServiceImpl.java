package com.example.productsapibackend.product.service.impl;

import com.example.productsapibackend.product.dto.ProductPatchRequest;
import com.example.productsapibackend.product.dto.ProductRequest;
import com.example.productsapibackend.product.dto.ProductResponse;
import com.example.productsapibackend.product.entity.Product;
import com.example.productsapibackend.product.exception.ProductNotFoundException;
import com.example.productsapibackend.product.mapper.ProductMapper;
import com.example.productsapibackend.product.repository.ProductRepository;
import com.example.productsapibackend.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of ProductService interface.
 * Handles business logic for product operations.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }
    
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(product);
    }
    
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        productMapper.updateEntityFromRequest(product, request);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }
    
    @Override
    public ProductResponse patchProduct(Long id, ProductPatchRequest patchRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        // Apply partial updates only for provided fields
        if (patchRequest.hasName()) {
            product.setName(patchRequest.getName());
        }
        if (patchRequest.hasPrice()) {
            product.setPrice(patchRequest.getPrice());
        }
        if (patchRequest.hasQuantity()) {
            product.setQuantity(patchRequest.getQuantity());
        }
        
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }
    
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
