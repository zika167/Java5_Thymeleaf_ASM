# Tài Liệu Thiết Kế: Fat C Grocery Store

## Tổng Quan

Fat C Grocery Store là ứng dụng e-commerce được xây dựng bằng Java Spring Boot, Thymeleaf, MariaDB và Docker. Hệ thống tuân theo kiến trúc phân lớp với sự tách biệt rõ ràng giữa presentation layer (Thymeleaf), business logic (Services) và data access (Repositories).

Thiết kế ưu tiên:
- **Đơn giản**: Triển khai dễ hiểu, phù hợp với timeline 4 tuần
- **Dễ bảo trì**: Cấu trúc code rõ ràng với trách nhiệm phân định
- **Khả năng mở rộng**: Tiếp cận database-first với caching để tối ưu hiệu suất
- **Dễ kiểm thử**: Service layer được thiết kế cho unit và integration testing

## Kiến Trúc

### Kiến Trúc Tổng Quan

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  (Thymeleaf Templates + Controllers + Theme Switcher)       │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      Business Layer                          │
│  (Services: Product, Cart, Order, User, Statistics)         │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    Data Access Layer                         │
│         (JPA Repositories + Entity Classes)                  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      Database Layer                          │
│              (MariaDB - 13 Tables)                          │
└─────────────────────────────────────────────────────────────┘
```

### Technology Stack

- **Backend Framework**: Spring Boot 3.x
- **Template Engine**: Thymeleaf
- **ORM**: Spring Data JPA (Hibernate)
- **Database**: MariaDB 10.11+
- **Security**: Spring Security
- **Email**: Spring Mail (SMTP)
- **Caching**: Spring Cache (Caffeine)
- **Testing**: JUnit 5, Mockito
- **Build**: Maven
- **Containerization**: Docker, Docker Compose
- **CI/CD**: GitHub Actions


## Components và Interfaces

### 1. Hệ Thống Tìm Kiếm (Search System)

#### Components
- **SearchController**: Xử lý các request tìm kiếm và phân trang
- **ProductService**: Business logic cho tìm kiếm và lọc sản phẩm
- **ProductRepository**: Truy cập dữ liệu với custom queries

#### Key Interfaces

```java
public interface ProductService {
    Page<Product> searchProducts(String keyword, Pageable pageable);
    Page<Product> filterByCategory(Long categoryId, Pageable pageable);
    Page<Product> filterByBrand(Long brandId, Pageable pageable);
    Page<Product> searchWithFilters(SearchCriteria criteria, Pageable pageable);
}
```

#### Luồng Tìm Kiếm (Search Flow)
1. User nhập từ khóa tìm kiếm trong UI
2. SearchController nhận request với pagination params
3. ProductService xây dựng query với filters
4. ProductRepository thực thi JPQL với FULLTEXT search
5. Kết quả trả về dạng Page<Product> với metadata
6. Thymeleaf render kết quả với pagination controls

### 2. Hệ Thống Giỏ Hàng (Shopping Cart System)

#### Components
- **CartController**: Xử lý các thao tác với giỏ hàng
- **CartService**: Business logic quản lý giỏ hàng
- **CartRepository**: Lưu trữ database
- **SessionCartService**: Giỏ hàng dựa trên session cho khách

#### Key Interfaces

```java
public interface CartService {
    Cart getOrCreateCart(Long userId, String sessionId);
    CartItem addItem(Long cartId, Long productId, int quantity);
    CartItem updateQuantity(Long cartItemId, int quantity);
    void removeItem(Long cartItemId);
    Cart mergeSessionCart(String sessionId, Long userId);
    BigDecimal calculateTotal(Long cartId);
}
```

#### Luồng Giỏ Hàng (Cart Flow)
- **Khách (Guest User)**: Giỏ hàng lưu trong HTTP Session
- **User đã đăng nhập**: Giỏ hàng lưu vào database
- **Sự kiện đăng nhập**: Session cart được merge với database cart


### 3. Hệ Thống Thanh Toán và Đơn Hàng (Checkout and Order System)

#### Components
- **CheckoutController**: Xử lý luồng thanh toán
- **OrderService**: Tạo và quản lý đơn hàng
- **PaymentService**: Tích hợp payment gateway
- **EmailService**: Gửi email xác nhận đơn hàng

#### Key Interfaces

```java
public interface OrderService {
    Order createOrder(CheckoutRequest request);
    Order updateOrderStatus(Long orderId, OrderStatus status);
    void processPayment(Long orderId, PaymentRequest payment);
    List<Order> getUserOrders(Long userId);
    OrderStats getOrderStatistics(LocalDate startDate, LocalDate endDate);
}

