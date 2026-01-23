package poly.edu.java5_asm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.entity.Brand;

import java.util.List;
import java.util.Optional;

/**
 * Repository cho Brand - Xử lý truy vấn database
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    // Tìm theo slug
    Optional<Brand> findBySlug(String slug);

    // Lấy tất cả brands đang hoạt động
    List<Brand> findByIsActiveTrueOrderByNameAsc();

    // Tìm theo tên
    Optional<Brand> findByNameIgnoreCase(String name);

    // Kiểm tra tồn tại theo slug
    boolean existsBySlug(String slug);

    // Đếm số lượng brands
    long countByIsActiveTrue();
}
