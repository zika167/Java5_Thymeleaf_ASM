package poly.edu.java5_asm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.request.CreateReviewRequest;
import poly.edu.java5_asm.dto.response.ReviewResponse;
import poly.edu.java5_asm.entity.Product;
import poly.edu.java5_asm.entity.Review;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.ProductRepository;
import poly.edu.java5_asm.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(User user, CreateReviewRequest request) {
        // Kiểm tra sản phẩm tồn tại
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Kiểm tra user đã đánh giá sản phẩm này chưa
        if (reviewRepository.findByProductAndUser(product, user).isPresent()) {
            throw new RuntimeException("Bạn đã đánh giá sản phẩm này rồi");
        }

        // Tạo review mới
        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(request.getRating())
                .title(request.getTitle())
                .comment(request.getComment())
                .isVerifiedPurchase(false) // TODO: Kiểm tra user đã mua sản phẩm này chưa
                .build();

        review = reviewRepository.save(review);
        log.info("Tạo đánh giá mới cho sản phẩm {} từ user {}", product.getId(), user.getId());

        return convertToResponse(review);
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(User user, Long reviewId, CreateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Đánh giá không tồn tại"));

        // Kiểm tra quyền sở hữu
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền cập nhật đánh giá này");
        }

        // Cập nhật thông tin
        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setComment(request.getComment());

        review = reviewRepository.save(review);
        log.info("Cập nhật đánh giá {} của user {}", reviewId, user.getId());

        return convertToResponse(review);
    }

    @Override
    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Đánh giá không tồn tại"));

        // Kiểm tra quyền sở hữu
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa đánh giá này");
        }

        reviewRepository.delete(review);
        log.info("Xóa đánh giá {} của user {}", reviewId, user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getProductReviews(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        List<Review> reviews = reviewRepository.findByProductOrderByCreatedAtDesc(product);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getProductReviewsPaginated(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        Page<Review> reviews = reviewRepository.findByProductOrderByCreatedAtDesc(product, pageable);
        return reviews.map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getUserReviews(User user) {
        List<Review> reviews = reviewRepository.findByUserOrderByCreatedAtDesc(user);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getUserProductReview(User user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        Review review = reviewRepository.findByProductAndUser(product, user)
                .orElseThrow(() -> new RuntimeException("Bạn chưa đánh giá sản phẩm này"));

        return convertToResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getProductAverageRating(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        List<Review> reviews = reviewRepository.findByProductOrderByCreatedAtDesc(product);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getProductReviewCount(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        return reviewRepository.countByProduct(product);
    }

    /**
     * Convert Review entity thành ReviewResponse
     */
    private ReviewResponse convertToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .userId(review.getUser().getId())
                .userName(review.getUser().getFullName() != null ?
                        review.getUser().getFullName() : review.getUser().getUsername())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .isVerifiedPurchase(review.getIsVerifiedPurchase())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
