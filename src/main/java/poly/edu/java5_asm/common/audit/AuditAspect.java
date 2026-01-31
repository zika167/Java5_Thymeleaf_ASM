package poly.edu.java5_asm.common.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspect tự động ghi audit log cho các method được đánh dấu @Auditable
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;

    /**
     * Ghi log khi method thực thi thành công
     */
    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void auditSuccess(JoinPoint joinPoint, Auditable auditable, Object result) {
        try {
            Long entityId = extractEntityId(result);
            String description = buildDescription(auditable, joinPoint);
            
            auditService.log(
                    auditable.action(),
                    auditable.entityType(),
                    entityId,
                    description
            );
        } catch (Exception e) {
            log.error("Failed to create audit log: {}", e.getMessage());
        }
    }

    /**
     * Ghi log khi method throw exception
     */
    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "ex")
    public void auditFailure(JoinPoint joinPoint, Auditable auditable, Exception ex) {
        try {
            String description = buildDescription(auditable, joinPoint);
            
            auditService.logError(
                    auditable.action(),
                    auditable.entityType(),
                    null,
                    description,
                    ex.getMessage()
            );
        } catch (Exception e) {
            log.error("Failed to create audit log: {}", e.getMessage());
        }
    }

    /**
     * Trích xuất entity ID từ result
     */
    private Long extractEntityId(Object result) {
        if (result == null) return null;
        
        try {
            // Thử lấy id từ các method phổ biến
            var idMethod = result.getClass().getMethod("getId");
            Object id = idMethod.invoke(result);
            if (id instanceof Long) return (Long) id;
            if (id instanceof Integer) return ((Integer) id).longValue();
        } catch (Exception ignored) {
            // Không có method getId
        }
        
        return null;
    }

    /**
     * Build description từ annotation và method info
     */
    private String buildDescription(Auditable auditable, JoinPoint joinPoint) {
        if (!auditable.description().isEmpty()) {
            return auditable.description();
        }
        
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        return className + "." + methodName + "()";
    }
}
