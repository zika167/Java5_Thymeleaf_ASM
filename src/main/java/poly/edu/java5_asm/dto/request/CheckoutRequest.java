package poly.edu.java5_asm.dto.request;

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
    private String shippingMethod;
    private String paymentMethod;
    private String customerNote;
}
