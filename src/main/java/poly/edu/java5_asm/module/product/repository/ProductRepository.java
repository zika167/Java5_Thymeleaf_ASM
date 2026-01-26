package poly.edu.java5_asm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.entity.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository cho Product - Xử lý truy vấn database
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm theo tên
    List<Product> findByNameContainingIgnoreCase(String name);

    // Tìm theo slug
    Product findBySlug(String slug);

    // Lọc theo category
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Lọc theo brand
    Page<Product> findByBrandId(Long brandId, Pageable pageable);

    // Lọc theo category và brand
    Page<Product> findByCategoryIdAndBrandId(Long categoryId, Long brandId, Pageable pageable);

    // Lọc theo khoảng giá
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Tìm kiếm và lọc tổng hợp
    @Query("SELECT p FROM Product p " +
            "WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:brandId IS NULL OR p.brand.id = :brandId) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND p.isActive = true")
    Page<Product> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );

    // Sản phẩm nổi bật
    Page<Product> findByIsFeaturedTrueAndIsActiveTrue(Pageable pageable);

    // Sản phẩm đang hoạt động
    Page<Product> findByIsActiveTrue(Pageable pageable);

    // Sản phẩm mới nhất
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.createdAt DESC")
    Page<Product> findLatestProducts(Pageable pageable);

    // Sản phẩm bán chạy (theo lượt xem)
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.viewCount DESC")
    Page<Product> findBestSellingProducts(Pageable pageable);

    // Đếm số sản phẩm theo category
    long countByCategoryId(Long categoryId);

    // Đếm số sản phẩm theo brand
    long countByBrandId(Long brandId);
}
