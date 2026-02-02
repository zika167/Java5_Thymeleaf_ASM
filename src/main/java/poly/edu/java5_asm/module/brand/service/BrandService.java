package poly.edu.java5_asm.module.brand.service;

import poly.edu.java5_asm.module.brand.dto.response.BrandResponse;

import java.util.List;

/**
 * Interface cho Brand Service
 */
public interface BrandService {

    List<BrandResponse> getAllActiveBrands();

    BrandResponse getBrandById(Long id);

    BrandResponse getBrandBySlug(String slug);

    long countActiveBrands();
}
