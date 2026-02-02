package poly.edu.java5_asm.module.admin.service;

import poly.edu.java5_asm.module.admin.dto.response.AnalyticsDataResponse;

/**
 * Service interface cho Analytics
 */
public interface AdminAnalyticsService {
    
    /**
     * Lấy dữ liệu analytics theo ngày (7 ngày gần nhất)
     */
    AnalyticsDataResponse getDailyAnalytics();
    
    /**
     * Lấy dữ liệu analytics theo tháng (12 tháng gần nhất)
     */
    AnalyticsDataResponse getMonthlyAnalytics();
    
    /**
     * Lấy dữ liệu analytics theo quý (4 quý gần nhất)
     */
    AnalyticsDataResponse getQuarterlyAnalytics();
    
    /**
     * Lấy dữ liệu analytics theo năm (3 năm gần nhất)
     */
    AnalyticsDataResponse getYearlyAnalytics();
    
    /**
     * Đếm số người dùng trực tuyến (active trong 5 phút gần nhất)
     */
    Long getOnlineUsersCount();
}
