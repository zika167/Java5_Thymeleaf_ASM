package poly.edu.java5_asm.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poly.edu.java5_asm.dto.request.CaffeineCalculationRequest;
import poly.edu.java5_asm.dto.result.CaffeineCalculationResult;
import poly.edu.java5_asm.service.CaffeineService;

@Service
@Slf4j
public class CaffeineServiceImpl implements CaffeineService {
    
    // Safe caffeine limits (mg/day)
    private static final int ADULT_LIMIT = 400;
    private static final int PREGNANT_LIMIT = 200;
    private static final int TEEN_LIMIT = 100;
    private static final int CHILD_LIMIT = 0;
    
    // Drink types with caffeine content (mg per serving)
    private static final Map<String, Integer> DRINK_TYPES = new LinkedHashMap<>();
    
    static {
        // Coffee
        DRINK_TYPES.put("Espresso (1 shot, 30ml)", 63);
        DRINK_TYPES.put("Cà phê phin (1 ly, 150ml)", 100);
        DRINK_TYPES.put("Cà phê sữa đá (1 ly, 200ml)", 80);
        DRINK_TYPES.put("Americano (1 ly, 240ml)", 95);
        DRINK_TYPES.put("Cappuccino (1 ly, 240ml)", 75);
        DRINK_TYPES.put("Latte (1 ly, 240ml)", 75);
        DRINK_TYPES.put("Cà phê đen (1 ly, 240ml)", 95);
        DRINK_TYPES.put("Cà phê pha máy (1 ly, 240ml)", 120);
        
        // Tea
        DRINK_TYPES.put("Trà đen (1 ly, 240ml)", 47);
        DRINK_TYPES.put("Trà xanh (1 ly, 240ml)", 28);
        DRINK_TYPES.put("Trà ô long (1 ly, 240ml)", 38);
        DRINK_TYPES.put("Trà sữa (1 ly, 240ml)", 30);
        
        // Energy Drinks
        DRINK_TYPES.put("Red Bull (1 lon, 250ml)", 80);
        DRINK_TYPES.put("Monster Energy (1 lon, 500ml)", 160);
        DRINK_TYPES.put("Sting (1 lon, 330ml)", 100);
        DRINK_TYPES.put("Number 1 (1 chai, 330ml)", 50);
        
        // Soft Drinks
        DRINK_TYPES.put("Coca Cola (1 lon, 330ml)", 34);
        DRINK_TYPES.put("Pepsi (1 lon, 330ml)", 38);
        
        // Others
        DRINK_TYPES.put("Sô-cô-la nóng (1 ly, 240ml)", 25);
        DRINK_TYPES.put("Sô-cô-la đen (1 thanh, 40g)", 20);
    }
    
    @Override
    public CaffeineCalculationResult calculateCaffeine(CaffeineCalculationRequest request) {
        log.info("Calculating caffeine for request: {}", request);
        
        // Validate input
        if (request.getAge() == null || request.getAge() < 0) {
            throw new IllegalArgumentException("Tuổi không hợp lệ");
        }
        
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            request.setQuantity(1);
        }
        
        // Get caffeine content per drink
        Integer caffeinePerDrink = DRINK_TYPES.get(request.getDrinkType());
        if (caffeinePerDrink == null) {
            throw new IllegalArgumentException("Loại đồ uống không hợp lệ");
        }
        
        // Calculate total caffeine
        int totalCaffeine = caffeinePerDrink * request.getQuantity();
        
        // Get safe limit
        int safeLimit = getSafeLimit(request.getAge(), request.getGender(), request.getIsPregnant());
        
        // Calculate percentage
        double percentage = safeLimit > 0 ? (totalCaffeine * 100.0 / safeLimit) : 0;
        
        // Determine status
        String status;
        String statusColor;
        String message;
        String recommendation;
        
        if (safeLimit == 0) {
            status = "DANGER";
            statusColor = "red";
            message = "Trẻ em dưới 12 tuổi không nên sử dụng caffeine!";
            recommendation = "Hãy chọn đồ uống không chứa caffeine như nước ép trái cây, sữa hoặc nước lọc.";
        } else if (percentage < 70) {
            status = "SAFE";
            statusColor = "green";
            message = String.format("Bạn đã nạp %dmg/%dmg caffeine - Mức an toàn!", totalCaffeine, safeLimit);
            recommendation = "Lượng caffeine của bạn nằm trong mức an toàn. Hãy tiếp tục duy trì!";
        } else if (percentage <= 100) {
            status = "WARNING";
            statusColor = "yellow";
            message = String.format("Bạn đã nạp %dmg/%dmg caffeine - Cảnh báo!", totalCaffeine, safeLimit);
            recommendation = "Bạn đang tiếp cận giới hạn an toàn. Hãy cân nhắc giảm lượng caffeine trong ngày.";
        } else {
            status = "DANGER";
            statusColor = "red";
            message = String.format("Bạn đã nạp %dmg/%dmg caffeine - Nguy hiểm!", totalCaffeine, safeLimit);
            recommendation = "Bạn đã vượt quá giới hạn an toàn! Hãy ngừng sử dụng caffeine và uống nhiều nước.";
        }
        
        return CaffeineCalculationResult.builder()
                .totalCaffeine(totalCaffeine)
                .safeLimit(safeLimit)
                .percentage(percentage)
                .status(status)
                .statusColor(statusColor)
                .message(message)
                .recommendation(recommendation)
                .build();
    }
    
    @Override
    public Map<String, Integer> getDrinkTypes() {
        return DRINK_TYPES;
    }
    
    @Override
    public Integer getSafeLimit(Integer age, String gender, Boolean isPregnant) {
        if (age == null || age < 0) {
            return 0;
        }
        
        // Children under 12
        if (age < 12) {
            return CHILD_LIMIT;
        }
        
        // Pregnant women
        if (Boolean.TRUE.equals(isPregnant)) {
            return PREGNANT_LIMIT;
        }
        
        // Teenagers 12-18
        if (age >= 12 && age < 18) {
            return TEEN_LIMIT;
        }
        
        // Adults 18+
        return ADULT_LIMIT;
    }
}
