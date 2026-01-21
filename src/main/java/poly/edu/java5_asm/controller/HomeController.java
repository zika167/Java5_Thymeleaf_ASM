package poly.edu.java5_asm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import poly.edu.java5_asm.dto.response.ProductListResponse;
import poly.edu.java5_asm.dto.response.ProductResponse;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.security.CustomUserDetails;
import poly.edu.java5_asm.service.CartService;
import poly.edu.java5_asm.service.ProductService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Featured products
        ProductListResponse featuredProducts = productService.getFeaturedProducts(0, 8);
        model.addAttribute("featuredProducts", featuredProducts.getProducts());

        // Latest products
        ProductListResponse latestProducts = productService.getLatestProducts(0, 8);
        model.addAttribute("latestProducts", latestProducts.getProducts());

        // Categories
        model.addAttribute("categories", productService.getAllCategories());

        // Cart count
        if (userDetails != null) {
            User user = userDetails.getUser();
            Integer cartCount = cartService.getCartItemCount(user);
            model.addAttribute("cartCount", cartCount);
        }

        return "index";
    }

    @GetMapping("/category")
    public String category(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // All products with pagination
        ProductListResponse products = productService.getAllProducts(0, 12, "createdAt", "DESC");
        model.addAttribute("products", products.getProducts());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", products.getCurrentPage());

        // Categories for sidebar
        model.addAttribute("categories", productService.getAllCategories());

        // Brands for filter
        model.addAttribute("brands", productService.getAllBrands());

        // Cart count
        if (userDetails != null) {
            User user = userDetails.getUser();
            Integer cartCount = cartService.getCartItemCount(user);
            model.addAttribute("cartCount", cartCount);
        }

        return "category";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Product details
        ProductResponse product = productService.getProductById(id);
        model.addAttribute("product", product);

        // Related products (same category)
        ProductListResponse relatedProducts = productService.getAllProducts(0, 4, "createdAt", "DESC");
        model.addAttribute("relatedProducts", relatedProducts.getProducts());

        // Cart count
        if (userDetails != null) {
            User user = userDetails.getUser();
            Integer cartCount = cartService.getCartItemCount(user);
            model.addAttribute("cartCount", cartCount);
        }

        return "product-detail";
    }

    @GetMapping("/cart")
    public String cart(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Cart count
        if (userDetails != null) {
            User user = userDetails.getUser();
            Integer cartCount = cartService.getCartItemCount(user);
            model.addAttribute("cartCount", cartCount);
        }

        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            User user = userDetails.getUser();
            // Cart data
            model.addAttribute("cart", cartService.getCart(user));
            // Cart count
            Integer cartCount = cartService.getCartItemCount(user);
            model.addAttribute("cartCount", cartCount);
        }

        return "checkout";
    }

    @GetMapping("/shipping")
    public String shipping(Model model) {
        return "shipping";
    }

    @GetMapping("/payment")
    public String payment(Model model) {
        return "payment";
    }

    @GetMapping("/favourite")
    public String favourite(Model model) {
        return "favourite";
    }

    @GetMapping("/add-new-card")
    public String addNewCard(Model model) {
        return "add-new-card";
    }

    @GetMapping("/reset-password")
    public String resetPassword() {
        return "reset-password";
    }

    @GetMapping("/reset-password-emailed")
    public String resetPasswordEmailed() {
        return "reset-password-emailed";
    }
}
