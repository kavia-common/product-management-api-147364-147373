package com.example.productsapibackend.product.mapper;

import com.example.productsapibackend.product.dto.ProductRequest;
import com.example.productsapibackend.product.dto.ProductResponse;
import com.example.productsapibackend.product.entity.Product;
import org.springframework.stereotype.Component;

/**
 * Mapper component for converting between Product entities and DTOs.
 */
@Component
public class ProductMapper {
    
    /**
     * Convert ProductRequest DTO to Product entity.
     * 
     * @param request the product request DTO
     * @return the product entity
     */
    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }
        
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        return product;
    }
    
    /**
     * Convert Product entity to ProductResponse DTO.
     * 
     * @param product the product entity
     * @return the product response DTO
     */
    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getQuantity(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
    
    /**
     * Update existing Product entity with data from ProductRequest.
     * 
     * @param product the existing product entity
     * @param request the product request DTO
     */
    public void updateEntityFromRequest(Product product, ProductRequest request) {
        if (product == null || request == null) {
            return;
        }
        
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
    }
}
