package poly.edu.java5_asm.dto.response;

import lombok.*;

import java.time.LocalDate;

/**
 * DTO Response cho thống kê traffic (lưu lượng truy cập)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrafficStatsResponse {

    private LocalDate date;              // Ngày
    private Long totalPageViews;         // Tổng số lượt xem trang
    private Long uniqueVisitors;         // Số người truy cập duy nhất
    private Long loginCount;             // Số lần đăng nhập
    private Long productViewCount;       // Số lượt xem sản phẩm
    private Long searchCount;            // Số lần tìm kiếm
    private Long addToCartCount;         // Số lần thêm vào giỏ
    private Long checkoutCount;          // Số lần thanh toán
}
