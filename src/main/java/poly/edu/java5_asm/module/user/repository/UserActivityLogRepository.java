package poly.edu.java5_asm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.entity.UserActivityLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository cho UserActivityLog - Xử lý truy vấn log hoạt động
 */
@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

    // Đếm tổng số page views trong khoảng thời gian
    @Query("SELECT COUNT(l) FROM UserActivityLog l WHERE l.activityType = 'PAGE_VIEW' " +
           "AND l.createdAt BETWEEN :startDate AND :endDate")
    Long countPageViewsBetween(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);

    // Đếm số người truy cập duy nhất (unique visitors) trong khoảng thời gian
    @Query("SELECT COUNT(DISTINCT l.sessionId) FROM UserActivityLog l " +
           "WHERE l.createdAt BETWEEN :startDate AND :endDate")
    Long countUniqueVisitorsBetween(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);

    // Đếm số lần đăng nhập trong khoảng thời gian
    @Query("SELECT COUNT(l) FROM UserActivityLog l WHERE l.activityType = 'LOGIN' " +
           "AND l.createdAt BETWEEN :startDate AND :endDate")
    Long countLoginsBetween(@Param("startDate") LocalDateTime startDate, 
                            @Param("endDate") LocalDateTime endDate);

    // Đếm số lượt xem sản phẩm trong khoảng thời gian
    @Query("SELECT COUNT(l) FROM UserActivityLog l WHERE l.activityType = 'PRODUCT_VIEW' " +
           "AND l.createdAt BETWEEN :startDate AND :endDate")
    Long countProductViewsBetween(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);

    // Đếm số lần tìm kiếm trong khoảng thời gian
    @Query("SELECT COUNT(l) FROM UserActivityLog l WHERE l.activityType = 'SEARCH' " +
           "AND l.createdAt BETWEEN :startDate AND :endDate")
    Long countSearchesBetween(@Param("startDate") LocalDateTime startDate, 
                               @Param("endDate") LocalDateTime endDate);

    // Đếm số lần thêm vào giỏ trong khoảng thời gian
    @Query("SELECT COUNT(l) FROM UserActivityLog l WHERE l.activityType = 'ADD_TO_CART' " +
           "AND l.createdAt BETWEEN :startDate AND :endDate")
    Long countAddToCartsBetween(@Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate);

    // Đếm số lần checkout trong khoảng thời gian
    @Query("SELECT COUNT(l) FROM UserActivityLog l WHERE l.activityType = 'CHECKOUT' " +
           "AND l.createdAt BETWEEN :startDate AND :endDate")
    Long countCheckoutsBetween(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);

    // Đếm tổng số hoạt động theo loại
    Long countByActivityType(UserActivityLog.ActivityType activityType);

    // Lấy logs trong khoảng thời gian
    List<UserActivityLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Lấy logs của user trong khoảng thời gian
    List<UserActivityLog> findByUserIdAndCreatedAtBetween(Long userId, 
                                                           LocalDateTime startDate, 
                                                           LocalDateTime endDate);
}
