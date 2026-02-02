package poly.edu.java5_asm.module.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller cho các trang Product (MVC)
 */
@Controller
public class ProductController {

    /**
     * Trang danh sách sản phẩm với filter
     */
    @GetMapping("/products")
    public String productsPage() {
        return "module/product/products";
    }
}
