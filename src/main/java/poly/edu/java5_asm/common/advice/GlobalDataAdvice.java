package poly.edu.java5_asm.common.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import poly.edu.java5_asm.module.category.entity.Category;
import poly.edu.java5_asm.module.category.repository.CategoryRepository;

import java.util.List;

/**
 * Global Controller Advice để inject data vào tất cả views
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalDataAdvice {

    private final CategoryRepository categoryRepository;

    /**
     * Inject danh sách categories vào tất cả views
     * Sử dụng trong header để hiển thị menu động
     */
    @ModelAttribute("navCategories")
    public List<Category> getNavCategories() {
        return categoryRepository.findByParentIsNullAndIsActiveTrueOrderByDisplayOrderAsc();
    }
}
