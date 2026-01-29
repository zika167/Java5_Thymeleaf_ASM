package poly.edu.java5_asm.module.brand.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.brand.repository.BrandRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API Controller cho Brand
 */
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandRepository brandRepository;

    /**
     * Lấy danh sách tất cả brands đang hoạt động
     */
    @GetMapping
    public List<BrandDTO> getAllBrands() {
        return brandRepository.findByIsActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BrandDTO toDTO(Brand brand) {
        return new BrandDTO(
                brand.getId(),
                brand.getName(),
                brand.getSlug(),
                brand.getLogoUrl()
        );
    }

    public record BrandDTO(Long id, String name, String slug, String logoUrl) {}
}
