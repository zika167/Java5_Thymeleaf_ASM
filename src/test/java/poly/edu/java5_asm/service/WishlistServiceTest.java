package poly.edu.java5_asm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import poly.edu.java5_asm.dto.response.WishlistResponse;
import poly.edu.java5_asm.entity.Brand;
import poly.edu.java5_asm.entity.Category;
import poly.edu.java5_asm.entity.Product;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.entity.Wishlist;
import poly.edu.java5_asm.exception.ProductNotFoundException;
import poly.edu.java5_asm.exception.ProductUnavailableException;
import poly.edu.java5_asm.exception.UserNotFoundException;
import poly.edu.java5_asm.exception.WishlistDuplicateException;
import poly.edu.java5_asm.exception.WishlistNotFoundException;
import poly.edu.java5_asm.repository.ProductRepository;
import poly.edu.java5_asm.repository.UserRepository;
import poly.edu.java5_asm.repository.WishlistRepository;
import poly.edu.java5_asm.service.impl.WishlistServiceImpl;

/**
 * Unit tests cho WishlistService
 */
@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    private User testUser;
    private Product testProduct;
    private Wishlist testWishlist;
    private Category testCategory;
    private Brand testBrand;

    @BeforeEach
    void setUp() {
        // Setup test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");

        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("Apple");

        testProduct = Product.builder()
                .id(1L)
                .name("iPhone 15 Pro")
                .price(new BigDecimal("29990000"))
                .discountPrice(new BigDecimal("27990000"))
                .imageUrl("/assets/img/products/iphone-15-pro.jpg")
                .stockQuantity(50)
                .isActive(true)
                .isOutOfStock(false)
                .category(testCategory)
                .brand(testBrand)
                .build();

        testWishlist = Wishlist.builder()
                .id(1L)
                .user(testUser)
                .product(testProduct)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testAddToWishlist_Success() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(testWishlist);

        // When
        WishlistResponse result = wishlistService.addToWishlist(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getProductId());
        assertEquals("iPhone 15 Pro", result.getProductName());
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void testAddToWishlist_DuplicateThrowsException() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(true);

        // When & Then
        assertThrows(WishlistDuplicateException.class, () -> {
            wishlistService.addToWishlist(1L, 1L);
        });

        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

    @Test
    void testAddToWishlist_UserNotFoundThrowsException() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            wishlistService.addToWishlist(1L, 1L);
        });
    }

    @Test
    void testAddToWishlist_ProductNotFoundThrowsException() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            wishlistService.addToWishlist(1L, 1L);
        });
    }

    @Test
    void testAddToWishlist_InactiveProductThrowsException() {
        // Given
        testProduct.setIsActive(false);
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        assertThrows(ProductUnavailableException.class, () -> {
            wishlistService.addToWishlist(1L, 1L);
        });
    }

    @Test
    void testAddToWishlist_OutOfStockProductThrowsException() {
        // Given
        testProduct.setIsOutOfStock(true);
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        assertThrows(ProductUnavailableException.class, () -> {
            wishlistService.addToWishlist(1L, 1L);
        });
    }

    @Test
    void testRemoveFromWishlist_Success() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(true);

        // When
        wishlistService.removeFromWishlist(1L, 1L);

        // Then
        verify(wishlistRepository).deleteByUserIdAndProductId(1L, 1L);
    }

    @Test
    void testRemoveFromWishlist_NotFoundThrowsException() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);

        // When & Then
        assertThrows(WishlistNotFoundException.class, () -> {
            wishlistService.removeFromWishlist(1L, 1L);
        });
    }

    @Test
    void testGetUserWishlist_Success() {
        // Given
        List<Wishlist> wishlists = Arrays.asList(testWishlist);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(wishlistRepository.findByUserIdWithProduct(1L)).thenReturn(wishlists);

        // When
        List<WishlistResponse> result = wishlistService.getUserWishlist(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("iPhone 15 Pro", result.get(0).getProductName());
    }

    @Test
    void testGetUserWishlist_UserNotFoundThrowsException() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            wishlistService.getUserWishlist(1L);
        });
    }

    @Test
    void testGetUserWishlistPaginated_Success() {
        // Given
        List<Wishlist> wishlists = Arrays.asList(testWishlist);
        Page<Wishlist> wishlistPage = new PageImpl<>(wishlists);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(wishlistRepository.findByUserIdWithProductPaginated(eq(1L), any(Pageable.class)))
                .thenReturn(wishlistPage);

        // When
        Page<WishlistResponse> result = wishlistService.getUserWishlistPaginated(1L, 0, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("iPhone 15 Pro", result.getContent().get(0).getProductName());
    }

    @Test
    void testIsInWishlist_ReturnsTrue() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(true);

        // When
        boolean result = wishlistService.isInWishlist(1L, 1L);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsInWishlist_ReturnsFalse() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);

        // When
        boolean result = wishlistService.isInWishlist(1L, 1L);

        // Then
        assertFalse(result);
    }

    @Test
    void testClearWishlist_Success() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(wishlistRepository.countByUserId(1L)).thenReturn(5L);

        // When
        wishlistService.clearWishlist(1L);

        // Then
        verify(wishlistRepository).deleteByUserId(1L);
    }

    @Test
    void testGetWishlistCount_ReturnsCorrectCount() {
        // Given
        when(wishlistRepository.countByUserId(1L)).thenReturn(5L);

        // When
        long result = wishlistService.getWishlistCount(1L);

        // Then
        assertEquals(5L, result);
    }

    @Test
    void testToggleWishlist_AddsWhenNotExists() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L))
                .thenReturn(false)
                .thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(testWishlist);

        // When
        boolean result = wishlistService.toggleWishlist(1L, 1L);

        // Then
        assertTrue(result);
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void testToggleWishlist_RemovesWhenExists() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L))
                .thenReturn(true)
                .thenReturn(true);

        // When
        boolean result = wishlistService.toggleWishlist(1L, 1L);

        // Then
        assertFalse(result);
        verify(wishlistRepository).deleteByUserIdAndProductId(1L, 1L);
    }

    @Test
    void testGetWishlistProductIds_ReturnsCorrectIds() {
        // Given
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);
        when(wishlistRepository.findProductIdsByUserId(1L)).thenReturn(productIds);

        // When
        List<Long> result = wishlistService.getWishlistProductIds(1L);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
        assertTrue(result.contains(3L));
    }
}
