package poly.edu.java5_asm.common.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity lưu trữ Audit Log
 * Ghi lại các thao tác quan trọng trong hệ thống
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_user_id", columnList = "user_id"),
        @Index(name = "idx_audit_action", columnList = "action"),
        @Index(name = "idx_audit_entity_type", columnList = "entity_type"),
        @Index(name = "idx_audit_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "action", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "entity_type", length = 50)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "request_url", length = 500)
    private String requestUrl;

    @Column(name = "request_method", length = 10)
    private String requestMethod;

    @Column(name = "status", length = 20)
    private String status = "SUCCESS";

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Static builder method
    public static AuditLogBuilder builder() {
        return new AuditLogBuilder();
    }

    // Inner Builder class
    public static class AuditLogBuilder {
        private Long userId;
        private String username;
        private AuditAction action;
        private String entityType;
        private Long entityId;
        private String description;
        private String oldValue;
        private String newValue;
        private String ipAddress;
        private String userAgent;
        private String requestUrl;
        private String requestMethod;
        private String status = "SUCCESS";
        private String errorMessage;

        public AuditLogBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AuditLogBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuditLogBuilder action(AuditAction action) {
            this.action = action;
            return this;
        }

        public AuditLogBuilder entityType(String entityType) {
            this.entityType = entityType;
            return this;
        }

        public AuditLogBuilder entityId(Long entityId) {
            this.entityId = entityId;
            return this;
        }

        public AuditLogBuilder description(String description) {
            this.description = description;
            return this;
        }

        public AuditLogBuilder oldValue(String oldValue) {
            this.oldValue = oldValue;
            return this;
        }

        public AuditLogBuilder newValue(String newValue) {
            this.newValue = newValue;
            return this;
        }

        public AuditLogBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public AuditLogBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public AuditLogBuilder requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public AuditLogBuilder requestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public AuditLogBuilder status(String status) {
            this.status = status;
            return this;
        }

        public AuditLogBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public AuditLog build() {
            AuditLog log = new AuditLog();
            log.userId = this.userId;
            log.username = this.username;
            log.action = this.action;
            log.entityType = this.entityType;
            log.entityId = this.entityId;
            log.description = this.description;
            log.oldValue = this.oldValue;
            log.newValue = this.newValue;
            log.ipAddress = this.ipAddress;
            log.userAgent = this.userAgent;
            log.requestUrl = this.requestUrl;
            log.requestMethod = this.requestMethod;
            log.status = this.status;
            log.errorMessage = this.errorMessage;
            return log;
        }
    }
}
