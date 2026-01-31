package poly.edu.java5_asm.common.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation đánh dấu method cần audit logging
 * Sử dụng: @Auditable(action = AuditAction.ORDER_CREATE, entityType = "Order")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    
    /**
     * Loại action cần audit
     */
    AuditAction action();
    
    /**
     * Loại entity (Order, Product, User, etc.)
     */
    String entityType() default "";
    
    /**
     * Mô tả thêm (có thể dùng SpEL)
     */
    String description() default "";
}
