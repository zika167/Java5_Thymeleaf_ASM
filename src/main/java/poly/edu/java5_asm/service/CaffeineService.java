package poly.edu.java5_asm.service;

import java.util.Map;

import poly.edu.java5_asm.dto.request.CaffeineCalculationRequest;
import poly.edu.java5_asm.dto.result.CaffeineCalculationResult;

public interface CaffeineService {
    /**
     * Calculate caffeine intake and compare with safe limits
     */
    CaffeineCalculationResult calculateCaffeine(CaffeineCalculationRequest request);
    
    /**
     * Get available drink types with caffeine content
     */
    Map<String, Integer> getDrinkTypes();
    
    /**
     * Get safe limit based on age, gender, and pregnancy status
     */
    Integer getSafeLimit(Integer age, String gender, Boolean isPregnant);
}
