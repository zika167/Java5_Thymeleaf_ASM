package poly.edu.java5_asm.module.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.brand.repository.BrandRepository;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Admin API Controller cho quản lý Brand
 */
@RestController
@RequestMapping("/api/admin/brands")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminBrandController {

    private final BrandRepository brandRepository;

    /**
     * Lấy tất cả brands (bao gồm cả inactive)
     */
    @GetMapping
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lấy brand theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getBrand(@PathVariable Long id) {
        return brandRepository.findById(id)
                .map(brand -> ResponseEntity.ok(toResponse(brand)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Tạo brand mới
     */
    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody BrandRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên thương hiệu không được để trống"));
        }

        String slug = generateSlug(request.name());
        if (brandRepository.existsBySlug(slug)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thương hiệu đã tồn tại"));
        }

        Brand brand = Brand.builder()
                .name(request.name().trim())
                .slug(slug)
                .logoUrl(request.logoUrl())
                .description(request.description())
                .isActive(request.isActive() != null ? request.isActive() : true)
                .build();

        brand = brandRepository.save(brand);
        return ResponseEntity.ok(Map.of("message", "Đã thêm thương hiệu mới", "brand", toResponse(brand)));
    }

    /**
     * Cập nhật brand
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @RequestBody BrandRequest request) {
        return brandRepository.findById(id)
                .map(brand -> {
                    if (request.name() != null && !request.name().isBlank()) {
                        brand.setName(request.name().trim());
                        String newSlug = generateSlug(request.name());
                        if (!newSlug.equals(brand.getSlug()) && brandRepository.existsBySlug(newSlug)) {
                            return ResponseEntity.badRequest().body(Map.of("message", "Slug đã tồn tại"));
                        }
                        brand.setSlug(newSlug);
                    }
                    if (request.logoUrl() != null) brand.setLogoUrl(request.logoUrl());
                    if (request.description() != null) brand.setDescription(request.description());
                    if (request.isActive() != null) brand.setIsActive(request.isActive());

                    brandRepository.save(brand);
                    return ResponseEntity.ok(Map.of("message", "Đã cập nhật thương hiệu", "brand", toResponse(brand)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Toggle trạng thái active
     */
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id) {
        return brandRepository.findById(id)
                .map(brand -> {
                    brand.setIsActive(!brand.getIsActive());
                    brandRepository.save(brand);
                    String status = brand.getIsActive() ? "kích hoạt" : "vô hiệu hóa";
                    return ResponseEntity.ok(Map.of("message", "Đã " + status + " thương hiệu"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Xóa brand
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        return brandRepository.findById(id)
                .map(brand -> {
                    if (!brand.getProducts().isEmpty()) {
                        return ResponseEntity.badRequest().body(Map.of("message", "Không thể xóa thương hiệu đang có sản phẩm"));
                    }
                    brandRepository.delete(brand);
                    return ResponseEntity.ok(Map.of("message", "Đã xóa thương hiệu"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private BrandResponse toResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getSlug(),
                brand.getLogoUrl(),
                brand.getDescription(),
                brand.getIsActive(),
                brand.getCreatedAt(),
                brand.getProducts() != null ? brand.getProducts().size() : 0
        );
    }

    private String generateSlug(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String slug = pattern.matcher(normalized).replaceAll("");
        slug = slug.toLowerCase().replaceAll("[đĐ]", "d");
        slug = slug.replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-").replaceAll("-+", "-");
        return slug.replaceAll("^-|-$", "");
    }

    public record BrandRequest(String name, String logoUrl, String description, Boolean isActive) {}
    public record BrandResponse(Long id, String name, String slug, String logoUrl, String description, Boolean isActive, java.time.LocalDateTime createdAt, int productCount) {}
}
