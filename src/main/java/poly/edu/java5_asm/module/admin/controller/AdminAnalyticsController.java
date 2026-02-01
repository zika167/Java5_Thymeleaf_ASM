package poly.edu.java5_asm.module.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.module.admin.dto.response.AnalyticsDataResponse;
import poly.edu.java5_asm.module.admin.service.AdminAnalyticsService;

/**
 * REST API Controller cho Analytics
 */
@RestController
@RequestMapping("/api/admin/analytics")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final AdminAnalyticsService analyticsService;

    /**
     * Lấy dữ liệu analytics theo ngày
     */
    @GetMapping("/daily")
    public ResponseEntity<AnalyticsDataResponse> getDailyAnalytics() {
        return ResponseEntity.ok(analyticsService.getDailyAnalytics());
    }

    /**
     * Lấy dữ liệu analytics theo tháng
     */
    @GetMapping("/monthly")
    public ResponseEntity<AnalyticsDataResponse> getMonthlyAnalytics() {
        return ResponseEntity.ok(analyticsService.getMonthlyAnalytics());
    }

    /**
     * Lấy dữ liệu analytics theo quý
     */
    @GetMapping("/quarterly")
    public ResponseEntity<AnalyticsDataResponse> getQuarterlyAnalytics() {
        return ResponseEntity.ok(analyticsService.getQuarterlyAnalytics());
    }

    /**
     * Lấy dữ liệu analytics theo năm
     */
    @GetMapping("/yearly")
    public ResponseEntity<AnalyticsDataResponse> getYearlyAnalytics() {
        return ResponseEntity.ok(analyticsService.getYearlyAnalytics());
    }

    /**
     * Lấy số người dùng trực tuyến (active trong 5 phút gần nhất)
     */
    @GetMapping("/online-users")
    public ResponseEntity<Long> getOnlineUsers() {
        return ResponseEntity.ok(analyticsService.getOnlineUsersCount());
    }
}
