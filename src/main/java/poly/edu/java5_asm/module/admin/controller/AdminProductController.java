package poly.edu.java5_asm.module.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.module.admin.dto.request.AdminProductRequest;
import poly.edu.java5_asm.module.product.dto.response.ProductResponse;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.category.entity.Category;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.brand.repository.BrandRepository;
import poly.edu.java5_asm.module.category.repository.CategoryRepository;
import poly.edu.java5_asm.module.product.repository.ProductRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API Controller cho Admin Product Management
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    /**
     * Tạo sản phẩm mới
     */
    @PostMapping
    @Transactional
    public ResponseEntity<ProductResponse> createProduct(@RequestBody AdminProductRequest request) {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        }
        
        Brand brand = null;
        if (request.getBrandId() != null) {
            brand = brandRepository.findById(request.getBrandId()).orElse(null);
        }

        Product product = Product.builder()
                .name(request.getName())
                .slug(generateSlug(request.getName()))
                .description(request.getDescription())
                .shortDescription(request.getShortDescription())
                .price(request.getPrice())
                .discountPrice(request.getDiscountPrice())
                .category(category)
                .brand(brand)
                .stockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0)
                .imageUrl(request.getImageUrl() != null ? request.getImageUrl() : "/assets/img/product/default.jpg")
                .sku(request.getSku())
                .weight(request.getWeight())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .isActive(true)
                .build();

        product = productRepository.save(product);
        
        return ResponseEntity.ok(convertToResponse(product));
    }

    /**
     * Cập nhật sản phẩm
     */
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody AdminProductRequest request
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        if (request.getName() != null) {
            product.setName(request.getName());
            product.setSlug(generateSlug(request.getName()));
        }
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getShortDescription() != null) product.setShortDescription(request.getShortDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getDiscountPrice() != null) product.setDiscountPrice(request.getDiscountPrice());
        if (request.getStockQuantity() != null) product.setStockQuantity(request.getStockQuantity());
        if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());
        if (request.getSku() != null) product.setSku(request.getSku());
        if (request.getWeight() != null) product.setWeight(request.getWeight());
        if (request.getIsFeatured() != null) product.setIsFeatured(request.getIsFeatured());
        if (request.getIsActive() != null) product.setIsActive(request.getIsActive());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId()).orElse(null);
            product.setBrand(brand);
        }

        product = productRepository.save(product);
        
        return ResponseEntity.ok(convertToResponse(product));
    }

    /**
     * Xóa sản phẩm (soft delete - set isActive = false)
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        product.setIsActive(false);
        productRepository.save(product);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Đã xóa sản phẩm");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Cập nhật số lượng tồn kho
     */
    @PutMapping("/{id}/stock")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity
    ) {
        log.info("Updating stock for product {} to quantity {}", id, quantity);
        
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            product.setStockQuantity(quantity);
            product.setIsOutOfStock(quantity <= 0);
            Product saved = productRepository.save(product);
            
            log.info("Stock updated successfully. New quantity: {}", saved.getStockQuantity());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã cập nhật tồn kho: " + quantity);
            response.put("stockQuantity", saved.getStockQuantity());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating stock for product {}: {}", id, e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi cập nhật tồn kho: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Toggle featured status
     */
    @PutMapping("/{id}/featured")
    @Transactional
    public ResponseEntity<Map<String, Object>> toggleFeatured(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        product.setIsFeatured(!product.getIsFeatured());
        productRepository.save(product);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", product.getIsFeatured() ? "Đã đánh dấu nổi bật" : "Đã bỏ đánh dấu nổi bật");
        response.put("isFeatured", product.getIsFeatured());
        
        return ResponseEntity.ok(response);
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    private ProductResponse convertToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .isFeatured(product.getIsFeatured())
                .isActive(product.getIsActive())
                .build();
    }
}
