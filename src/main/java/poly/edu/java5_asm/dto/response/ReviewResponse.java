package poly.edu.java5_asm.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Response cho Review
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private Integer rating;
    private String title;
    private String comment;
    private Boolean isVerifiedPurchase;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Thông tin người đánh giá
    private Long userId;
    private String userName;
    private String userAvatar;

    // Thông tin sản phẩm (cho trường hợp lấy reviews của user)
    private Long productId;
    private String productName;
    private String productImage;
}
