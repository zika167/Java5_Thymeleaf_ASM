package poly.edu.java5_asm.module.product.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * DTO Response cho Product - Trả về thông tin sản phẩm
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private Integer stockQuantity;
    private Double averageRating;
    private Integer totalReviews;
    private Boolean isInStock;
    private Boolean isFeatured;
    private Boolean isActive;
}
