package poly.edu.java5_asm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO Response cho Wishlist
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistResponse {

    private Long id;
    private LocalDateTime createdAt;

    // Thông tin sản phẩm
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private BigDecimal productDiscountPrice;
    private String productImageUrl;
    private Integer productStock;
    private String productCategoryName;
    private String productBrandName;
}
