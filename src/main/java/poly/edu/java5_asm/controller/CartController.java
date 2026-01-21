package poly.edu.java5_asm.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.dto.request.AddToCartRequest;
import poly.edu.java5_asm.dto.request.UpdateCartItemRequest;
import poly.edu.java5_asm.dto.response.CartResponse;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.security.CustomUserDetails;
import poly.edu.java5_asm.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    /**
     * Lấy user từ Authentication hoặc session ID cho guest
     */
    private String getCartIdentifier(Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            return "user_" + user.getId();
        }
        // Cho guest user, dùng session ID
        return "guest_" + session.getId();
    }

    /**
     * Lấy giỏ hàng hiện tại
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication, HttpSession session) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            CartResponse cart = cartService.getCartByIdentifier(cartId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            log.error("Lỗi khi lấy giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Thêm sản phẩm vào giỏ hàng (hỗ trợ cả guest user)
     */
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            Authentication authentication,
            HttpSession session,
            @RequestBody AddToCartRequest request) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            CartResponse cart = cartService.addToCartByIdentifier(cartId, request);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cập nhật số lượng item trong giỏ
     */
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCartItem(
            Authentication authentication,
            HttpSession session,
            @RequestBody UpdateCartItemRequest request) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            CartResponse cart = cartService.updateCartItemByIdentifier(cartId, request);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi khi cập nhật giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa item khỏi giỏ hàng
     */
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartResponse> removeFromCart(
            Authentication authentication,
            HttpSession session,
            @PathVariable Long cartItemId) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            CartResponse cart = cartService.removeFromCartByIdentifier(cartId, cartItemId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi khi xóa khỏi giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Lỗi khi xóa khỏi giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Authentication authentication, HttpSession session) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            cartService.clearCartByIdentifier(cartId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Lỗi khi xóa giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy số lượng items trong giỏ
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount(Authentication authentication, HttpSession session) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            Integer count = cartService.getCartItemCountByIdentifier(cartId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Lỗi khi lấy số lượng giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
