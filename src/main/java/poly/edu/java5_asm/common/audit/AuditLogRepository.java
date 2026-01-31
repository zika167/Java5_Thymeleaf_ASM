package poly.edu.java5_asm.common.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository cho Audit Log
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Tìm theo user
    Page<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<AuditLog> findByUsernameOrderByCreatedAtDesc(String username);

    // Tìm theo action
    Page<AuditLog> findByActionOrderByCreatedAtDesc(AuditAction action, Pageable pageable);
    
    // Tìm theo entity
    List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, Long entityId);

    // Tìm theo khoảng thời gian
    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    Page<AuditLog> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // Tìm theo nhiều điều kiện
    @Query("SELECT a FROM AuditLog a WHERE " +
            "(:userId IS NULL OR a.userId = :userId) AND " +
            "(:action IS NULL OR a.action = :action) AND " +
            "(:entityType IS NULL OR a.entityType = :entityType) AND " +
            "(:startDate IS NULL OR a.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR a.createdAt <= :endDate) " +
            "ORDER BY a.createdAt DESC")
    Page<AuditLog> searchAuditLogs(
            @Param("userId") Long userId,
            @Param("action") AuditAction action,
            @Param("entityType") String entityType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // Đếm theo action trong khoảng thời gian
    @Query("SELECT a.action, COUNT(a) FROM AuditLog a " +
            "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY a.action")
    List<Object[]> countByActionInDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Lấy các login failed gần đây của user
    @Query("SELECT a FROM AuditLog a WHERE a.username = :username AND a.action = 'LOGIN_FAILED' " +
            "AND a.createdAt > :since ORDER BY a.createdAt DESC")
    List<AuditLog> findRecentLoginFailures(
            @Param("username") String username,
            @Param("since") LocalDateTime since);
}
