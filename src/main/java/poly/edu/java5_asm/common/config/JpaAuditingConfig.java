package poly.edu.java5_asm.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing Configuration
 * 
 * Kích hoạt tính năng tự động cập nhật @CreatedDate, @LastModifiedDate
 * trong BaseEntity
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
