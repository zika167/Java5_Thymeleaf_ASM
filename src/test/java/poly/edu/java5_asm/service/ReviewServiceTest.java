package poly.edu.java5_asm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import poly.edu.java5_asm.dto.request.CreateReviewRequest;
import poly.edu.java5_asm.dto.response.ProductRatingResponse;
import poly.edu.java5_asm.dto.response.ReviewResponse;
import poly.edu.java5_asm.entity.Product;
import poly.edu.java5_asm.entity.Review;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.OrderRepository;
import poly.edu.java5_asm.repository.ProductRepository;
import poly.edu.java5_asm.repository.ReviewRepository;
import poly.edu.java5_asm.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests cho ReviewService
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User testUser;
    private Product testProduct;
    private CreateReviewRequest testRequest;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .fullName("Test User")
                .email("test@example.com")
                .avatarUrl("/assets/img/avatar.jpg")
                .build();

        // Setup test product
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .imageUrl("/assets/img/product.jpg")
                .build();

        // Setup test request
        testRequest = CreateReviewRequest.builder()
                .rating(5)
                .title("Great product!")
                .comment("I love this product")
                .build();
    }

    @Test
    void testCreateReview_Success() {
        // Given
        when(reviewRepository.existsByProductIdAndUserId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        Review savedReview = Review.builder()
                .id(1L)
                .rating(5)
                .title("Great product!")
                .comment("I love this product")
                .isVerifiedPurchase(false)
                .user(testUser)
                .product(testProduct)
                .build();
        
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        // When
        ReviewResponse response = reviewService.createReview(1L, 1L, testRequest);

        // Then
        assertNotNull(response);
        assertEquals(5, response.getRating());
        assertEquals("Great product!", response.getTitle());
        assertEquals("I love this product", response.getComment());
        assertEquals("Test User", response.getUserName());
        
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void testCreateReview_AlreadyReviewed() {
        // Given
        when(reviewRepository.existsByProductIdAndUserId(1L, 1L)).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.createReview(1L, 1L, testRequest);
        });

        assertEquals("Bạn đã đánh giá sản phẩm này rồi", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testCalculateProductRating_Success() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.calculateAverageRatingByProductId(1L)).thenReturn(4.5);
        when(reviewRepository.countByProductId(1L)).thenReturn(100L);
        
        // Mock rating distribution using the actual interface
        java.util.List<ReviewRepository.RatingDistribution> distributions = java.util.Arrays.asList(
            new ReviewRepository.RatingDistribution() {
                public Integer getRating() { return 5; }
                public Long getCount() { return 50L; }
            },
            new ReviewRepository.RatingDistribution() {
                public Integer getRating() { return 4; }
                public Long getCount() { return 30L; }
            },
            new ReviewRepository.RatingDistribution() {
                public Integer getRating() { return 3; }
                public Long getCount() { return 15L; }
            },
            new ReviewRepository.RatingDistribution() {
                public Integer getRating() { return 2; }
                public Long getCount() { return 3L; }
            },
            new ReviewRepository.RatingDistribution() {
                public Integer getRating() { return 1; }
                public Long getCount() { return 2L; }
            }
        );
        
        when(reviewRepository.getRatingDistribution(1L)).thenReturn(distributions);

        // When
        ProductRatingResponse response = reviewService.calculateProductRating(1L);

        // Then
        assertNotNull(response);
        assertEquals(4.5, response.getAverageRating());
        assertEquals(100L, response.getTotalReviews());
        assertEquals(50L, response.getRatingDistribution().get(5));
        assertEquals(50.0, response.getRatingPercentage().get(5));
    }

    @Test
    void testCalculateProductRating_NoReviews() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.calculateAverageRatingByProductId(1L)).thenReturn(null);
        when(reviewRepository.countByProductId(1L)).thenReturn(0L);
        
        // Mock empty rating distribution
        when(reviewRepository.getRatingDistribution(1L)).thenReturn(java.util.Collections.emptyList());

        // When
        ProductRatingResponse response = reviewService.calculateProductRating(1L);

        // Then
        assertNotNull(response);
        assertEquals(0.0, response.getAverageRating());
        assertEquals(0L, response.getTotalReviews());
    }

    @Test
    void testHasUserReviewedProduct() {
        // Given
        when(reviewRepository.existsByProductIdAndUserId(1L, 1L)).thenReturn(true);

        // When
        boolean result = reviewService.hasUserReviewedProduct(1L, 1L);

        // Then
        assertTrue(result);
    }

    @Test
    void testDeleteReview_Success() {
        // Given
        Review review = Review.builder()
                .id(1L)
                .user(testUser)
                .product(testProduct)
                .build();
        
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // When
        reviewService.deleteReview(1L, 1L, false);

        // Then
        verify(reviewRepository).delete(review);
    }

    @Test
    void testDeleteReview_Unauthorized() {
        // Given
        Review review = Review.builder()
                .id(1L)
                .user(testUser)
                .product(testProduct)
                .build();
        
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.deleteReview(1L, 999L, false); // Different user ID
        });

        assertEquals("Bạn không có quyền xóa đánh giá này", exception.getMessage());
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void testDeleteReview_AdminCanDelete() {
        // Given
        Review review = Review.builder()
                .id(1L)
                .user(testUser)
                .product(testProduct)
                .build();
        
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // When
        reviewService.deleteReview(1L, 999L, true); // Admin can delete any review

        // Then
        verify(reviewRepository).delete(review);
    }
}
