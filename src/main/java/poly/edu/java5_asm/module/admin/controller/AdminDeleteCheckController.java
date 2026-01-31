package poly.edu.java5_asm.module.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.brand.repository.BrandRepository;
import poly.edu.java5_asm.module.category.entity.Category;
import poly.edu.java5_asm.module.category.repository.CategoryRepository;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;
import poly.edu.java5_asm.module.order.repository.OrderRepository;
import poly.edu.java5_asm.module.order.repository.OrderItemRepository;
import poly.edu.java5_asm.module.review.repository.ReviewRepository;
import poly.edu.java5_asm.module.wishlist.repository.WishlistRepository;
import poly.edu.java5_asm.module.cart.repository.CartItemRepository;

import java.util.*;

/**
 * Controller để kiểm tra ràng buộc khóa ngoại trước khi xóa
 */
@RestController
@RequestMapping("/api/admin/check-delete")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDeleteCheckController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final WishlistRepository wishlistRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * Kiểm tra ràng buộc trước khi xóa User
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> checkUserDelete(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> result = new HashMap<>();
        List<String> warnings = new ArrayList<>();
        boolean canDelete = true;

        // Check orders
        int orderCount = user.getOrders() != null ? user.getOrders().size() : 0;
        if (orderCount > 0) {
            warnings.add("Có " + orderCount + " đơn hàng");
            canDelete = false;
        }

        // Check reviews
        int reviewCount = user.getReviews() != null ? user.getReviews().size() : 0;
        if (reviewCount > 0) {
            warnings.add("Có " + reviewCount + " đánh giá");
        }

        // Check wishlists
        int wishlistCount = user.getWishlists() != null ? user.getWishlists().size() : 0;
        if (wishlistCount > 0) {
            warnings.add("Có " + wishlistCount + " sản phẩm yêu thích");
        }

        // Check addresses
        int addressCount = user.getAddresses() != null ? user.getAddresses().size() : 0;
        if (addressCount > 0) {
            warnings.add("Có " + addressCount + " địa chỉ");
        }

        result.put("canDelete", canDelete);
        result.put("warnings", warnings);
        result.put("message", canDelete ? "Có thể xóa user này" : "Không thể xóa user có đơn hàng. Chỉ có thể vô hiệu hóa.");
        
        return ResponseEntity.ok(result);
    }

    /**
     * Kiểm tra ràng buộc trước khi xóa Product
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<Map<String, Object>> checkProductDelete(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> result = new HashMap<>();
        List<String> warnings = new ArrayList<>();
        boolean canHardDelete = true;

        // Check order items
        int orderItemCount = product.getOrderItems() != null ? product.getOrderItems().size() : 0;
        if (orderItemCount > 0) {
            warnings.add("Có trong " + orderItemCount + " đơn hàng");
            canHardDelete = false;
        }

        // Check reviews
        int reviewCount = product.getReviews() != null ? product.getReviews().size() : 0;
        if (reviewCount > 0) {
            warnings.add("Có " + reviewCount + " đánh giá");
        }

        // Check wishlists
        int wishlistCount = product.getWishlists() != null ? product.getWishlists().size() : 0;
        if (wishlistCount > 0) {
            warnings.add("Có trong " + wishlistCount + " danh sách yêu thích");
        }

        // Check cart items
        int cartItemCount = product.getCartItems() != null ? product.getCartItems().size() : 0;
        if (cartItemCount > 0) {
            warnings.add("Có trong " + cartItemCount + " giỏ hàng");
        }

        result.put("canHardDelete", canHardDelete);
        result.put("canSoftDelete", true); // Luôn có thể soft delete
        result.put("warnings", warnings);
        result.put("message", canHardDelete ? "Có thể xóa vĩnh viễn sản phẩm này" : "Sản phẩm có trong đơn hàng, chỉ có thể ẩn (soft delete)");
        
        return ResponseEntity.ok(result);
    }

    /**
     * Kiểm tra ràng buộc trước khi xóa Brand
     */
    @GetMapping("/brand/{id}")
    public ResponseEntity<Map<String, Object>> checkBrandDelete(@PathVariable Long id) {
        Brand brand = brandRepository.findById(id).orElse(null);
        if (brand == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> result = new HashMap<>();
        List<String> warnings = new ArrayList<>();
        boolean canDelete = true;

        // Check products
        int productCount = brand.getProducts() != null ? brand.getProducts().size() : 0;
        if (productCount > 0) {
            warnings.add("Có " + productCount + " sản phẩm thuộc thương hiệu này");
            canDelete = false;
        }

        result.put("canDelete", canDelete);
        result.put("warnings", warnings);
        result.put("productCount", productCount);
        result.put("message", canDelete ? "Có thể xóa thương hiệu này" : "Không thể xóa thương hiệu đang có sản phẩm. Vui lòng chuyển sản phẩm sang thương hiệu khác hoặc vô hiệu hóa.");
        
        return ResponseEntity.ok(result);
    }

    /**
     * Kiểm tra ràng buộc trước khi xóa Category
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<Map<String, Object>> checkCategoryDelete(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> result = new HashMap<>();
        List<String> warnings = new ArrayList<>();
        boolean canDelete = true;

        // Check products
        int productCount = category.getProducts() != null ? category.getProducts().size() : 0;
        if (productCount > 0) {
            warnings.add("Có " + productCount + " sản phẩm thuộc danh mục này");
            canDelete = false;
        }

        // Check sub-categories
        int subCategoryCount = category.getSubCategories() != null ? category.getSubCategories().size() : 0;
        if (subCategoryCount > 0) {
            warnings.add("Có " + subCategoryCount + " danh mục con");
            canDelete = false;
        }

        result.put("canDelete", canDelete);
        result.put("warnings", warnings);
        result.put("productCount", productCount);
        result.put("subCategoryCount", subCategoryCount);
        result.put("message", canDelete ? "Có thể xóa danh mục này" : "Không thể xóa danh mục đang có sản phẩm hoặc danh mục con.");
        
        return ResponseEntity.ok(result);
    }
}
