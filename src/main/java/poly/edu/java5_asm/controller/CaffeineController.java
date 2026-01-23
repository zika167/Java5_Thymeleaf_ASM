package poly.edu.java5_asm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poly.edu.java5_asm.dto.CaffeineCalculationRequest;
import poly.edu.java5_asm.dto.CaffeineCalculationResult;
import poly.edu.java5_asm.service.CaffeineService;

@Controller
@RequestMapping("/cc-doctor")
@RequiredArgsConstructor
@Slf4j
public class CaffeineController {
    
    private final CaffeineService caffeineService;
    
    @GetMapping
    public String showCalculator(Model model) {
        log.info("Showing CC-Doctor calculator page");
        
        // Add empty request object for form binding
        model.addAttribute("request", new CaffeineCalculationRequest());
        
        // Add drink types for dropdown
        model.addAttribute("drinkTypes", caffeineService.getDrinkTypes());
        
        return "cc-doctor";
    }
    
    @PostMapping("/calculate")
    public String calculate(@ModelAttribute("request") CaffeineCalculationRequest request, Model model) {
        log.info("Calculating caffeine for: {}", request);
        
        try {
            // Calculate caffeine
            CaffeineCalculationResult result = caffeineService.calculateCaffeine(request);
            
            // Add result to model
            model.addAttribute("result", result);
            model.addAttribute("request", request);
            
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("request", request);
        } catch (Exception e) {
            log.error("Error calculating caffeine", e);
            model.addAttribute("error", "Đã xảy ra lỗi khi tính toán. Vui lòng thử lại!");
            model.addAttribute("request", request);
        }
        
        // Add drink types for dropdown
        model.addAttribute("drinkTypes", caffeineService.getDrinkTypes());
        
        return "cc-doctor";
    }
}
