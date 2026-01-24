package poly.edu.java5_asm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import poly.edu.java5_asm.dto.request.CreateReviewRequest;
import poly.edu.java5_asm.dto.response.ProductRatingResponse;
import poly.edu.java5_asm.dto.response.ReviewListResponse;
import poly.edu.java5_asm.dto.response.ReviewResponse;
import poly.edu.java5_asm.entity.Order;
import poly.edu.java5_asm.entity.Product;
import poly.edu.java5_asm.entity.Review;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.OrderRepository;
import poly.edu.java5_asm.repository.ProductRepository;
import poly.edu.java5_asm.repository.ReviewRepository;
import poly.edu.java5_asm.repository.UserRepository;

/**
 * Service xử lý logic nghiệp vụ cho Review
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    /**
     * Tạo review mới
     */
    @Transactional
    public ReviewResponse createReview(Long userId, Long productId, CreateReviewRequest request) {
        // Kiểm tra user đã review sản phẩm này chưa
        if (reviewRepository.existsByProductIdAndUserId(productId, userId)) {
            throw new RuntimeException("Bạn đã đánh giá sản phẩm này rồi");
        }

        // Lấy thông tin user và product
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Kiểm tra user đã mua sản phẩm chưa
        boolean hasVerifiedPurchase = hasUserPurchasedProduct(userId, productId);

        // Tạo review mới
        Review review = Review.builder()
                .rating(request.getRating())
                .title(request.getTitle())
                .comment(request.getComment())
                .isVerifiedPurchase(hasVerifiedPurchase)
                .user(user)
                .product(product)
                .build();

        Review savedReview = reviewRepository.save(review);

        return mapToReviewResponse(savedReview);
    }

    /**
     * Lấy danh sách reviews của sản phẩm
     */
    public ReviewListResponse getProductReviews(Long productId, int page, int size, String sortBy) {
        // Validate product tồn tại
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }

        // Tạo pageable với sắp xếp
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        // Lấy reviews với JOIN FETCH để tránh N+1 query
        Page<Review> reviewPage = reviewRepository.findByProductIdWithUserAndProduct(productId, pageable);

        return mapToReviewListResponse(reviewPage);
    }

    /**
     * Lấy danh sách reviews của user
     */
    public ReviewListResponse getUserReviews(Long userId, int page, int size) {
        // Validate user tồn tại
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }

        // Tạo pageable
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        // Lấy reviews với JOIN FETCH để tránh N+1 query
        Page<Review> reviewPage = reviewRepository.findByUserIdWithProduct(userId, pageable);

        return mapToReviewListResponse(reviewPage);
    }

    /**
     * Xóa review
     */
    @Transactional
    public void deleteReview(Long reviewId, Long userId, boolean isAdmin) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));

        // Kiểm tra quyền: chỉ chủ review hoặc admin mới được xóa
        if (!isAdmin && !review.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền xóa đánh giá này");
        }

        reviewRepository.delete(review);
    }

    /**
     * Tính toán rating của sản phẩm (Optimized version)
     */
    public ProductRatingResponse calculateProductRating(Long productId) {
        // Validate product tồn tại
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }

        // Tính average rating
        Double averageRating = reviewRepository.calculateAverageRatingByProductId(productId);
        if (averageRating == null) {
            averageRating = 0.0;
        }

        // Đếm tổng số reviews
        long totalReviews = reviewRepository.countByProductId(productId);

        // Lấy phân bố rating trong 1 query duy nhất (thay vì 5 queries)
        @SuppressWarnings("unchecked")
        List<ReviewRepository.RatingDistribution> distributions = 
            (List<ReviewRepository.RatingDistribution>) (List<?>) reviewRepository.getRatingDistribution(productId);
        
        // Tạo maps cho rating distribution và percentage
        Map<Integer, Long> ratingDistribution = new HashMap<>();
        Map<Integer, Double> ratingPercentage = new HashMap<>();

        // Initialize với 0 cho tất cả ratings
        for (int i = 1; i <= 5; i++) {
            ratingDistribution.put(i, 0L);
            ratingPercentage.put(i, 0.0);
        }

        // Fill data từ query result
        for (ReviewRepository.RatingDistribution dist : distributions) {
            ratingDistribution.put(dist.getRating(), dist.getCount());
            
            // Tính phần trăm
            double percentage = totalReviews > 0 ? (dist.getCount() * 100.0 / totalReviews) : 0.0;
            ratingPercentage.put(dist.getRating(), Math.round(percentage * 10.0) / 10.0);
        }

        return ProductRatingResponse.builder()
                .productId(productId)
                .averageRating(Math.round(averageRating * 10.0) / 10.0)
                .totalReviews(totalReviews)
                .ratingDistribution(ratingDistribution)
                .ratingPercentage(ratingPercentage)
                .build();
    }

    /**
     * Kiểm tra user đã review sản phẩm chưa
     */
    public boolean hasUserReviewedProduct(Long userId, Long productId) {
        return reviewRepository.existsByProductIdAndUserId(productId, userId);
    }

    /**
     * Kiểm tra user đã mua sản phẩm chưa (verified purchase)
     */
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        // Tìm các đơn hàng đã hoàn thành của user có chứa sản phẩm này
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .filter(order -> Order.OrderStatus.DELIVERED.equals(order.getStatus()))
                .flatMap(order -> order.getOrderItems().stream())
                .anyMatch(orderItem -> orderItem.getProduct().getId().equals(productId));
    }

    /**
     * Map Review entity sang ReviewResponse DTO
     */
    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .isVerifiedPurchase(review.getIsVerifiedPurchase())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .userId(review.getUser().getId())
                .userName(review.getUser().getFullName() != null ? review.getUser().getFullName() : review.getUser().getUsername())
                .userAvatar(review.getUser().getAvatarUrl())
                .productId(review.getProduct().getId())
                .productName(review.getProduct().getName())
                .productImage(review.getProduct().getImageUrl())
                .build();
    }

    /**
     * Map Page<Review> sang ReviewListResponse
     */
    private ReviewListResponse mapToReviewListResponse(Page<Review> reviewPage) {
        List<ReviewResponse> reviews = reviewPage.getContent().stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());

        return ReviewListResponse.builder()
                .reviews(reviews)
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .currentPage(reviewPage.getNumber())
                .pageSize(reviewPage.getSize())
                .build();
    }
}
