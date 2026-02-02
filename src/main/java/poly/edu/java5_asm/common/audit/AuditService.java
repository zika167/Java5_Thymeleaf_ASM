package poly.edu.java5_asm.common.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import poly.edu.java5_asm.common.security.CustomUserDetails;
import poly.edu.java5_asm.common.audit.AuditLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service xử lý Audit Logging
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Ghi log audit (async để không block request)
     */
    @Async
    @Transactional
    public void log(AuditAction action, String entityType, Long entityId, String description) {
        try {
            AuditLog auditLog = buildAuditLog(action, entityType, entityId, description);
            auditLogRepository.save(auditLog);
            log.debug("Audit log saved: {} - {} - {}", action, entityType, entityId);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage());
        }
    }

    /**
     * Ghi log với old/new value (cho update operations)
     */
    @Async
    @Transactional
    public void logWithValues(AuditAction action, String entityType, Long entityId,
                              String description, String oldValue, String newValue) {
        try {
            AuditLog auditLog = buildAuditLog(action, entityType, entityId, description);
            auditLog.setOldValue(oldValue);
            auditLog.setNewValue(newValue);
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage());
        }
    }

    /**
     * Ghi log lỗi
     */
    @Async
    @Transactional
    public void logError(AuditAction action, String entityType, Long entityId,
                         String description, String errorMessage) {
        try {
            AuditLog auditLog = buildAuditLog(action, entityType, entityId, description);
            auditLog.setStatus("FAILED");
            auditLog.setErrorMessage(errorMessage);
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage());
        }
    }

    /**
     * Ghi log authentication
     */
    @Transactional
    public void logAuth(AuditAction action, String username, String description, boolean success) {
        try {
            AuditLog.AuditLogBuilder builder = AuditLog.builder()
                    .action(action)
                    .username(username)
                    .description(description)
                    .status(success ? "SUCCESS" : "FAILED");

            addRequestInfo(builder);
            auditLogRepository.save(builder.build());
        } catch (Exception e) {
            log.error("Failed to save auth audit log: {}", e.getMessage());
        }
    }

    /**
     * Build AuditLog với thông tin từ context
     */
    private AuditLog buildAuditLog(AuditAction action, String entityType, Long entityId, String description) {
        AuditLog.AuditLogBuilder builder = AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .description(description)
                .status("SUCCESS");

        // Lấy thông tin user từ SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            builder.userId(userDetails.getUser().getId());
            builder.username(userDetails.getUsername());
        }

        // Lấy thông tin request
        addRequestInfo(builder);

        return builder.build();
    }

    /**
     * Thêm thông tin request vào audit log
     */
    private void addRequestInfo(AuditLog.AuditLogBuilder builder) {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                builder.ipAddress(getClientIp(request));
                builder.userAgent(request.getHeader("User-Agent"));
                builder.requestUrl(request.getRequestURI());
                builder.requestMethod(request.getMethod());
            }
        } catch (Exception e) {
            log.debug("Could not get request info: {}", e.getMessage());
        }
    }

    /**
     * Lấy IP thực của client (xử lý proxy/load balancer)
     */
    private String getClientIp(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    // ==================== Query Methods ====================

    /**
     * Lấy audit logs theo user
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getLogsByUser(Long userId, Pageable pageable) {
        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    /**
     * Lấy audit logs theo action
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getLogsByAction(AuditAction action, Pageable pageable) {
        return auditLogRepository.findByActionOrderByCreatedAtDesc(action, pageable);
    }

    /**
     * Lấy audit logs theo entity
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId);
    }

    /**
     * Tìm kiếm audit logs
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> searchLogs(Long userId, AuditAction action, String entityType,
                                     LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.searchAuditLogs(userId, action, entityType, startDate, endDate, pageable);
    }

    /**
     * Kiểm tra login failures gần đây (cho brute force detection)
     */
    @Transactional(readOnly = true)
    public int countRecentLoginFailures(String username, int minutes) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(minutes);
        return auditLogRepository.findRecentLoginFailures(username, since).size();
    }
}
