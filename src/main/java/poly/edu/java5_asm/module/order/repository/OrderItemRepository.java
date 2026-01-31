package poly.edu.java5_asm.module.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.order.entity.OrderItem;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Tìm tất cả items của đơn hàng
    List<OrderItem> findByOrder(Order order);
    
    // Tìm tất cả items theo order ID
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Kiểm tra user đã mua sản phẩm này chưa (đơn hàng đã DELIVERED)
     * Dùng để xác định verified purchase cho review
     */
    @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi " +
           "WHERE oi.order.user.id = :userId " +
           "AND oi.product.id = :productId " +
           "AND oi.order.status = 'DELIVERED'")
    boolean existsByUserIdAndProductIdAndOrderDelivered(@Param("userId") Long userId, 
                                                         @Param("productId") Long productId);
}
