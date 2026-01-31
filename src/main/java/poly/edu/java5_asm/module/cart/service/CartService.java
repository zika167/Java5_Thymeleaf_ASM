package poly.edu.java5_asm.module.cart.service;

import poly.edu.java5_asm.module.cart.dto.request.AddToCartRequest;
import poly.edu.java5_asm.module.cart.dto.request.UpdateCartItemRequest;
import poly.edu.java5_asm.module.cart.dto.response.CartResponse;
import poly.edu.java5_asm.module.cart.entity.Cart;
import poly.edu.java5_asm.module.user.entity.User;

/**
 * Interface cho Cart Service
 */
public interface CartService {

    // === Cart Management ===
    Cart getOrCreateCart(User user);
    Cart getOrCreateGuestCart(String sessionId);
    Cart getOrCreateCartByIdentifier(String identifier);

    // === Add to Cart ===
    CartResponse addToCart(User user, AddToCartRequest request);
    CartResponse addToCartByIdentifier(String identifier, AddToCartRequest request);

    // === Update Cart Item ===
    CartResponse updateCartItem(User user, UpdateCartItemRequest request);
    CartResponse updateCartItemByIdentifier(String identifier, UpdateCartItemRequest request);

    // === Remove from Cart ===
    CartResponse removeFromCart(User user, Long cartItemId);
    CartResponse removeFromCartByIdentifier(String identifier, Long cartItemId);

    // === Clear Cart ===
    void clearCart(User user);
    void clearCartByIdentifier(String identifier);

    // === Get Cart ===
    CartResponse getCart(User user);
    CartResponse getCartByIdentifier(String identifier);

    // === Cart Info ===
    boolean isCartEmpty(User user);
    Integer getCartItemCount(User user);
    Integer getCartItemCountByIdentifier(String identifier);
}
