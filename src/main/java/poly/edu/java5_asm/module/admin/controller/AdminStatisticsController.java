package poly.edu.java5_asm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import poly.edu.java5_asm.dto.response.DashboardStatsResponse;
import poly.edu.java5_asm.dto.response.TrafficStatsResponse;
import poly.edu.java5_asm.dto.response.UserRegistrationStatsResponse;
import poly.edu.java5_asm.service.AdminStatisticsService;

import java.time.LocalDate;
import java.util.List;

/**
 * REST API Controller cho Admin Statistics
 * Chỉ ADMIN mới có quyền truy cập
 */
@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Chỉ ADMIN mới truy cập được
public class AdminStatisticsController {

    private final AdminStatisticsService statisticsService;

    /**
     * API: Lấy thống kê tổng quan Dashboard
     * GET /api/admin/statistics/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        DashboardStatsResponse stats = statisticsService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * API: Lấy thống kê đăng ký người dùng theo khoảng thời gian
     * GET /api/admin/statistics/registrations?startDate=2024-01-01&endDate=2024-01-31
     */
    @GetMapping("/registrations")
    public ResponseEntity<List<UserRegistrationStatsResponse>> getRegistrationStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<UserRegistrationStatsResponse> stats = statisticsService.getUserRegistrationStats(startDate, endDate);
        return ResponseEntity.ok(stats);
    }

    /**
     * API: Lấy thống kê traffic theo khoảng thời gian
     * GET /api/admin/statistics/traffic?startDate=2024-01-01&endDate=2024-01-31
     */
    @GetMapping("/traffic")
    public ResponseEntity<List<TrafficStatsResponse>> getTrafficStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<TrafficStatsResponse> stats = statisticsService.getTrafficStats(startDate, endDate);
        return ResponseEntity.ok(stats);
    }

    /**
     * API: Lấy thống kê đăng ký 7 ngày gần nhất
     * GET /api/admin/statistics/registrations/last-7-days
     */
    @GetMapping("/registrations/last-7-days")
    public ResponseEntity<List<UserRegistrationStatsResponse>> getLast7DaysRegistrations() {
        List<UserRegistrationStatsResponse> stats = statisticsService.getLast7DaysRegistrationStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * API: Lấy thống kê traffic 7 ngày gần nhất
     * GET /api/admin/statistics/traffic/last-7-days
     */
    @GetMapping("/traffic/last-7-days")
    public ResponseEntity<List<TrafficStatsResponse>> getLast7DaysTraffic() {
        List<TrafficStatsResponse> stats = statisticsService.getLast7DaysTrafficStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * API: Lấy thống kê đăng ký 30 ngày gần nhất
     * GET /api/admin/statistics/registrations/last-30-days
     */
    @GetMapping("/registrations/last-30-days")
    public ResponseEntity<List<UserRegistrationStatsResponse>> getLast30DaysRegistrations() {
        List<UserRegistrationStatsResponse> stats = statisticsService.getLast30DaysRegistrationStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * API: Lấy thống kê traffic 30 ngày gần nhất
     * GET /api/admin/statistics/traffic/last-30-days
     */
    @GetMapping("/traffic/last-30-days")
    public ResponseEntity<List<TrafficStatsResponse>> getLast30DaysTraffic() {
        List<TrafficStatsResponse> stats = statisticsService.getLast30DaysTrafficStats();
        return ResponseEntity.ok(stats);
    }
}
