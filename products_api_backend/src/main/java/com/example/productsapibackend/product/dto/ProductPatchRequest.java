package com.example.productsapibackend.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for partially updating a product.
 * All fields are optional - only provided fields will be updated.
 */
@Schema(description = "Product partial update request")
public class ProductPatchRequest {
    
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    @Schema(description = "Product name", example = "Laptop")
    private String name;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;
    
    @Min(value = 0, message = "Quantity must be non-negative")
    @Schema(description = "Product quantity", example = "10")
    private Integer quantity;
    
    // Constructors
    public ProductPatchRequest() {
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    // Helper methods to check if fields are set
    public boolean hasName() {
        return name != null;
    }
    
    public boolean hasPrice() {
        return price != null;
    }
    
    public boolean hasQuantity() {
        return quantity != null;
    }
}
