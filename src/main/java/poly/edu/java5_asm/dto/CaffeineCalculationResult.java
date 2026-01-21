package poly.edu.java5_asm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaffeineCalculationResult {
    private Integer totalCaffeine; // mg
    private Integer safeLimit; // mg
    private Double percentage; // %
    private String status; // SAFE, WARNING, DANGER
    private String statusColor; // green, yellow, red
    private String message;
    private String recommendation;
}
