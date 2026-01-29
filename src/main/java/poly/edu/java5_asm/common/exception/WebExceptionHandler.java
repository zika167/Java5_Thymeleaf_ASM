package poly.edu.java5_asm.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Exception Handler cho Web Pages (non-REST)
 * Xử lý các exception và redirect về trang lỗi thân thiện
 */
@Slf4j
@ControllerAdvice
public class WebExceptionHandler {

    /**
     * Handle MethodArgumentTypeMismatchException
     * Xảy ra khi URL path variable không thể convert sang kiểu dữ liệu mong muốn
     * Ví dụ: /product/abc khi expect Long
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch: {} - {}", ex.getName(), ex.getValue());
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("product", null);
        mav.setViewName("module/product/product-detail");
        return mav;
    }
}
