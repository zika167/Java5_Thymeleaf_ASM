package poly.edu.java5_asm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response từ Momo callback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MomoResponse {

    private boolean success;
    private String message;
    private String orderNumber;
    private String transactionId;
    private String requestId;
    private BigDecimal amount;
    private String responseTime;
    private int resultCode;
    private String resultMessage;
}
