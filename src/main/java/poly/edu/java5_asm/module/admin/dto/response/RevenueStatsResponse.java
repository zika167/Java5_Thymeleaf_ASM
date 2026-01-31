package poly.edu.java5_asm.module.admin.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO Response cho thống kê doanh thu theo ngày
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueStatsResponse {

    private LocalDate date;           // Ngày
    private BigDecimal revenue;       // Doanh thu trong ngày
    private Long orderCount;          // Số đơn hàng trong ngày
}
