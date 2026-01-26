package poly.edu.java5_asm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import poly.edu.java5_asm.dto.request.CreateReviewRequest;
import poly.edu.java5_asm.dto.response.ReviewResponse;
import poly.edu.java5_asm.entity.User;

import java.util.List;

/**
 * Service interface quản lý đánh giá sản phẩm
 */
public interface ReviewService {

    /**
     * Tạo đánh giá mới
     * - Mỗi user chỉ có thể đánh giá 1 lần cho 1 sản phẩm
     * - Tự động detect verified purchase
     */
    ReviewResponse createReview(User user, CreateReviewRequest request);

    /**
     * Cập nhật đánh giá
     */
    ReviewResponse updateReview(User user, Long reviewId, CreateReviewRequest request);

    /**
     * Xóa đánh giá
     */
    void deleteReview(User user, Long reviewId);

    /**
     * Lấy tất cả đánh giá của sản phẩm
     */
    List<ReviewResponse> getProductReviews(Long productId);

    /**
     * Lấy đánh giá của sản phẩm với phân trang
     */
    Page<ReviewResponse> getProductReviewsPaginated(Long productId, Pageable pageable);

    /**
     * Lấy đánh giá của user
     */
    List<ReviewResponse> getUserReviews(User user);

    /**
     * Lấy đánh giá của user cho sản phẩm
     */
    ReviewResponse getUserProductReview(User user, Long productId);

    /**
     * Lấy trung bình rating của sản phẩm
     */
    Double getProductAverageRating(Long productId);

    /**
     * Lấy số lượng đánh giá của sản phẩm
     */
    Long getProductReviewCount(Long productId);
}
