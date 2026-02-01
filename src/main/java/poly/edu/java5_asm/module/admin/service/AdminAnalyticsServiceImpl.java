package poly.edu.java5_asm.module.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import poly.edu.java5_asm.module.admin.dto.response.AnalyticsDataResponse;
import poly.edu.java5_asm.module.user.repository.UserActivityLogRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation của AdminAnalyticsService
 */
@Service
@RequiredArgsConstructor
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final UserActivityLogRepository activityLogRepository;

    @Override
    public AnalyticsDataResponse getDailyAnalytics() {
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        
        // Lấy dữ liệu 7 ngày gần nhất
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
            
            labels.add(date.format(formatter));
            Long count = activityLogRepository.countUniqueVisitorsBetween(startOfDay, endOfDay);
            data.add(count != null ? count : 0L);
        }
        
        return AnalyticsDataResponse.builder()
                .labels(labels)
                .data(data)
                .period("daily")
                .build();
    }

    @Override
    public AnalyticsDataResponse getMonthlyAnalytics() {
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        
        // Lấy dữ liệu 12 tháng gần nhất
        for (int i = 11; i >= 0; i--) {
            LocalDate month = today.minusMonths(i);
            LocalDateTime startOfMonth = month.withDayOfMonth(1).atStartOfDay();
            LocalDateTime endOfMonth = month.withDayOfMonth(month.lengthOfMonth())
                    .plusDays(1).atStartOfDay();
            
            labels.add(month.format(formatter));
            Long count = activityLogRepository.countUniqueVisitorsBetween(startOfMonth, endOfMonth);
            data.add(count != null ? count : 0L);
        }
        
        return AnalyticsDataResponse.builder()
                .labels(labels)
                .data(data)
                .period("monthly")
                .build();
    }

    @Override
    public AnalyticsDataResponse getQuarterlyAnalytics() {
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentQuarter = today.get(IsoFields.QUARTER_OF_YEAR);
        
        // Lấy dữ liệu 4 quý gần nhất
        for (int i = 3; i >= 0; i--) {
            int quarter = currentQuarter - i;
            int year = currentYear;
            
            // Điều chỉnh năm nếu quý âm
            while (quarter <= 0) {
                quarter += 4;
                year--;
            }
            
            LocalDateTime startOfQuarter = getStartOfQuarter(year, quarter);
            LocalDateTime endOfQuarter = getEndOfQuarter(year, quarter);
            
            labels.add("Q" + quarter + " " + year);
            Long count = activityLogRepository.countUniqueVisitorsBetween(startOfQuarter, endOfQuarter);
            data.add(count != null ? count : 0L);
        }
        
        return AnalyticsDataResponse.builder()
                .labels(labels)
                .data(data)
                .period("quarterly")
                .build();
    }

    @Override
    public AnalyticsDataResponse getYearlyAnalytics() {
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        int currentYear = LocalDate.now().getYear();
        
        // Lấy dữ liệu 3 năm gần nhất
        for (int i = 2; i >= 0; i--) {
            int year = currentYear - i;
            LocalDateTime startOfYear = LocalDate.of(year, 1, 1).atStartOfDay();
            LocalDateTime endOfYear = LocalDate.of(year, 12, 31).plusDays(1).atStartOfDay();
            
            labels.add(String.valueOf(year));
            Long count = activityLogRepository.countUniqueVisitorsBetween(startOfYear, endOfYear);
            data.add(count != null ? count : 0L);
        }
        
        return AnalyticsDataResponse.builder()
                .labels(labels)
                .data(data)
                .period("yearly")
                .build();
    }

    @Override
    public Long getOnlineUsersCount() {
        // Đếm số session duy nhất có hoạt động trong 5 phút gần nhất
        // KHÔNG bao gồm admin (role = ADMIN)
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        LocalDateTime now = LocalDateTime.now();
        
        Long count = activityLogRepository.countOnlineUsersExcludingAdmin(fiveMinutesAgo, now);
        return count != null ? count : 0L;
    }

    // Helper methods
    private LocalDateTime getStartOfQuarter(int year, int quarter) {
        int month = (quarter - 1) * 3 + 1;
        return LocalDate.of(year, month, 1).atStartOfDay();
    }

    private LocalDateTime getEndOfQuarter(int year, int quarter) {
        int month = quarter * 3;
        LocalDate endDate = LocalDate.of(year, month, 1)
                .withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth());
        return endDate.plusDays(1).atStartOfDay();
    }
}
