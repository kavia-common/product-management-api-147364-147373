package com.example.productsapibackend.product.controller;

import com.example.productsapibackend.product.dto.ProductPatchRequest;
import com.example.productsapibackend.product.dto.ProductRequest;
import com.example.productsapibackend.product.dto.ProductResponse;
import com.example.productsapibackend.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Product CRUD operations.
 * Exposes endpoints at /api/products
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * Create a new product.
     * 
     * @param request the product creation request
     * @return the created product response
     */
    // PUBLIC_INTERFACE
    @PostMapping
    @Operation(
        summary = "Create a new product",
        description = "Creates a new product with the provided name, price, and quantity"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get all products with pagination and sorting.
     * 
     * @param page page number (default: 0)
     * @param size page size (default: 20)
     * @param sort sorting criteria (e.g., "name,asc" or "price,desc")
     * @return page of product responses
     */
    // PUBLIC_INTERFACE
    @GetMapping
    @Operation(
        summary = "Get all products",
        description = "Retrieves a paginated list of all products with optional sorting"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @Parameter(description = "Page number (0-indexed)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort criteria (e.g., 'name,asc' or 'price,desc')") 
            @RequestParam(defaultValue = "id,asc") String sort) {
        
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get a product by ID.
     * 
     * @param id the product ID
     * @return the product response
     */
    // PUBLIC_INTERFACE
    @GetMapping("/{id}")
    @Operation(
        summary = "Get product by ID",
        description = "Retrieves a single product by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Product ID") 
            @PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update a product (full update).
     * 
     * @param id the product ID
     * @param request the product update request
     * @return the updated product response
     */
    // PUBLIC_INTERFACE
    @PutMapping("/{id}")
    @Operation(
        summary = "Update a product",
        description = "Fully updates an existing product with all fields required"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Product ID") 
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Partially update a product.
     * 
     * @param id the product ID
     * @param patchRequest the partial update request
     * @return the updated product response
     */
    // PUBLIC_INTERFACE
    @PatchMapping("/{id}")
    @Operation(
        summary = "Partially update a product",
        description = "Updates only the provided fields of an existing product"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> patchProduct(
            @Parameter(description = "Product ID") 
            @PathVariable Long id,
            @Valid @RequestBody ProductPatchRequest patchRequest) {
        ProductResponse response = productService.patchProduct(id, patchRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete a product by ID.
     * 
     * @param id the product ID
     * @return no content
     */
    // PUBLIC_INTERFACE
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a product",
        description = "Deletes a product by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") 
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
