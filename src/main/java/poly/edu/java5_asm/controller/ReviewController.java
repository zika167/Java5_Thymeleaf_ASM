package poly.edu.java5_asm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.dto.request.CreateReviewRequest;
import poly.edu.java5_asm.dto.response.ProductRatingResponse;
import poly.edu.java5_asm.dto.response.ReviewListResponse;
import poly.edu.java5_asm.dto.response.ReviewResponse;
import poly.edu.java5_asm.security.CustomUserDetails;
import poly.edu.java5_asm.service.ReviewService;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller cho Review
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Tạo review mới cho sản phẩm
     * POST /api/reviews/products/{productId}
     */
    @PostMapping("/products/{productId}")
    public ResponseEntity<?> createReview(
            @PathVariable Long productId,
            @Valid @RequestBody CreateReviewRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập để đánh giá sản phẩm"));
        }

        try {
            ReviewResponse review = reviewService.createReview(
                    userDetails.getUserId(),
                    productId,
                    request
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Lấy danh sách reviews của sản phẩm
     * GET /api/reviews/products/{productId}
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy) {

        try {
            ReviewListResponse reviews = reviewService.getProductReviews(productId, page, size, sortBy);
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Lấy danh sách reviews của user hiện tại
     * GET /api/reviews/my-reviews
     */
    @GetMapping("/my-reviews")
    public ResponseEntity<?> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        try {
            ReviewListResponse reviews = reviewService.getUserReviews(
                    userDetails.getUserId(),
                    page,
                    size
            );
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Lấy danh sách reviews của một user cụ thể (public)
     * GET /api/reviews/users/{userId}
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserReviews(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            ReviewListResponse reviews = reviewService.getUserReviews(userId, page, size);
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Xóa review
     * DELETE /api/reviews/{reviewId}
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        try {
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            reviewService.deleteReview(reviewId, userDetails.getUserId(), isAdmin);

            return ResponseEntity.ok(Map.of("message", "Xóa đánh giá thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Lấy thống kê rating của sản phẩm
     * GET /api/reviews/products/{productId}/rating
     */
    @GetMapping("/products/{productId}/rating")
    public ResponseEntity<?> getProductRating(@PathVariable Long productId) {
        try {
            ProductRatingResponse rating = reviewService.calculateProductRating(productId);
            return ResponseEntity.ok(rating);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Kiểm tra user đã review sản phẩm chưa
     * GET /api/reviews/products/{productId}/check
     */
    @GetMapping("/products/{productId}/check")
    public ResponseEntity<?> checkUserReview(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.ok(Map.of("hasReviewed", false));
        }

        boolean hasReviewed = reviewService.hasUserReviewedProduct(
                userDetails.getUserId(),
                productId
        );

        boolean hasPurchased = reviewService.hasUserPurchasedProduct(
                userDetails.getUserId(),
                productId
        );

        Map<String, Object> response = new HashMap<>();
        response.put("hasReviewed", hasReviewed);
        response.put("hasPurchased", hasPurchased);

        return ResponseEntity.ok(response);
    }
}
