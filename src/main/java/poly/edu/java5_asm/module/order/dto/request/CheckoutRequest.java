package poly.edu.java5_asm.module.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequest {

    private Long shippingAddressId;

    @NotBlank(message = "Phương thức giao hàng không được để trống")
    private String shippingMethod;

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    private String paymentMethod;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private String customerNote;
}
