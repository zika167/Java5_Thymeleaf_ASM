package poly.edu.java5_asm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.entity.Product;
import poly.edu.java5_asm.entity.Review;
import poly.edu.java5_asm.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Tìm tất cả review của sản phẩm
    List<Review> findByProductOrderByCreatedAtDesc(Product product);

    // Tìm review của sản phẩm với phân trang
    Page<Review> findByProductOrderByCreatedAtDesc(Product product, Pageable pageable);

    // Tìm review của user cho sản phẩm
    Optional<Review> findByProductAndUser(Product product, User user);

    // Tìm tất cả review của user
    List<Review> findByUserOrderByCreatedAtDesc(User user);

    // Đếm review của sản phẩm
    Long countByProduct(Product product);

    // Tính trung bình rating của sản phẩm
    Double findAverageRatingByProduct(Product product);
}