public interface EmailService {
    void sendOrderConfirmation(Order order);
    void sendOrderStatusUpdate(Order order, OrderStatus newStatus);
    void sendAsync(EmailMessage message);
}
```

#### Luồng Thanh Toán (Checkout Flow)
1. User click "Tiến hành thanh toán"
2. Hệ thống validate các item trong giỏ và tồn kho
3. User chọn địa chỉ giao hàng
4. User chọn phương thức thanh toán (COD/VNPay/Momo)
5. Hệ thống tạo đơn hàng với trạng thái PENDING
6. Xử lý thanh toán (nếu thanh toán online)
7. Cập nhật trạng thái đơn hàng thành CONFIRMED
8. Gửi email bất đồng bộ
9. Cập nhật số lượng tồn kho

### 4. Hệ Thống Admin Dashboard

#### Components
- **AdminDashboardController**: Các endpoint cho Admin UI
- **StatisticsService**: Thống kê user và đơn hàng
- **ActivityLogService**: Giám sát traffic
- **ChartDataService**: Format dữ liệu cho biểu đồ

#### Key Interfaces

```java
public interface StatisticsService {
    UserStats getUserStatistics(LocalDate startDate, LocalDate endDate);
    TrafficStats getTrafficStatistics(LocalDate startDate, LocalDate endDate);
    List<ProductViewStats> getTopViewedProducts(int limit);
    Map<Integer, Long> getTrafficByHour(LocalDate date);
}

public interface ActivityLogService {
    void logActivity(ActivityType type, Long userId, String sessionId, 
                    String ipAddress, String pageUrl);
    void logLogin(Long userId, String ipAddress);
    void logPageView(String sessionId, String pageUrl);
    void logProductView(Long productId, String sessionId);
}
```

### 5. Kiến Trúc Frontend

#### Cấu Trúc Thymeleaf

```
templates/
├── layout/
│   ├── main-layout.html          # Layout cơ bản
│   ├── admin-layout.html         # Layout admin
├── fragments/
│   ├── header.html               # Thanh navigation
│   ├── footer.html               # Footer
│   ├── sidebar.html              # Sidebar danh mục
│   ├── product-card.html         # Hiển thị sản phẩm
│   ├── pagination.html           # Điều khiển phân trang
│   └── theme-switcher.html       # Nút chuyển theme
├── pages/
│   ├── home.html
│   ├── product-list.html
│   ├── product-detail.html
│   ├── cart.html
│   ├── checkout.html
│   ├── order-confirmation.html
│   └── profile.html
└── admin/
    ├── dashboard.html
    ├── users.html
    ├── orders.html
    └── products.html
```

#### Ví Dụ Sử Dụng Fragment

```html
<!-- main-layout.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="fragments/header :: head"></head>
<body>
    <nav th:replace="fragments/header :: navbar"></nav>
    <div th:replace="${content}"></div>
    <footer th:replace="fragments/footer :: footer"></footer>
