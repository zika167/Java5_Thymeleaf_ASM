package poly.edu.java5_asm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.dto.request.CreateReviewRequest;
import poly.edu.java5_asm.dto.response.ReviewResponse;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.security.CustomUserDetails;
import poly.edu.java5_asm.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Tạo đánh giá mới
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            Authentication authentication,
            @Valid @RequestBody CreateReviewRequest request) {
        try {
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            ReviewResponse review = reviewService.createReview(user, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (RuntimeException e) {
            log.error("Lỗi khi tạo đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Lỗi khi tạo đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cập nhật đánh giá
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            Authentication authentication,
            @PathVariable Long reviewId,
            @Valid @RequestBody CreateReviewRequest request) {
        try {
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            ReviewResponse review = reviewService.updateReview(user, reviewId, request);
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            log.error("Lỗi khi cập nhật đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa đánh giá
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            Authentication authentication,
            @PathVariable Long reviewId) {
        try {
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            reviewService.deleteReview(user, reviewId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Lỗi khi xóa đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Lỗi khi xóa đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy tất cả đánh giá của sản phẩm
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(
            @PathVariable Long productId) {
        try {
            List<ReviewResponse> reviews = reviewService.getProductReviews(productId);
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            log.error("Lỗi khi lấy đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Lỗi khi lấy đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy đánh giá của sản phẩm với phân trang
     */
    @GetMapping("/product/{productId}/paginated")
    public ResponseEntity<Page<ReviewResponse>> getProductReviewsPaginated(
            @PathVariable Long productId,
            Pageable pageable) {
        try {
            Page<ReviewResponse> reviews = reviewService.getProductReviewsPaginated(productId, pageable);
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            log.error("Lỗi khi lấy đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Lỗi khi lấy đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy đánh giá của user
     */
    @GetMapping("/user")
    public ResponseEntity<List<ReviewResponse>> getUserReviews(
            Authentication authentication) {
        try {
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            List<ReviewResponse> reviews = reviewService.getUserReviews(user);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            log.error("Lỗi khi lấy đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy trung bình rating của sản phẩm
     */
    @GetMapping("/product/{productId}/average-rating")
    public ResponseEntity<Double> getProductAverageRating(
            @PathVariable Long productId) {
        try {
            Double averageRating = reviewService.getProductAverageRating(productId);
            return ResponseEntity.ok(averageRating);
        } catch (RuntimeException e) {
            log.error("Lỗi khi lấy trung bình rating: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Lỗi khi lấy trung bình rating: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy số lượng đánh giá của sản phẩm
     */
    @GetMapping("/product/{productId}/count")
    public ResponseEntity<Long> getProductReviewCount(
            @PathVariable Long productId) {
        try {
            Long count = reviewService.getProductReviewCount(productId);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            log.error("Lỗi khi lấy số lượng đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Lỗi khi lấy số lượng đánh giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
