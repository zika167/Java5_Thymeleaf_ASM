package poly.edu.java5_asm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.entity.Wishlist;

import java.util.List;
import java.util.Optional;

/**
 * Repository cho Wishlist
 */
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    /**
     * Tìm wishlist item cụ thể của user cho 1 sản phẩm
     */
    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * Kiểm tra sản phẩm có trong wishlist của user không
     */
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    /**
     * Lấy tất cả wishlist của user (basic)
     */
    List<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Lấy wishlist của user với pagination
     */
    Page<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Lấy wishlist với JOIN FETCH (tối ưu N+1 query)
     */
    @Query("SELECT w FROM Wishlist w " +
            "JOIN FETCH w.product p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.brand " +
            "WHERE w.user.id = :userId " +
            "ORDER BY w.createdAt DESC")
    List<Wishlist> findByUserIdWithProduct(@Param("userId") Long userId);

    /**
     * Lấy wishlist với pagination và JOIN FETCH
     */
    @Query("SELECT w FROM Wishlist w " +
            "JOIN FETCH w.product p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.brand " +
            "WHERE w.user.id = :userId " +
            "ORDER BY w.createdAt DESC")
    Page<Wishlist> findByUserIdWithProductPaginated(@Param("userId") Long userId, Pageable pageable);

    /**
     * Đếm số sản phẩm trong wishlist của user
     */
    long countByUserId(Long userId);

    /**
     * Xóa tất cả wishlist của user
     */
    void deleteByUserId(Long userId);

    /**
     * Xóa wishlist item cụ thể
     */
    void deleteByUserIdAndProductId(Long userId, Long productId);

    /**
     * Lấy danh sách product IDs trong wishlist của user
     */
    @Query("SELECT w.product.id FROM Wishlist w WHERE w.user.id = :userId")
    List<Long> findProductIdsByUserId(@Param("userId") Long userId);
}
