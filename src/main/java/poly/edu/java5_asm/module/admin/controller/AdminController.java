package poly.edu.java5_asm.module.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller xử lý các trang admin UI
 * <p>
 * Tất cả endpoints đều yêu cầu role ADMIN
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    /**
     * Trang dashboard - tổng quan thống kê
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "shared/admin/dashboard";
    }

    /**
     * Trang quản lý users
     */
    @GetMapping("/users")
    public String users(Model model) {
        return "shared/admin/users";
    }

    /**
     * Trang quản lý orders
     */
    @GetMapping("/orders")
    public String orders(Model model) {
        return "shared/admin/orders";
    }

    /**
     * Trang quản lý products
     */
    @GetMapping("/products")
    public String products(Model model) {
        return "shared/admin/products";
    }

    /**
     * Trang quản lý brands (thương hiệu)
     */
    @GetMapping("/brands")
    public String brands(Model model) {
        return "shared/admin/brands";
    }

    /**
     * Trang quản lý categories (danh mục)
     */
    @GetMapping("/categories")
    public String categories(Model model) {
        return "shared/admin/categories";
    }

    /**
     * Trang quản lý truy cập (Analytics)
     */
    @GetMapping("/analytics")
    public String analytics(Model model) {
        return "shared/admin/analytics";
    }
}
