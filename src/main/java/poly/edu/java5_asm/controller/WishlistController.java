package poly.edu.java5_asm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import poly.edu.java5_asm.dto.response.WishlistResponse;
import poly.edu.java5_asm.security.CustomUserDetails;
import poly.edu.java5_asm.service.WishlistService;

/**
 * REST Controller cho Wishlist
 * 
 * Improvements:
 * - Exception handling được xử lý bởi GlobalExceptionHandler
 * - Thêm pagination endpoint
 * - Thêm batch operations
 * - Logging
 */
@Slf4j
@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * Thêm sản phẩm vào wishlist
     * POST /api/wishlist/products/{productId}
     */
    @PostMapping("/products/{productId}")
    public ResponseEntity<?> addToWishlist(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        log.info("User {} adding product {} to wishlist", userDetails.getUserId(), productId);
        
        WishlistResponse wishlist = wishlistService.addToWishlist(
                userDetails.getUserId(),
                productId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(wishlist);
    }

    /**
     * Xóa sản phẩm khỏi wishlist
     * DELETE /api/wishlist/products/{productId}
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        log.info("User {} removing product {} from wishlist", userDetails.getUserId(), productId);
        
        wishlistService.removeFromWishlist(
                userDetails.getUserId(),
                productId
        );

        return ResponseEntity.ok(Map.of("message", "Đã xóa khỏi danh sách yêu thích"));
    }

    /**
     * Lấy danh sách wishlist của user
     * GET /api/wishlist
     */
    @GetMapping
    public ResponseEntity<?> getUserWishlist(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        log.debug("User {} getting wishlist", userDetails.getUserId());
        
        List<WishlistResponse> wishlist = wishlistService.getUserWishlist(
                userDetails.getUserId()
        );

        return ResponseEntity.ok(wishlist);
    }

    /**
     * Lấy danh sách wishlist với pagination (NEW)
     * GET /api/wishlist/paginated?page=0&size=10
     */
    @GetMapping("/paginated")
    public ResponseEntity<?> getUserWishlistPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        log.debug("User {} getting paginated wishlist (page: {}, size: {})", 
                userDetails.getUserId(), page, size);
        
        Page<WishlistResponse> wishlistPage = wishlistService.getUserWishlistPaginated(
                userDetails.getUserId(), page, size
        );

        return ResponseEntity.ok(wishlistPage);
    }

    /**
     * Kiểm tra sản phẩm có trong wishlist không
     * GET /api/wishlist/products/{productId}/check
     */
    @GetMapping("/products/{productId}/check")
    public ResponseEntity<?> checkInWishlist(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.ok(Map.of("inWishlist", false));
        }

        boolean inWishlist = wishlistService.isInWishlist(
                userDetails.getUserId(),
                productId
        );

        return ResponseEntity.ok(Map.of("inWishlist", inWishlist));
    }

    /**
     * Xóa toàn bộ wishlist
     * DELETE /api/wishlist
     */
    @DeleteMapping
    public ResponseEntity<?> clearWishlist(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        log.info("User {} clearing wishlist", userDetails.getUserId());
        
        wishlistService.clearWishlist(userDetails.getUserId());

        return ResponseEntity.ok(Map.of("message", "Đã xóa toàn bộ danh sách yêu thích"));
    }

    /**
     * Đếm số items trong wishlist
     * GET /api/wishlist/count
     */
    @GetMapping("/count")
    public ResponseEntity<?> getWishlistCount(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.ok(Map.of("count", 0));
        }

        long count = wishlistService.getWishlistCount(userDetails.getUserId());

        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * Toggle wishlist (add/remove)
     * POST /api/wishlist/products/{productId}/toggle
     */
    @PostMapping("/products/{productId}/toggle")
    public ResponseEntity<?> toggleWishlist(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        log.info("User {} toggling wishlist for product {}", userDetails.getUserId(), productId);
        
        boolean added = wishlistService.toggleWishlist(
                userDetails.getUserId(),
                productId
        );

        return ResponseEntity.ok(Map.of(
                "inWishlist", added,
                "message", added ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích"
        ));
    }

    /**
     * Batch add products to wishlist (NEW)
     * POST /api/wishlist/batch
     * Body: { "productIds": [1, 2, 3] }
     */
    @PostMapping("/batch")
    public ResponseEntity<?> addMultipleToWishlist(
            @RequestBody Map<String, List<Long>> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        List<Long> productIds = request.get("productIds");
        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Danh sách sản phẩm không được rỗng"));
        }

        log.info("User {} adding {} products to wishlist", 
                userDetails.getUserId(), productIds.size());
        
        List<WishlistResponse> results = wishlistService.addMultipleToWishlist(
                userDetails.getUserId(),
                productIds
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", String.format("Đã thêm %d sản phẩm vào danh sách yêu thích", results.size()),
                "items", results
        ));
    }

    /**
     * Batch remove products from wishlist (NEW)
     * DELETE /api/wishlist/batch
     * Body: { "productIds": [1, 2, 3] }
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> removeMultipleFromWishlist(
            @RequestBody Map<String, List<Long>> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bạn cần đăng nhập"));
        }

        List<Long> productIds = request.get("productIds");
        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Danh sách sản phẩm không được rỗng"));
        }

        log.info("User {} removing {} products from wishlist", 
                userDetails.getUserId(), productIds.size());
        
        wishlistService.removeMultipleFromWishlist(
                userDetails.getUserId(),
                productIds
        );

        return ResponseEntity.ok(Map.of(
                "message", String.format("Đã xóa %d sản phẩm khỏi danh sách yêu thích", productIds.size())
        ));
    }
}
