package poly.edu.java5_asm.dto.response;

import lombok.*;

/**
 * DTO Response cho Dashboard Admin - Tổng quan thống kê
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {

    // Thống kê người dùng
    private Long totalUsers;              // Tổng số người dùng
    private Long activeUsers;             // Số người dùng đang hoạt động
    private Long newUsersToday;           // Số người đăng ký hôm nay
    private Long newUsersThisWeek;        // Số người đăng ký tuần này
    private Long newUsersThisMonth;       // Số người đăng ký tháng này

    // Thống kê traffic
    private Long totalPageViewsToday;     // Tổng lượt xem hôm nay
    private Long totalPageViewsThisWeek;  // Tổng lượt xem tuần này
    private Long totalPageViewsThisMonth; // Tổng lượt xem tháng này
    private Long uniqueVisitorsToday;     // Số người truy cập duy nhất hôm nay
    private Long uniqueVisitorsThisWeek;  // Số người truy cập duy nhất tuần này
    private Long uniqueVisitorsThisMonth; // Số người truy cập duy nhất tháng này

    // Thống kê hoạt động
    private Long totalLogins;             // Tổng số lần đăng nhập
    private Long totalProductViews;       // Tổng lượt xem sản phẩm
    private Long totalSearches;           // Tổng số lần tìm kiếm
    private Long totalAddToCarts;         // Tổng số lần thêm vào giỏ
    private Long totalCheckouts;          // Tổng số lần thanh toán
}
