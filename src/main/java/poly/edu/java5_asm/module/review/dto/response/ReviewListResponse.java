package poly.edu.java5_asm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO Response cho danh sách Reviews với phân trang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewListResponse {

    private List<ReviewResponse> reviews;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
