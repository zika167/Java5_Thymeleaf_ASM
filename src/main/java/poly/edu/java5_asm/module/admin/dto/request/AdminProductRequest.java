package poly.edu.java5_asm.module.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO cho Admin Product Management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductRequest {
    private String name;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Long categoryId;
    private Long brandId;
    private Integer stockQuantity;
    private String imageUrl;
    private String sku;
    private String weight;
    private Boolean isFeatured;
    private Boolean isActive;
}