</body>
</html>
```


## Data Models

### Core Entities

#### User Entity
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private LocalDate registeredDate;
    private Boolean isActive;
    
    @Enumerated(EnumType.STRING)
    private Role role; // USER, ADMIN
    
    private LocalDateTime lastLoginAt;
    private Integer loginCount;
    
    @OneToMany(mappedBy = "user")
    private List<Address> addresses;
    
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
```

#### Product Entity
```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String slug;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer stockQuantity;
    private Integer lowStockThreshold;
    private Boolean isOutOfStock;
    private String imageUrl;
    private String searchKeywords;
    private String tags;
    private String sku;
    private String weight;
    private Boolean isFeatured;
    private Boolean isActive;
    private Integer viewCount;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;
}
```

#### Cart Entity
```java
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private String sessionId;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Trường tính toán
    public BigDecimal getTotal() {
        return items.stream()
            .map(item -> item.getPrice().multiply(
                BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

#### Order Entity
```java
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNumber;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    
    private String shippingMethod;
    private BigDecimal shippingFee;
    
    private String paymentMethod; // COD, VNPAY, MOMO
    private String paymentTransactionId;
    private String paymentGatewayResponse;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // PENDING, PAID, FAILED, REFUNDED
    
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    
    private String customerNote;
    private String adminNote;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
    
    private LocalDateTime orderedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
}
```

#### UserActivityLog Entity
```java
@Entity
@Table(name = "user_activity_logs")
public class UserActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private String sessionId;
    
    @Enumerated(EnumType.STRING)
    private ActivityType activityType; 
    // LOGIN, LOGOUT, PAGE_VIEW, PRODUCT_VIEW, SEARCH, ADD_TO_CART, CHECKOUT
    
    private String ipAddress;
    private String userAgent;
    private String pageUrl;
    
    @Column(columnDefinition = "JSON")
    private String metadata; // Dữ liệu bổ sung dạng JSON
    
    private LocalDateTime createdAt;
}
```

### DTOs (Data Transfer Objects)

```java
public class SearchCriteria {
    private String keyword;
    private Long categoryId;
    private Long brandId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean isFeatured;
    private String sortBy; // price, name, newest
    private String sortOrder; // asc, desc
}

public class CheckoutRequest {
    private Long cartId;
    private Long shippingAddressId;
    private String paymentMethod;
    private String customerNote;
}

