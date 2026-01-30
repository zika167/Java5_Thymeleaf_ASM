package poly.edu.java5_asm.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import poly.edu.java5_asm.module.product.dto.response.ProductListResponse;
import poly.edu.java5_asm.module.product.dto.response.ProductResponse;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.common.security.CustomUserDetails;
import poly.edu.java5_asm.module.cart.service.CartService;
import poly.edu.java5_asm.module.product.service.ProductService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return loadHomePage(model, userDetails, "index");
    }

    @GetMapping("/index-logined")
    public String indexLogined(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return loadHomePage(model, userDetails, "index");
    }

    private String loadHomePage(Model model, CustomUserDetails userDetails, String viewName) {
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

        return viewName;
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

        return "module/product/category";
    }

    @GetMapping("/product/detail")
    public String productDeltailTypo() {
        return "redirect:/category";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Product details - có thể null nếu không tìm thấy
        ProductResponse product = null;
        try {
            product = productService.getProductById(id);
        } catch (Exception e) {
            // Product not found - template sẽ hiển thị thông báo lỗi
        }
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

        return "module/product/product-detail";
    }

    @GetMapping("/cart")
    public String cart(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Cart count
        if (userDetails != null) {
            User user = userDetails.getUser();
            Integer cartCount = cartService.getCartItemCount(user);
            model.addAttribute("cartCount", cartCount);
        }

        return "module/cart/cart";
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

        return "module/order/checkout";
    }

    @GetMapping("/shipping")
    public String shipping(Model model) {
        return "module/order/shipping";
    }

    @GetMapping("/favourite")
    public String favourite(Model model) {
        return "module/wishlist/favourite";
    }

    @GetMapping("/add-new-card")
    public String addNewCard(Model model) {
        return "module/payment/add-new-card";
    }

    @GetMapping("/reset-password")
    public String resetPassword() {
        return "module/auth/reset-password";
    }

    @GetMapping("/reset-password-emailed")
    public String resetPasswordEmailed() {
        return "module/auth/reset-password-emailed";
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            User user = userDetails.getUser();
            Integer cartCount = cartService.getCartItemCount(user);
            model.addAttribute("cartCount", cartCount);
            model.addAttribute("user", user);
        }
        return "module/order/my-orders";
    }

    @GetMapping("/order-detail/{id}")
    public String orderDetail(@PathVariable Long id, Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            User user = userDetails.getUser();
            Integer cartCount = cartService.getCartItemCount(user);
            model.addAttribute("cartCount", cartCount);
        }
        model.addAttribute("orderId", id);
        return "module/order/order-detail";
    }

    @GetMapping("/addresses")
    public String addresses(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            User user = userDetails.getUser();
            Integer cartCount = cartService.getCartItemCount(user);
            model.addAttribute("cartCount", cartCount);
            model.addAttribute("user", user);
        }
        return "module/address/addresses";
    }
}
