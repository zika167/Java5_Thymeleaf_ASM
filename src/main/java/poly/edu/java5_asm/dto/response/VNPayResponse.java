package poly.edu.java5_asm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response từ VNPay callback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VNPayResponse {

    private boolean success;
    private String message;
    private String orderNumber;
    private String transactionId;
    private String bankCode;
    private BigDecimal amount;
    private String paymentDate;
    private String responseCode;
    private String responseMessage;
}
