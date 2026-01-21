package poly.edu.java5_asm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import poly.edu.java5_asm.dto.request.AddToCartRequest;
import poly.edu.java5_asm.dto.request.UpdateCartItemRequest;
import poly.edu.java5_asm.dto.response.CartResponse;
import poly.edu.java5_asm.entity.*;
import poly.edu.java5_asm.repository.CartItemRepository;
import poly.edu.java5_asm.repository.CartRepository;
import poly.edu.java5_asm.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Demo test file for CartService
 * This demonstrates basic unit testing patterns for the service layer
 */
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Cart testCart;
    private Product testProduct;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .build();

        // Setup test cart
        testCart = Cart.builder()
                .id(1L)
                .user(testUser)
                .build();

        // Setup test product
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("100000"))
                .stockQuantity(10)
                .isActive(true)
                .build();

        // Setup test cart item
        testCartItem = CartItem.builder()
                .id(1L)
                .cart(testCart)
                .product(testProduct)
                .quantity(2)
                .price(new BigDecimal("100000"))
                .build();
    }

    @Test
    void testGetOrCreateCart_WhenCartExists_ShouldReturnExistingCart() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

        // When
        Cart result = cartService.getOrCreateCart(testUser);

        // Then
        assertNotNull(result);
        assertEquals(testCart.getId(), result.getId());
        verify(cartRepository, times(1)).findByUser(testUser);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testGetOrCreateCart_WhenCartDoesNotExist_ShouldCreateNewCart() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // When
        Cart result = cartService.getOrCreateCart(testUser);

        // Then
        assertNotNull(result);
        verify(cartRepository, times(1)).findByUser(testUser);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddToCart_WithValidProduct_ShouldAddSuccessfully() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByCartAndProduct(testCart, testProduct)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);
        when(cartItemRepository.findByCart(testCart)).thenReturn(List.of(testCartItem));

        // When
        CartResponse result = cartService.addToCart(testUser, request);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalItems());
        verify(productRepository, times(1)).findById(1L);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddToCart_WithInsufficientStock_ShouldThrowException() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(20); // More than available stock

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(testUser, request);
        });

        assertEquals("Số lượng tồn kho không đủ", exception.getMessage());
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testAddToCart_WithInvalidQuantity_ShouldThrowException() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(0); // Invalid quantity

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(testUser, request);
        });

        assertEquals("Số lượng phải lớn hơn 0", exception.getMessage());
    }

    @Test
    void testUpdateCartItem_WithValidQuantity_ShouldUpdateSuccessfully() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setCartItemId(1L);
        request.setQuantity(3);

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);
        when(cartItemRepository.findByCart(testCart)).thenReturn(List.of(testCartItem));

        // When
        CartResponse result = cartService.updateCartItem(testUser, request);

        // Then
        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testRemoveFromCart_WithValidItem_ShouldRemoveSuccessfully() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.findByCart(testCart)).thenReturn(new ArrayList<>());

        // When
        CartResponse result = cartService.removeFromCart(testUser, 1L);

        // Then
        assertNotNull(result);
        verify(cartItemRepository, times(1)).delete(testCartItem);
    }

    @Test
    void testGetCart_ShouldReturnCartWithItems() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCart(testCart)).thenReturn(List.of(testCartItem));

        // When
        CartResponse result = cartService.getCart(testUser);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalItems());
        assertTrue(result.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testIsCartEmpty_WithEmptyCart_ShouldReturnTrue() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCart(testCart)).thenReturn(new ArrayList<>());

        // When
        boolean result = cartService.isCartEmpty(testUser);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsCartEmpty_WithItems_ShouldReturnFalse() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCart(testCart)).thenReturn(List.of(testCartItem));

        // When
        boolean result = cartService.isCartEmpty(testUser);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetCartItemCount_ShouldReturnCorrectCount() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCart(testCart)).thenReturn(List.of(testCartItem));

        // When
        Integer result = cartService.getCartItemCount(testUser);

        // Then
        assertNotNull(result);
        assertEquals(2, result); // testCartItem has quantity = 2
    }

    @Test
    void testClearCart_ShouldRemoveAllItems() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

        // When
        cartService.clearCart(testUser);

        // Then
        verify(cartItemRepository, times(1)).deleteByCart(testCart);
    }
}
