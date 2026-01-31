package poly.edu.java5_asm.module.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Tìm đơn hàng theo mã đơn hàng
    Optional<Order> findByOrderNumber(String orderNumber);

    // Tìm tất cả đơn hàng của user
    List<Order> findByUserOrderByOrderedAtDesc(User user);

    // Tìm tất cả đơn hàng của user theo ID
    List<Order> findByUserId(Long userId);

    // Tìm đơn hàng của user với phân trang
    Page<Order> findByUserOrderByOrderedAtDesc(User user, Pageable pageable);

    // Tìm đơn hàng theo trạng thái
    List<Order> findByStatusOrderByOrderedAtDesc(Order.OrderStatus status);

    // Tìm đơn hàng theo trạng thái thanh toán
    List<Order> findByPaymentStatusOrderByOrderedAtDesc(Order.PaymentStatus paymentStatus);

    // Tìm đơn hàng trong khoảng thời gian
    List<Order> findByOrderedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Admin: Tìm tất cả đơn hàng với phân trang
    Page<Order> findAllByOrderByOrderedAtDesc(Pageable pageable);
    
    // Admin: Tìm đơn hàng theo status với phân trang
    Page<Order> findByStatusOrderByOrderedAtDesc(Order.OrderStatus status, Pageable pageable);
}
