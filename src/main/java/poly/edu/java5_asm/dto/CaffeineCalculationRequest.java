package poly.edu.java5_asm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaffeineCalculationRequest {
    private Integer age;
    private String gender; // MALE, FEMALE
    private Boolean isPregnant = false;
    private String drinkType;
    private Integer quantity = 1;
}