public class TrafficStats {
    private Long totalPageViews;
    private Long uniqueVisitors;
    private List<PageViewCount> topPages;
    private Map<Integer, Long> trafficByHour;
}
```


## Correctness Properties

*Property là đặc tính hoặc hành vi phải đúng trong tất cả các lần thực thi hợp lệ của hệ thống—về cơ bản, là một phát biểu chính thức về những gì hệ thống nên làm. Properties đóng vai trò cầu nối giữa đặc tả có thể đọc được bởi con người và các đảm bảo tính đúng đắn có thể xác minh bằng máy.*

### Property 1: Kết Quả Tìm Kiếm Khớp Query

*Với mọi* query tìm kiếm và tổ hợp filter, tất cả sản phẩm trả về phải khớp với tiêu chí tìm kiếm (keyword trong name/description VÀ khớp với các filter đã chọn).

**Validates: Requirements 1.1, 1.2, 1.3, 1.5**

### Property 2: Tính Nhất Quán Của Phân Trang

*Với mọi* tập kết quả tìm kiếm, tổng số item trên tất cả các trang phải bằng tổng số đếm, và không có item nào xuất hiện trên nhiều trang.

**Validates: Requirements 1.4**

### Property 3: Tính Toán Tổng Giỏ Hàng

*Với mọi* giỏ hàng có items, tổng tiền tính toán phải bằng tổng của (giá item × số lượng) cho tất cả items trong giỏ.

**Validates: Requirements 2.3**

### Property 4: Lưu Trữ Giỏ Hàng

*Với mọi* user (khách hoặc đã đăng nhập), thêm item vào giỏ rồi lấy giỏ ra phải chứa item đó với số lượng và giá đúng.

**Validates: Requirements 2.1, 2.2**

### Property 5: Tính Đúng Đắn Của Merge Giỏ Hàng

*Với mọi* giỏ hàng khách và giỏ hàng user, merge chúng phải cho ra giỏ hàng chứa tất cả items unique với số lượng được cộng dồn cho items trùng.

**Validates: Requirements 2.5**

### Property 6: Xóa Item Khỏi Giỏ Hàng

*Với mọi* cart item, xóa nó phải cho ra giỏ hàng không còn chứa item đó và có tổng tiền được tính lại.

**Validates: Requirements 2.4**

### Property 7: Validate Tồn Kho

*Với mọi* lần thử checkout, nếu số lượng của bất kỳ cart item nào vượt quá tồn kho, việc tạo đơn hàng phải thất bại với thông báo lỗi phù hợp.

**Validates: Requirements 3.1, 3.7**

### Property 8: Tính Duy Nhất Của Mã Đơn Hàng

*Với mọi* hai đơn hàng được tạo bất kỳ lúc nào, mã đơn hàng của chúng phải là duy nhất.

**Validates: Requirements 3.4**

### Property 9: Chuyển Đổi Trạng Thái Đơn Hàng

*Với mọi* đơn hàng, chuyển đổi trạng thái phải tuân theo state machine hợp lệ (PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED, với CANCELLED là trạng thái kết thúc từ bất kỳ trạng thái non-terminal nào).

**Validates: Requirements 3.6**

### Property 10: Tính Đầy Đủ Của Nội Dung Email

*Với mọi* email xác nhận đơn hàng, nội dung email phải chứa mã đơn hàng, tất cả items trong đơn, tổng tiền và địa chỉ giao hàng.

**Validates: Requirements 4.1, 4.4**

### Property 11: Logic Retry Email

*Với mọi* lần gửi email thất bại, hệ thống phải retry tối đa 3 lần trước khi log lỗi cuối cùng.

**Validates: Requirements 4.3**

### Property 12: Độ Chính Xác Thống Kê User

*Với mọi* khoảng thời gian, tổng của user mới, user active và user inactive phải bằng tổng số user đã đăng ký trong khoảng thời gian đó.

**Validates: Requirements 5.1, 5.2, 5.4**

### Property 13: Tính Đầy Đủ Của Activity Logging

*Với mọi* hành động của user (login, xem trang, xem sản phẩm), một activity log entry phải được tạo với timestamp, user/session identifier và loại hành động.

**Validates: Requirements 6.1, 6.2, 6.3**

### Property 14: Tính Nhất Quán Thống Kê Traffic

*Với mọi* khoảng thời gian, tổng page views phải bằng tổng page views trên tất cả các giờ trong khoảng thời gian đó.

**Validates: Requirements 6.4, 6.5, 6.7**

### Property 15: Xếp Hạng Sản Phẩm Được Xem

*Với mọi* danh sách sản phẩm được xem nhiều nhất, các sản phẩm phải được sắp xếp theo view_count giảm dần.

**Validates: Requirements 6.6**

### Property 16: Phân Loại Visitor

*Với mọi* session, một visitor phải được phân loại là unique nếu đây là lần truy cập đầu tiên, hoặc returning nếu có activity logs trước đó.

**Validates: Requirements 6.8**

### Property 17: Thymeleaf Data Injection

*Với mọi* page template, tất cả model attributes được truyền từ controller phải có thể truy cập được trong HTML được render.

**Validates: Requirements 7.4**


## Xử Lý Lỗi (Error Handling)

### Chiến Lược Xử Lý Lỗi

Ứng dụng sử dụng cách tiếp cận xử lý lỗi theo lớp:

1. **Controller Layer**: Bắt exceptions và trả về HTTP status codes phù hợp
2. **Service Layer**: Throw business exceptions với thông báo có ý nghĩa
3. **Repository Layer**: Xử lý database exceptions

### Custom Exceptions

```java
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
}

