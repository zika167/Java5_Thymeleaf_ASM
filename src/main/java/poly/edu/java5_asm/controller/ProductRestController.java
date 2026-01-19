package poly.edu.java5_asm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.dto.request.ProductSearchRequest;
import poly.edu.java5_asm.dto.response.BrandResponse;
import poly.edu.java5_asm.dto.response.CategoryResponse;
import poly.edu.java5_asm.dto.response.ProductListResponse;
import poly.edu.java5_asm.dto.response.ProductResponse;
import poly.edu.java5_asm.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST API Controller cho Product
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    /**
     * API: Tìm kiếm và lọc sản phẩm
     * GET /api/products/search?keyword=...&categoryId=...&brandId=...
     */
    @GetMapping("/search")
    public ResponseEntity<ProductListResponse> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        ProductSearchRequest request = ProductSearchRequest.builder()
                .keyword(keyword)
                .categoryId(categoryId)
                .brandId(brandId)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        ProductListResponse response = productService.searchAndFilterProducts(request);
        return ResponseEntity.ok(response);
    }

    /**
     * API: Lấy chi tiết sản phẩm
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * API: Lấy tất cả sản phẩm
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        ProductListResponse response = productService.getAllProducts(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    /**
     * API: Lấy sản phẩm nổi bật
     * GET /api/products/featured
     */
    @GetMapping("/featured")
    public ResponseEntity<ProductListResponse> getFeaturedProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size
    ) {
        ProductListResponse response = productService.getFeaturedProducts(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * API: Lấy sản phẩm mới nhất
     * GET /api/products/latest
     */
    @GetMapping("/latest")
    public ResponseEntity<ProductListResponse> getLatestProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size
    ) {
        ProductListResponse response = productService.getLatestProducts(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * API: Lấy sản phẩm bán chạy
     * GET /api/products/best-selling
     */
    @GetMapping("/best-selling")
    public ResponseEntity<ProductListResponse> getBestSellingProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size
    ) {
        ProductListResponse response = productService.getBestSellingProducts(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * API: Lấy danh sách categories
     * GET /api/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = productService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    /**
     * API: Lấy danh sách brands
     * GET /api/brands
     */
    @GetMapping("/brands")
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        List<BrandResponse> response = productService.getAllBrands();
        return ResponseEntity.ok(response);
    }
}
