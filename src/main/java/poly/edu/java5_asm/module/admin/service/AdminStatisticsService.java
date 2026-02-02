package poly.edu.java5_asm.module.admin.service;

import poly.edu.java5_asm.module.admin.dto.response.DashboardStatsResponse;
import poly.edu.java5_asm.module.admin.dto.response.RevenueStatsResponse;
import poly.edu.java5_asm.module.admin.dto.response.TrafficStatsResponse;
import poly.edu.java5_asm.module.admin.dto.response.UserRegistrationStatsResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface cho Admin Statistics Service
 */
public interface AdminStatisticsService {

    DashboardStatsResponse getDashboardStats();

    List<UserRegistrationStatsResponse> getUserRegistrationStats(LocalDate startDate, LocalDate endDate);

    List<TrafficStatsResponse> getTrafficStats(LocalDate startDate, LocalDate endDate);

    List<RevenueStatsResponse> getRevenueStats(LocalDate startDate, LocalDate endDate);

    List<UserRegistrationStatsResponse> getLast7DaysRegistrationStats();

    List<TrafficStatsResponse> getLast7DaysTrafficStats();

    List<RevenueStatsResponse> getLast7DaysRevenueStats();

    List<UserRegistrationStatsResponse> getLast30DaysRegistrationStats();

    List<TrafficStatsResponse> getLast30DaysTrafficStats();

    List<RevenueStatsResponse> getLast30DaysRevenueStats();
}
