package poly.edu.java5_asm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.response.DashboardStatsResponse;
import poly.edu.java5_asm.dto.response.TrafficStatsResponse;
import poly.edu.java5_asm.dto.response.UserRegistrationStatsResponse;
import poly.edu.java5_asm.entity.UserActivityLog;
import poly.edu.java5_asm.repository.UserActivityLogRepository;
import poly.edu.java5_asm.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service xử lý logic thống kê cho Admin
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStatisticsService {

    private final UserRepository userRepository;
    private final UserActivityLogRepository activityLogRepository;

    /**
     * Lấy thống kê tổng quan cho Dashboard
     */
    public DashboardStatsResponse getDashboardStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfWeek = now.minusWeeks(1);
        LocalDateTime startOfMonth = now.minusMonths(1);

        return DashboardStatsResponse.builder()
                // Thống kê người dùng
                .totalUsers(userRepository.count())
                .activeUsers(userRepository.countByIsActiveTrue())
                .newUsersToday(userRepository.countByRegisteredDate(LocalDate.now()))
                .newUsersThisWeek(userRepository.countByRegisteredDateBetween(
                        startOfWeek.toLocalDate(), LocalDate.now()))
                .newUsersThisMonth(userRepository.countByRegisteredDateBetween(
                        startOfMonth.toLocalDate(), LocalDate.now()))

                // Thống kê traffic
                .totalPageViewsToday(activityLogRepository.countPageViewsBetween(startOfToday, now))
                .totalPageViewsThisWeek(activityLogRepository.countPageViewsBetween(startOfWeek, now))
                .totalPageViewsThisMonth(activityLogRepository.countPageViewsBetween(startOfMonth, now))
                .uniqueVisitorsToday(activityLogRepository.countUniqueVisitorsBetween(startOfToday, now))
                .uniqueVisitorsThisWeek(activityLogRepository.countUniqueVisitorsBetween(startOfWeek, now))
                .uniqueVisitorsThisMonth(activityLogRepository.countUniqueVisitorsBetween(startOfMonth, now))

                // Thống kê hoạt động
                .totalLogins(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.LOGIN))
                .totalProductViews(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.PRODUCT_VIEW))
                .totalSearches(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.SEARCH))
                .totalAddToCarts(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.ADD_TO_CART))
                .totalCheckouts(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.CHECKOUT))
                .build();
    }

    /**
     * Lấy thống kê đăng ký người dùng theo ngày
     */
    public List<UserRegistrationStatsResponse> getUserRegistrationStats(LocalDate startDate, LocalDate endDate) {
        List<UserRegistrationStatsResponse> stats = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            Long count = userRepository.countByRegisteredDate(currentDate);

            stats.add(UserRegistrationStatsResponse.builder()
                    .date(currentDate)
                    .registrationCount(count)
                    .build());

            currentDate = currentDate.plusDays(1);
        }

        return stats;
    }

    /**
     * Lấy thống kê traffic theo ngày
     */
    public List<TrafficStatsResponse> getTrafficStats(LocalDate startDate, LocalDate endDate) {
        List<TrafficStatsResponse> stats = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            LocalDateTime dayStart = currentDate.atStartOfDay();
            LocalDateTime dayEnd = currentDate.atTime(LocalTime.MAX);

            stats.add(TrafficStatsResponse.builder()
                    .date(currentDate)
                    .totalPageViews(activityLogRepository.countPageViewsBetween(dayStart, dayEnd))
                    .uniqueVisitors(activityLogRepository.countUniqueVisitorsBetween(dayStart, dayEnd))
                    .loginCount(activityLogRepository.countLoginsBetween(dayStart, dayEnd))
                    .productViewCount(activityLogRepository.countProductViewsBetween(dayStart, dayEnd))
                    .searchCount(activityLogRepository.countSearchesBetween(dayStart, dayEnd))
                    .addToCartCount(activityLogRepository.countAddToCartsBetween(dayStart, dayEnd))
                    .checkoutCount(activityLogRepository.countCheckoutsBetween(dayStart, dayEnd))
                    .build());

            currentDate = currentDate.plusDays(1);
        }

        return stats;
    }

    /**
     * Lấy thống kê đăng ký 7 ngày gần nhất
     */
    public List<UserRegistrationStatsResponse> getLast7DaysRegistrationStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        return getUserRegistrationStats(startDate, endDate);
    }

    /**
     * Lấy thống kê traffic 7 ngày gần nhất
     */
    public List<TrafficStatsResponse> getLast7DaysTrafficStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        return getTrafficStats(startDate, endDate);
    }

    /**
     * Lấy thống kê đăng ký 30 ngày gần nhất
     */
    public List<UserRegistrationStatsResponse> getLast30DaysRegistrationStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        return getUserRegistrationStats(startDate, endDate);
    }

    /**
     * Lấy thống kê traffic 30 ngày gần nhất
     */
    public List<TrafficStatsResponse> getLast30DaysTrafficStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        return getTrafficStats(startDate, endDate);
    }
}
