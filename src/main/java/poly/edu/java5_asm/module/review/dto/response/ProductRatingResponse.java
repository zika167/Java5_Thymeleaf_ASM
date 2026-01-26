package poly.edu.java5_asm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO Response cho thống kê rating của sản phẩm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRatingResponse {

    private Long productId;
    private Double averageRating;
    private Long totalReviews;

    // Phân bố rating: key = số sao (1-5), value = số lượng reviews
    private Map<Integer, Long> ratingDistribution;

    // Phần trăm cho mỗi mức rating
    private Map<Integer, Double> ratingPercentage;
}
