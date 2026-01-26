package poly.edu.java5_asm.dto.request;

import lombok.*;

import java.math.BigDecimal;

/**
 * DTO Request cho tìm kiếm và lọc sản phẩm
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchRequest {

    private String keyword;         // Từ khóa tìm kiếm
    private Long categoryId;        // Lọc theo category
    private Long brandId;           // Lọc theo brand
    private BigDecimal minPrice;    // Giá tối thiểu
    private BigDecimal maxPrice;    // Giá tối đa

    // Phân trang
    @Builder.Default
    private Integer page = 0;       // Trang hiện tại (mặc định 0)

    @Builder.Default
    private Integer size = 12;      // Số sản phẩm mỗi trang (mặc định 12)

    // Sắp xếp
    @Builder.Default
    private String sortBy = "createdAt";  // Sắp xếp theo (mặc định: ngày tạo)

    @Builder.Default
    private String sortDirection = "DESC"; // Hướng sắp xếp (ASC/DESC)
}
