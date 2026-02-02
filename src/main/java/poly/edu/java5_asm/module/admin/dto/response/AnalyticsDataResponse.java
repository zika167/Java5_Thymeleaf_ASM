package poly.edu.java5_asm.module.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO cho dữ liệu Analytics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDataResponse {
    private List<String> labels;      // Nhãn trục X (ngày, tháng, quý, năm)
    private List<Long> data;          // Dữ liệu số lượng truy cập
    private String period;            // Loại thời gian: daily, monthly, quarterly, yearly
}
