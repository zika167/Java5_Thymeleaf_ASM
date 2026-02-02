package poly.edu.java5_asm.module.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.module.admin.dto.response.DashboardStatsResponse;
import poly.edu.java5_asm.module.admin.dto.response.RevenueStatsResponse;
import poly.edu.java5_asm.module.admin.dto.response.TrafficStatsResponse;
import poly.edu.java5_asm.module.admin.dto.response.UserRegistrationStatsResponse;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.user.entity.UserActivityLog;
import poly.edu.java5_asm.module.order.repository.OrderRepository;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.user.repository.UserActivityLogRepository;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation của AdminStatisticsService
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStatisticsServiceImpl implements AdminStatisticsService {

    private final UserRepository userRepository;
    private final UserActivityLogRepository activityLogRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public DashboardStatsResponse getDashboardStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfWeek = now.minusWeeks(1);
        LocalDateTime startOfMonth = now.minusMonths(1);

        List<Order> allOrders = orderRepository.findAll();
        Long totalOrders = (long) allOrders.size();
        BigDecimal totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardStatsResponse.builder()
                .totalUsers(userRepository.count())
                .activeUsers(userRepository.countByIsActiveTrue())
                .newUsersToday(userRepository.countByRegisteredDate(LocalDate.now()))
                .newUsersThisWeek(userRepository.countByRegisteredDateBetween(
                        startOfWeek.toLocalDate(), LocalDate.now()))
                .newUsersThisMonth(userRepository.countByRegisteredDateBetween(
                        startOfMonth.toLocalDate(), LocalDate.now()))
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .totalProducts(productRepository.count())
                .totalPageViewsToday(activityLogRepository.countPageViewsBetween(startOfToday, now))
                .totalPageViewsThisWeek(activityLogRepository.countPageViewsBetween(startOfWeek, now))
                .totalPageViewsThisMonth(activityLogRepository.countPageViewsBetween(startOfMonth, now))
                .uniqueVisitorsToday(activityLogRepository.countUniqueVisitorsBetween(startOfToday, now))
                .uniqueVisitorsThisWeek(activityLogRepository.countUniqueVisitorsBetween(startOfWeek, now))
                .uniqueVisitorsThisMonth(activityLogRepository.countUniqueVisitorsBetween(startOfMonth, now))
                .totalLogins(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.LOGIN))
                .totalProductViews(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.PRODUCT_VIEW))
                .totalSearches(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.SEARCH))
                .totalAddToCarts(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.ADD_TO_CART))
                .totalCheckouts(activityLogRepository.countByActivityType(UserActivityLog.ActivityType.CHECKOUT))
                .build();
    }

    @Override
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

    @Override
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

    @Override
    public List<UserRegistrationStatsResponse> getLast7DaysRegistrationStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        return getUserRegistrationStats(startDate, endDate);
    }

    @Override
    public List<TrafficStatsResponse> getLast7DaysTrafficStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        return getTrafficStats(startDate, endDate);
    }

    @Override
    public List<UserRegistrationStatsResponse> getLast30DaysRegistrationStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        return getUserRegistrationStats(startDate, endDate);
    }

    @Override
    public List<TrafficStatsResponse> getLast30DaysTrafficStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        return getTrafficStats(startDate, endDate);
    }

    @Override
    public List<RevenueStatsResponse> getRevenueStats(LocalDate startDate, LocalDate endDate) {
        List<RevenueStatsResponse> stats = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            LocalDateTime dayStart = currentDate.atStartOfDay();
            LocalDateTime dayEnd = currentDate.atTime(LocalTime.MAX);

            List<Order> ordersInDay = orderRepository.findByOrderedAtBetween(dayStart, dayEnd);
            
            // Chỉ tính doanh thu từ đơn hàng đã giao (DELIVERED)
            BigDecimal dailyRevenue = ordersInDay.stream()
                    .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            long orderCount = ordersInDay.stream()
                    .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                    .count();

            stats.add(RevenueStatsResponse.builder()
                    .date(currentDate)
                    .revenue(dailyRevenue)
                    .orderCount(orderCount)
                    .build());

            currentDate = currentDate.plusDays(1);
        }

        return stats;
    }

    @Override
    public List<RevenueStatsResponse> getLast7DaysRevenueStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        return getRevenueStats(startDate, endDate);
    }

    @Override
    public List<RevenueStatsResponse> getLast30DaysRevenueStats() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        return getRevenueStats(startDate, endDate);
    }
}
