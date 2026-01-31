package poly.edu.java5_asm.common.audit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * API Controller cho Audit Logs (Admin only)
 */
@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Audit Logs", description = "API quản lý Audit Logs (Admin)")
public class AuditLogController {

    private final AuditService auditService;

    @GetMapping
    @Operation(summary = "Tìm kiếm audit logs", description = "Tìm kiếm với nhiều điều kiện")
    public ResponseEntity<Page<AuditLog>> searchLogs(
            @Parameter(description = "User ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "Loại action") @RequestParam(required = false) AuditAction action,
            @Parameter(description = "Loại entity") @RequestParam(required = false) String entityType,
            @Parameter(description = "Từ ngày") @RequestParam(required = false) 
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Đến ngày") @RequestParam(required = false) 
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Số trang") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số items/trang") @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> logs = auditService.searchLogs(userId, action, entityType, startDate, endDate, pageable);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lấy audit logs theo user")
    public ResponseEntity<Page<AuditLog>> getLogsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(auditService.getLogsByUser(userId, pageable));
    }

    @GetMapping("/action/{action}")
    @Operation(summary = "Lấy audit logs theo action")
    public ResponseEntity<Page<AuditLog>> getLogsByAction(
            @PathVariable AuditAction action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(auditService.getLogsByAction(action, pageable));
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "Lấy audit logs theo entity")
    public ResponseEntity<List<AuditLog>> getLogsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId
    ) {
        return ResponseEntity.ok(auditService.getLogsByEntity(entityType, entityId));
    }

    @GetMapping("/actions")
    @Operation(summary = "Lấy danh sách các loại action")
    public ResponseEntity<AuditAction[]> getActions() {
        return ResponseEntity.ok(AuditAction.values());
    }
}