public enum ErrorCode {
    PRODUCT_NOT_FOUND("Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK("Không đủ hàng trong kho", HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND("Không tìm thấy giỏ hàng", HttpStatus.NOT_FOUND),
    INVALID_CHECKOUT("Yêu cầu thanh toán không hợp lệ", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND),
    EMAIL_SEND_FAILED("Gửi email thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    PAYMENT_FAILED("Xử lý thanh toán thất bại", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS("Truy cập không được phép", HttpStatus.FORBIDDEN);
}
```

### Global Exception Handler

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ModelAndView handleBusinessException(BusinessException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorCode", ex.getErrorCode());
        mav.addObject("errorMessage", ex.getMessage());
        mav.setStatus(ex.getHttpStatus());
        return mav;
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        // Log lỗi
        logger.error("Lỗi không mong đợi", ex);
        
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", "Đã xảy ra lỗi không mong đợi");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }
}
```

## Chiến Lược Testing

### Cách Tiếp Cận Testing Kép (Dual Testing Approach)

Ứng dụng sử dụng cả **unit tests** và **property-based tests** để coverage toàn diện:

- **Unit tests**: Xác minh các ví dụ cụ thể, edge cases và điều kiện lỗi
- **Property tests**: Xác minh các properties phổ quát trên tất cả inputs
- Cả hai đều bổ sung cho nhau và cần thiết để coverage toàn diện

### Unit Testing

**Các Lĩnh Vực Tập Trung:**
- Các ví dụ cụ thể minh họa hành vi đúng
- Edge cases (giỏ hàng rỗng, hết hàng, input không hợp lệ)
- Điều kiện lỗi và xử lý exception
- Điểm tích hợp giữa các components

**Ví Dụ Unit Tests:**
```java
@Test
void testAddToCart_ValidProduct_Success() {
    // Given
    Product product = createTestProduct();
    Cart cart = createTestCart();
    
    // When
    CartItem item = cartService.addItem(cart.getId(), product.getId(), 2);
    
    // Then
    assertNotNull(item);
    assertEquals(2, item.getQuantity());
    assertEquals(product.getPrice(), item.getPrice());
}

@Test
void testCheckout_InsufficientStock_ThrowsException() {
    // Given
    Product product = createProductWithStock(5);
    Cart cart = createCartWithItem(product, 10);
    
    // When & Then
    assertThrows(BusinessException.class, () -> {
        orderService.createOrder(createCheckoutRequest(cart));
    });
}
```

### Property-Based Testing

**Framework**: JUnit 5 + QuickTheories hoặc jqwik

**Cấu Hình:**
- Tối thiểu 100 iterations cho mỗi property test
- Mỗi test được tag với tên feature và số property
- Format tag: `@Tag("Feature: fat-c-grocery-store, Property N: [property text]")`

**Ví Dụ Property Tests:**
```java
@Property
@Tag("Feature: fat-c-grocery-store, Property 3: Cart Total Calculation")
void cartTotalShouldEqualSumOfItems(@ForAll("carts") Cart cart) {
    // Với mọi giỏ hàng có items
    BigDecimal expectedTotal = cart.getItems().stream()
        .map(item -> item.getPrice().multiply(
            BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    BigDecimal actualTotal = cartService.calculateTotal(cart.getId());
    
    assertEquals(expectedTotal, actualTotal);
}
```

### Mục Tiêu Test Coverage

- **Unit test coverage**: > 80%
- **Integration test coverage**: > 60%
- **Property test coverage**: Tất cả 17 properties
- **Critical paths**: 100% coverage (checkout, payment, email)


## Bảo Mật (Security Considerations)

### Authentication & Authorization

**Cấu Hình Spring Security:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/products/**", "/search/**").permitAll()
                .requestMatchers("/cart/**").permitAll()
                .requestMatchers("/checkout/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
            );
        
        return http.build();
    }
}
```

### Bảo Mật Password

- Passwords được hash bằng BCrypt (strength 12)
- Yêu cầu password tối thiểu: 8 ký tự, 1 chữ hoa, 1 số
- Reset password qua email với token có thời hạn

### CSRF Protection

- CSRF tokens được bật cho tất cả POST/PUT/DELETE requests
- Thymeleaf tự động include CSRF tokens trong forms

### SQL Injection Prevention

- Tất cả queries sử dụng JPA/JPQL với parameterized queries
- Không có raw SQL với string concatenation

### XSS Prevention

- Thymeleaf tự động escape tất cả output mặc định
- Chỉ dùng `th:utext` cho nội dung admin đáng tin cậy
- Content Security Policy headers được cấu hình

### Session Security

- Secure session cookies (HttpOnly, Secure flags)
- Session timeout: 30 phút không hoạt động
- Session fixation protection được bật

## Tối Ưu Hiệu Suất (Performance Optimization)

### Chiến Lược Caching

**Cấu Hình Cache:**
```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
            "products", "categories", "brands"
        );
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000));
        return cacheManager;
    }
}
```

**Các Thao Tác Được Cache:**
- Danh sách sản phẩm (TTL 10 phút)
- Cây danh mục (TTL 10 phút)
- Danh sách thương hiệu (TTL 10 phút)

### Tối Ưu Database

**Indexes:**
- Products: name, slug, category_id, brand_id, is_featured
- Orders: user_id, order_number, status, ordered_at
- UserActivityLogs: user_id, session_id, activity_type, created_at
- FULLTEXT index trên products(name, description)

**Tối Ưu Query:**
- Dùng `@EntityGraph` để tránh N+1 queries
- Pagination cho tất cả list queries
- Lazy loading cho collections
- Database connection pooling (HikariCP)

### Tối Ưu Frontend

- CSS và JavaScript được minified
- Tối ưu hóa và lazy loading hình ảnh
- CDN cho static assets (Bootstrap, jQuery)
- Browser caching headers
- Gzip compression được bật


## Kiến Trúc Deployment

### Cài Đặt Docker

**Dockerfile:**
```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**docker-compose.yml:**
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mariadb://db:3306/java5_asm
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=root
    depends_on:
      - db
  
  db:
    image: mariadb:10.11
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=java5_asm
    volumes:
      - ./mariadb_data:/var/lib/mysql
      - ./mariadb_init:/docker-entrypoint-initdb.d
```

### CI/CD Pipeline

**GitHub Actions Workflow:**
```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Run tests
        run: mvn test
  
  build:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Build with Maven
        run: mvn clean package -DskipTests
      - name: Build Docker image
        run: docker build -t fatc-grocery:latest .
      - name: Push to DockerHub
        run: |
          echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
          docker tag fatc-grocery:latest ${{ secrets.DOCKER_USERNAME }}/fatc-grocery:latest
          docker push ${{ secrets.DOCKER_USERNAME }}/fatc-grocery:latest
  
  deploy:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to Render/Railway
        run: |
          # Lệnh deployment ở đây
```

### Cấu Hình Environment

**application.yml:**
```yaml
spring:
  profiles:
    active: ${SPRING_PROFILE:dev}
  datasource:
    url: ${DB_URL:jdbc:mariadb://localhost:3306/java5_asm}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: ${SHOW_SQL:false}
  mail:
    host: ${MAIL_HOST:smtp.gmail.com}
    port: ${MAIL_PORT:587}
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
  cache:
    type: caffeine

server:
  port: ${PORT:8080}
  compression:
    enabled: true
```

## Monitoring và Logging

### Chiến Lược Logging

**Log Levels:**
- ERROR: Lỗi nghiêm trọng cần xử lý ngay
- WARN: Vấn đề tiềm ẩn (hàng sắp hết, email retry thất bại)
- INFO: Sự kiện business quan trọng (đơn hàng được tạo, user đăng ký)
- DEBUG: Thông tin debug chi tiết (chỉ development)

**Cấu Hình Logging:**
```java
@Slf4j
@Service
public class OrderService {
    
    public Order createOrder(CheckoutRequest request) {
        log.info("Đang tạo đơn hàng cho user: {}", request.getUserId());
        
        try {
            Order order = processOrder(request);
            log.info("Tạo đơn hàng thành công: {}", order.getOrderNumber());
            return order;
        } catch (Exception e) {
            log.error("Tạo đơn hàng thất bại cho user: {}", request.getUserId(), e);
            throw new BusinessException(ErrorCode.ORDER_CREATION_FAILED, e);
        }
    }
}
```

### Health Checks

**Spring Boot Actuator:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

**Custom Health Indicators:**
- Kết nối database
- Tính khả dụng của email service
- Dung lượng đĩa
- Sử dụng memory


## Quyết Định Thiết Kế và Lý Do

### Tại Sao Dùng Database Logging Cho Traffic Monitoring?

**Quyết Định**: Sử dụng bảng `user_activity_logs` thay vì third-party analytics

**Lý Do**:
1. **Timeline**: Có thể triển khai trong 2-3 ngày vs 5-7 ngày cho tích hợp
2. **Chi Phí**: $0 vs $0-$200/tháng cho third-party services
3. **Kiểm Soát**: Sở hữu hoàn toàn dữ liệu và custom queries
4. **Quyền Riêng Tư**: Không chia sẻ dữ liệu với dịch vụ bên ngoài
5. **Offline**: Hoạt động trong development không cần internet
6. **Phù Hợp**: Đủ cho < 10,000 requests/ngày

**Trade-offs**:
- Analytics kém tinh vi hơn GA4/Mixpanel
- Cần viết query thủ công để có insights
- Overhead lưu trữ database
- Không có real-time dashboard sẵn

### Tại Sao Session + Database Cho Cart?

**Quyết Định**: Cách tiếp cận hybrid (session cho khách, database cho user đã đăng nhập)

**Lý Do**:
1. **Trải Nghiệm User**: Khách có thể mua hàng không cần đăng ký
2. **Persistence**: Giỏ hàng của user đã đăng nhập tồn tại sau khi đóng browser
3. **Merge Logic**: Chuyển đổi mượt mà khi khách đăng nhập
4. **Hiệu Suất**: Session carts không hit database

### Tại Sao Thymeleaf Thay Vì React/Vue?

**Quyết Định**: Server-side rendering với Thymeleaf

**Lý Do**:
1. **Timeline**: Phát triển nhanh hơn cho deadline 4 tuần
2. **Kỹ Năng Team**: Java developers quen thuộc với Thymeleaf
3. **SEO**: SEO tốt hơn out-of-the-box
4. **Đơn Giản**: Không cần quy trình build frontend riêng
5. **Tích Hợp**: Tích hợp chặt chẽ với Spring Security

## Cải Tiến Tương Lai

**Tính Năng Post-MVP:**
1. Analytics nâng cao với tích hợp third-party
2. Thông báo real-time (WebSocket)
3. Gợi ý sản phẩm (dựa trên ML)
4. Hỗ trợ đa ngôn ngữ (i18n)
5. Mobile app (React Native)
6. Tìm kiếm nâng cao (Elasticsearch)
7. Hệ thống quản lý kho
8. Quản lý nhà cung cấp
9. Chương trình khách hàng thân thiết
10. Tích hợp mạng xã hội

---

**Phiên Bản Tài Liệu Thiết Kế**: 1.0  
**Cập Nhật Lần Cuối**: 19 Tháng 1, 2026  
**Trạng Thái**: Sẵn Sàng Để Review
