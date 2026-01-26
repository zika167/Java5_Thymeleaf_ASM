package poly.edu.java5_asm.dto.response;

import lombok.*;

/**
 * DTO Response cho Brand
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponse {

    private Long id;
    private String name;
    private String slug;
    private String logoUrl;
    private Long productCount; // Số lượng sản phẩm của brand
}
