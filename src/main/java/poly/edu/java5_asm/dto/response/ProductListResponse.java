package poly.edu.java5_asm.dto.response;

import lombok.*;

import java.util.List;

/**
 * DTO Response cho danh sách sản phẩm - Có phân trang
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponse {

    private List<ProductResponse> products; // Danh sách sản phẩm
    private int currentPage;                // Trang hiện tại (bắt đầu từ 0)
    private int totalPages;                 // Tổng số trang
    private long totalItems;                // Tổng số sản phẩm
    private int pageSize;                   // Số sản phẩm mỗi trang
}
