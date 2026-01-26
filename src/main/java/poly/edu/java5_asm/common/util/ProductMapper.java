package poly.edu.java5_asm.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import poly.edu.java5_asm.dto.response.BrandResponse;
import poly.edu.java5_asm.dto.response.CategoryResponse;
import poly.edu.java5_asm.dto.response.ProductListResponse;
import poly.edu.java5_asm.dto.response.ProductResponse;
import poly.edu.java5_asm.entity.Brand;
import poly.edu.java5_asm.entity.Category;
import poly.edu.java5_asm.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper chuyển đổi Entity sang DTO
 * Tránh trả về toàn bộ Entity (gây circular reference và lộ data nhạy cảm)
 */
@Component
public class ProductMapper {

    // Chuyển Product Entity → ProductResponse DTO
    public ProductResponse toResponse(Product product) {
        if (product == null) return null;

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .shortDescription(product.getShortDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .imageUrl(product.getImageUrl())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .averageRating(calculateAverageRating(product))
                .totalReviews(product.getReviews() != null ? product.getReviews().size() : 0)
                .isInStock(product.getIsActive() && !product.getIsOutOfStock())
                .isFeatured(product.getIsFeatured())
                .build();
    }

    // Chuyển List<Product> → List<ProductResponse>
    public List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null) return List.of();
        
        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Chuyển Page<Product> → ProductListResponse (có phân trang)
    public ProductListResponse toProductListResponse(Page<Product> productPage) {
        if (productPage == null) return null;

        return ProductListResponse.builder()
                .products(toResponseList(productPage.getContent()))
                .currentPage(productPage.getNumber())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .pageSize(productPage.getSize())
                .build();
    }

    // Chuyển Category Entity → CategoryResponse DTO
    public CategoryResponse toCategoryResponse(Category category, Long productCount) {
        if (category == null) return null;

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .iconUrl(category.getIconUrl())
                .productCount(productCount)
                .build();
    }

    // Chuyển Brand Entity → BrandResponse DTO
    public BrandResponse toBrandResponse(Brand brand, Long productCount) {
        if (brand == null) return null;

        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .logoUrl(brand.getLogoUrl())
                .productCount(productCount)
                .build();
    }

    // Tính điểm đánh giá trung bình
    private Double calculateAverageRating(Product product) {
        if (product.getReviews() == null || product.getReviews().isEmpty()) {
            return 0.0;
        }
        
        return product.getReviews().stream()
                .mapToInt(review -> review.getRating())
                .average()
                .orElse(0.0);
    }
}
