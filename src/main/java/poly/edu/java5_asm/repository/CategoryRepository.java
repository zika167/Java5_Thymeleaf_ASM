package poly.edu.java5_asm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * Repository cho Category - Xử lý truy vấn database
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Tìm theo slug
    Optional<Category> findBySlug(String slug);

    // Lấy tất cả categories đang hoạt động
    List<Category> findByIsActiveTrueOrderByDisplayOrderAsc();

    // Lấy categories cha (không có parent)
    List<Category> findByParentIsNullAndIsActiveTrueOrderByDisplayOrderAsc();

    // Lấy categories con theo parent
    List<Category> findByParentIdAndIsActiveTrueOrderByDisplayOrderAsc(Long parentId);

    // Kiểm tra tồn tại theo slug
    boolean existsBySlug(String slug);

    // Đếm số lượng categories
    long countByIsActiveTrue();
}
