package poly.edu.java5_asm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.entity.Cart;
import poly.edu.java5_asm.entity.User;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Tìm giỏ hàng của user
    Optional<Cart> findByUser(User user);

    // Tìm giỏ hàng theo session ID (cho guest user)
    Optional<Cart> findBySessionId(String sessionId);

    // Kiểm tra giỏ hàng tồn tại cho user
    boolean existsByUser(User user);
}
