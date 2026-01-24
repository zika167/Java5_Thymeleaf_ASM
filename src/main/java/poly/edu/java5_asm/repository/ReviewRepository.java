package poly.edu.java5_asm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.edu.java5_asm.entity.Review;

/**
 * Repository cho Review
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Lấy tất cả reviews của một sản phẩm
     */
    Page<Review> findByProductId(Long productId, Pageable pageable);

    /**
     * Lấy tất cả reviews của một sản phẩm với JOIN FETCH (tối ưu N+1 query)
     */
    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.user " +
           "JOIN FETCH r.product " +
           "WHERE r.product.id = :productId")
    Page<Review> findByProductIdWithUserAndProduct(@Param("productId") Long productId, Pageable pageable);

    /**
     * Lấy tất cả reviews của một user
     */
    Page<Review> findByUserId(Long userId, Pageable pageable);

    /**
     * Lấy tất cả reviews của một user với JOIN FETCH (tối ưu N+1 query)
     */
    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.user " +
           "JOIN FETCH r.product " +
           "WHERE r.user.id = :userId")
    Page<Review> findByUserIdWithProduct(@Param("userId") Long userId, Pageable pageable);

    /**
     * Tìm review của user cho một sản phẩm cụ thể
     */
    Optional<Review> findByProductIdAndUserId(Long productId, Long userId);

    /**
     * Kiểm tra user đã review sản phẩm chưa
     */
    boolean existsByProductIdAndUserId(Long productId, Long userId);

    /**
     * Đếm số lượng reviews của một sản phẩm
     */
    long countByProductId(Long productId);

    /**
     * Tính điểm trung bình của sản phẩm
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double calculateAverageRatingByProductId(@Param("productId") Long productId);

    /**
     * Đếm số lượng reviews theo từng mức rating
     */
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId AND r.rating = :rating")
    long countByProductIdAndRating(@Param("productId") Long productId, @Param("rating") Integer rating);

    /**
     * Lấy phân bố rating trong một query duy nhất (tối ưu)
     */
    @Query("SELECT r.rating as rating, COUNT(r) as count " +
           "FROM Review r " +
           "WHERE r.product.id = :productId " +
           "GROUP BY r.rating")
    List<RatingDistribution> getRatingDistribution(@Param("productId") Long productId);

    /**
     * Interface projection cho rating distribution
     */
    interface RatingDistribution {
        Integer getRating();
        Long getCount();
    }

    /**
     * Xóa review theo ID và User ID (để đảm bảo chỉ chủ review mới xóa được)
     */
    void deleteByIdAndUserId(Long id, Long userId);

    /**
     * Lấy reviews verified purchase của sản phẩm
     */
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.isVerifiedPurchase = true")
    Page<Review> findVerifiedPurchaseReviews(@Param("productId") Long productId, Pageable pageable);
}
