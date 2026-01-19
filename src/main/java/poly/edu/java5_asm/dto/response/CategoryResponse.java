package poly.edu.java5_asm.dto.response;

import lombok.*;

/**
 * DTO Response cho Category
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private String slug;
    private String iconUrl;
    private Long productCount; // Số lượng sản phẩm trong category
}
