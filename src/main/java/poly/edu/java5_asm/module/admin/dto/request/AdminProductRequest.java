package poly.edu.java5_asm.module.admin.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO cho Admin Product Management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductRequest {
    
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được quá 255 ký tự")
    private String name;
    
    private String description;
    
    @Size(max = 500, message = "Mô tả ngắn không được quá 500 ký tự")
    private String shortDescription;
    
    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0", message = "Giá sản phẩm phải >= 0")
    private BigDecimal price;
    
    @DecimalMin(value = "0", message = "Giá khuyến mãi phải >= 0")
    private BigDecimal discountPrice;
    
    private Long categoryId;
    
    private Long brandId;
    
    @Min(value = 0, message = "Số lượng tồn kho phải >= 0")
    private Integer stockQuantity;
    
    private String imageUrl;
    
    @Size(max = 50, message = "SKU không được quá 50 ký tự")
    private String sku;
    
    private String weight;
    
    private Boolean isFeatured;
    
    private Boolean isActive;
}
