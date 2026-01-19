# Kế Hoạch Triển Khai: Fat C Grocery Store

## Tổng Quan

Tài liệu này mô tả các tasks cụ thể để triển khai Fat C Grocery Store theo thiết kế đã được phê duyệt. Mỗi task được chia nhỏ thành các bước có thể thực hiện, với references đến requirements cụ thể.

## Tasks

### 1. Cài Đặt Cơ Sở Dự Án và Database

- [ ] 1.1 Chạy database migration script
  - Thực thi `03-migration-optimize.sql` để tối ưu schema
  - Xác minh 13 bảng được tạo thành công
  - Kiểm tra indexes và foreign keys
  - _Requirements: 10.3, 10.4_

- [ ] 1.2 Tạo Entity classes cho core models
  - Tạo User, Product, Category, Brand entities
  - Tạo Cart, CartItem entities
  - Tạo Order, OrderItem, Address entities
  - Thêm annotations JPA phù hợp
  - _Requirements: 1.1, 2.1, 3.1_

- [ ] 1.3 Tạo Repository interfaces
  - ProductRepository với custom queries
  - CartRepository và CartItemRepository
  - OrderRepository và OrderItemRepository
  - UserRepository với authentication queries
  - _Requirements: 1.1, 2.1, 3.1_

- [ ] 1.4 Cấu hình Spring Boot application
  - Setup application.yml với database config
  - Cấu hình JPA và Hibernate
  - Setup logging configuration
  - Cấu hình connection pooling (HikariCP)
  - _Requirements: 10.1, 10.2_

### 2. Triển Khai Hệ Thống Tìm Kiếm

- [ ] 2.1 Tạo SearchCriteria DTO
  - Định nghĩa fields cho keyword, filters, sorting
  - Thêm validation annotations
  - _Requirements: 1.1, 1.5_

- [ ] 2.2 Implement ProductService với search logic
  - Method searchProducts với pagination
  - Method filterByCategory
  - Method filterByBrand
  - Method searchWithFilters (kết hợp nhiều filters)
  - _Requirements: 1.1, 1.2, 1.3, 1.5_

- [ ]* 2.3 Viết property test cho search results
  - **Property 1: Search Results Match Query**
  - **Validates: Requirements 1.1, 1.2, 1.3, 1.5**

- [ ]* 2.4 Viết property test cho pagination
  - **Property 2: Pagination Consistency**
  - **Validates: Requirements 1.4**

- [ ] 2.5 Tạo SearchController
  - Endpoint GET /search với query params
  - Xử lý pagination parameters
  - Trả về Page<Product> với metadata
  - _Requirements: 1.1, 1.4_

- [ ]* 2.6 Viết unit tests cho SearchController
  - Test với keyword hợp lệ
  - Test với filters
  - Test pagination
  - _Requirements: 1.1, 1.4_


### 3. Triển Khai Hệ Thống Giỏ Hàng

- [ ] 3.1 Tạo CartService với session management
  - Method getOrCreateCart cho cả user và guest
  - Logic lưu cart vào session cho guest
  - Logic lưu cart vào database cho logged-in user
  - _Requirements: 2.1, 2.2_

- [ ]* 3.2 Viết property test cho cart persistence
  - **Property 4: Cart Persistence**
  - **Validates: Requirements 2.1, 2.2**

- [ ] 3.3 Implement cart operations
  - Method addItem với validation
  - Method updateQuantity với recalculation
  - Method removeItem
  - Method calculateTotal
  - _Requirements: 2.3, 2.4_

- [ ]* 3.4 Viết property test cho cart total calculation
  - **Property 3: Cart Total Calculation**
  - **Validates: Requirements 2.3**

- [ ]* 3.5 Viết property test cho cart item removal
  - **Property 6: Cart Item Removal**
  - **Validates: Requirements 2.4**

- [ ] 3.6 Implement cart merge logic
  - Method mergeSessionCart khi user login
  - Logic kết hợp items từ session và database
  - Xử lý duplicate items (cộng dồn quantity)
  - _Requirements: 2.5_

- [ ]* 3.7 Viết property test cho cart merge
  - **Property 5: Cart Merge Correctness**
  - **Validates: Requirements 2.5**

- [ ] 3.8 Tạo CartController
  - Endpoint POST /cart/add
  - Endpoint PUT /cart/update
  - Endpoint DELETE /cart/remove
  - Endpoint GET /cart
  - _Requirements: 2.1, 2.3, 2.4_

- [ ]* 3.9 Viết unit tests cho CartController
  - Test add item thành công
  - Test update quantity
  - Test remove item
  - Test empty cart edge case
  - _Requirements: 2.1, 2.3, 2.4, 2.6_

### 4. Checkpoint - Đảm bảo tất cả tests pass

Đảm bảo tất cả tests pass, hỏi user nếu có thắc mắc.


### 5. Triển Khai Hệ Thống Checkout và Order

- [ ] 5.1 Tạo CheckoutRequest và OrderService
  - Định nghĩa CheckoutRequest DTO với validation
  - Tạo OrderService interface và implementation
  - _Requirements: 3.1, 3.4_

- [ ] 5.2 Implement order creation logic
  - Method createOrder với validation
  - Validate cart items và stock availability
  - Generate unique order number
  - Tạo order với status PENDING
  - _Requirements: 3.1, 3.4_

- [ ]* 5.3 Viết property test cho stock validation
  - **Property 7: Stock Validation**
  - **Validates: Requirements 3.1, 3.7**

- [ ]* 5.4 Viết property test cho order number uniqueness
  - **Property 8: Order Number Uniqueness**
  - **Validates: Requirements 3.4**

- [ ] 5.5 Implement order status management
  - Method updateOrderStatus
  - Validate state transitions (PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED)
  - Xử lý CANCELLED state
  - _Requirements: 3.6_

- [ ]* 5.6 Viết property test cho order status transitions
  - **Property 9: Order Status Transitions**
  - **Validates: Requirements 3.6**

- [ ] 5.7 Tạo PaymentService stub
  - Interface cho payment gateway integration
  - Method processPayment cho COD, VNPay, Momo
  - Cập nhật payment status
  - _Requirements: 3.3, 3.6_

- [ ] 5.8 Tạo CheckoutController
  - Endpoint POST /checkout
  - Validate user authentication
  - Xử lý shipping address selection
  - Xử lý payment method selection
  - _Requirements: 3.2, 3.3_

- [ ]* 5.9 Viết unit tests cho checkout flow
  - Test checkout thành công
  - Test insufficient stock error
  - Test invalid address error
  - _Requirements: 3.1, 3.7_


### 6. Triển Khai Email Notification System

- [ ] 6.1 Cấu hình Spring Mail
  - Setup SMTP configuration trong application.yml
  - Tạo MailConfig class
  - Configure async email sending
  - _Requirements: 4.1_

- [ ] 6.2 Tạo email templates
  - Template cho order confirmation
  - Template cho order status update
  - Sử dụng Thymeleaf cho email templates
  - _Requirements: 4.1, 4.4_

- [ ] 6.3 Implement EmailService
  - Method sendOrderConfirmation
  - Method sendOrderStatusUpdate
  - Method sendAsync với @Async
  - _Requirements: 4.1, 4.2_

- [ ]* 6.4 Viết property test cho email content
  - **Property 10: Email Content Completeness**
  - **Validates: Requirements 4.1, 4.4**

- [ ] 6.5 Implement email retry logic
  - Retry mechanism với max 3 attempts
  - Exponential backoff
  - Log final failure
  - _Requirements: 4.3_

- [ ]* 6.6 Viết property test cho email retry
  - **Property 11: Email Retry Logic**
  - **Validates: Requirements 4.3**

- [ ]* 6.7 Viết unit tests cho EmailService
  - Test email sending thành công
  - Test retry logic
  - Test failure logging
  - _Requirements: 4.1, 4.3_

### 7. Checkpoint - Đảm bảo email system hoạt động

Đảm bảo tất cả tests pass, test gửi email thực tế với SMTP.


### 8. Triển Khai Admin Dashboard - User Statistics

- [ ] 8.1 Tạo UserActivityLog entity và repository
  - Entity với các fields: activityType, userId, sessionId, ipAddress, pageUrl
  - ActivityLogRepository với custom queries
  - _Requirements: 6.1, 6.2, 6.3_

- [ ] 8.2 Implement ActivityLogService
  - Method logActivity
  - Method logLogin
  - Method logPageView
  - Method logProductView
  - _Requirements: 6.1, 6.2, 6.3_

- [ ]* 8.3 Viết property test cho activity logging
  - **Property 13: Activity Logging Completeness**
  - **Validates: Requirements 6.1, 6.2, 6.3**

- [ ] 8.4 Tạo StatisticsService cho user stats
  - Method getUserStatistics với date range
  - Tính total registered users
  - Tính new users trong period
  - Tính active vs inactive users
  - Tính user role distribution
  - _Requirements: 5.1, 5.2, 5.4, 5.5_

- [ ]* 8.5 Viết property test cho user statistics
  - **Property 12: User Statistics Accuracy**
  - **Validates: Requirements 5.1, 5.2, 5.4**

- [ ]* 8.6 Viết unit tests cho StatisticsService
  - Test user count accuracy
  - Test date range filtering
  - Test role distribution
  - _Requirements: 5.1, 5.2, 5.5_


### 9. Triển Khai Admin Dashboard - Traffic Monitoring

- [ ] 9.1 Implement traffic statistics methods
  - Method getTrafficStatistics với date range
  - Method getTopViewedProducts
  - Method getTrafficByHour
  - Query để tính unique visitors vs returning visitors
  - _Requirements: 6.4, 6.5, 6.6, 6.7, 6.8_

- [ ]* 9.2 Viết property test cho traffic statistics
  - **Property 14: Traffic Statistics Consistency**
  - **Validates: Requirements 6.4, 6.5, 6.7**

- [ ]* 9.3 Viết property test cho product view ranking
  - **Property 15: Product View Ranking**
  - **Validates: Requirements 6.6**

- [ ]* 9.4 Viết property test cho visitor classification
  - **Property 16: Visitor Classification**
  - **Validates: Requirements 6.8**

- [ ] 9.5 Tạo ChartDataService
  - Format dữ liệu cho Chart.js
  - Method getRegistrationTrendData
  - Method getTrafficByHourData
  - Method getTopProductsData
  - _Requirements: 5.3, 6.7_

- [ ] 9.6 Tạo AdminDashboardController
  - Endpoint GET /admin/dashboard
  - Endpoint GET /admin/statistics/users
  - Endpoint GET /admin/statistics/traffic
  - Endpoint GET /admin/statistics/products
  - _Requirements: 5.1, 6.4_

- [ ]* 9.7 Viết unit tests cho AdminDashboardController
  - Test authorization (chỉ ADMIN)
  - Test data accuracy
  - Test date range filtering
  - _Requirements: 5.1, 6.4_

### 10. Checkpoint - Đảm bảo admin dashboard hoạt động

Đảm bảo tất cả tests pass, verify statistics accuracy.


### 11. Triển Khai Frontend - Thymeleaf Conversion

- [ ] 11.1 Tạo layout structure
  - Tạo main-layout.html với header, footer, content
  - Tạo admin-layout.html
  - Setup CSS và JavaScript includes
  - _Requirements: 7.1, 7.2_

- [ ] 11.2 Tạo reusable fragments
  - header.html với navigation bar
  - footer.html
  - sidebar.html cho categories
  - product-card.html
  - pagination.html
  - _Requirements: 7.2, 7.3_

- [ ] 11.3 Convert trang Home
  - home.html với featured products
  - Inject data từ HomeController
  - Hiển thị categories và banners
  - _Requirements: 7.4_

- [ ] 11.4 Convert trang Product List
  - product-list.html với search results
  - Pagination controls
  - Filter sidebar
  - _Requirements: 7.4, 7.6_

- [ ] 11.5 Convert trang Product Detail
  - product-detail.html
  - Hiển thị product info, images, reviews
  - Add to cart button
  - _Requirements: 7.4_

- [ ] 11.6 Convert trang Cart
  - cart.html với cart items
  - Update quantity controls
  - Remove item buttons
  - Total calculation display
  - _Requirements: 7.4, 7.6_

- [ ] 11.7 Convert trang Checkout
  - checkout.html
  - Shipping address selection
  - Payment method selection
  - Order summary
  - _Requirements: 7.4_

- [ ] 11.8 Convert trang Order Confirmation
  - order-confirmation.html
  - Hiển thị order details
  - Thank you message
  - _Requirements: 7.4_

- [ ]* 11.9 Viết property test cho Thymeleaf data injection
  - **Property 17: Thymeleaf Data Injection**
  - **Validates: Requirements 7.4**

- [ ]* 11.10 Viết integration tests cho pages
  - Test rendering với mock data
  - Test conditional rendering
  - Test iteration
  - _Requirements: 7.4, 7.5, 7.6_


### 12. Triển Khai Admin Frontend Pages

- [ ] 12.1 Convert Admin Dashboard page
  - admin/dashboard.html
  - User statistics charts (Chart.js)
  - Traffic statistics charts
  - Top products table
  - _Requirements: 5.3, 6.4_

- [ ] 12.2 Convert Admin Users page
  - admin/users.html
  - User list với pagination
  - User details modal
  - _Requirements: 5.1_

- [ ] 12.3 Convert Admin Orders page
  - admin/orders.html
  - Order list với filters
  - Order status update controls
  - _Requirements: 3.6_

- [ ] 12.4 Convert Admin Products page
  - admin/products.html
  - Product list với CRUD operations
  - Stock management
  - _Requirements: 1.1_

### 13. Triển Khai Security

- [ ] 13.1 Cấu hình Spring Security
  - SecurityConfig với authorization rules
  - Form login configuration
  - Session management
  - _Requirements: 3.2_

- [ ] 13.2 Implement password security
  - BCrypt password encoder
  - Password validation rules
  - _Requirements: 3.2_

- [ ] 13.3 Setup CSRF protection
  - Enable CSRF tokens
  - Thymeleaf CSRF integration
  - _Requirements: 3.2_

- [ ]* 13.4 Viết security tests
  - Test authentication
  - Test authorization (ADMIN vs USER)
  - Test CSRF protection
  - _Requirements: 3.2_

### 14. Checkpoint - Đảm bảo security hoạt động

Đảm bảo authentication và authorization hoạt động đúng.


### 15. Triển Khai Caching và Performance Optimization

- [ ] 15.1 Cấu hình Caffeine cache
  - CacheConfig với cache managers
  - Cache cho products, categories, brands
  - TTL 10 phút
  - _Requirements: 1.1_

- [ ] 15.2 Thêm @Cacheable annotations
  - ProductService methods
  - CategoryService methods
  - BrandService methods
  - _Requirements: 1.1_

- [ ] 15.3 Optimize database queries
  - Thêm @EntityGraph để tránh N+1 queries
  - Review và optimize JPQL queries
  - _Requirements: 1.1, 2.1, 3.1_

- [ ]* 15.4 Viết performance tests
  - Test cache hit/miss
  - Test query performance
  - _Requirements: 1.1_

### 16. Triển Khai Docker và CI/CD

- [ ] 16.1 Tạo Dockerfile
  - Multi-stage build cho Spring Boot
  - Optimize image size
  - _Requirements: 10.1_

- [ ] 16.2 Cập nhật docker-compose.yml
  - Service cho app và database
  - Environment variables
  - Volume mounts
  - _Requirements: 10.2, 10.5, 10.6_

- [ ] 16.3 Tạo GitHub Actions workflow
  - Job test: chạy unit và integration tests
  - Job build: build Docker image
  - Job deploy: push to DockerHub
  - _Requirements: 9.1, 9.2, 9.3, 9.6_

- [ ] 16.4 Setup deployment scripts
  - Script deploy lên Render/Railway
  - Environment configuration
  - _Requirements: 9.4_

- [ ]* 16.5 Test CI/CD pipeline
  - Push code và verify workflow
  - Test deployment
  - _Requirements: 9.1, 9.4_

### 17. Checkpoint - Đảm bảo deployment hoạt động

Verify application chạy được trong Docker và deploy thành công.


### 18. Integration Testing và Bug Fixes

- [ ] 18.1 End-to-end testing cho checkout flow
  - Test complete flow: browse → add to cart → checkout → order → email
  - Test với cả guest và logged-in user
  - _Requirements: 1.1, 2.1, 3.1, 4.1_

- [ ] 18.2 Test user registration và login flow
  - Test registration → login → cart merge
  - Test session management
  - _Requirements: 2.5_

- [ ] 18.3 Test admin dashboard accuracy
  - Verify statistics với real data
  - Test charts rendering
  - _Requirements: 5.1, 6.4_

- [ ] 18.4 Cross-browser testing
  - Test trên Chrome, Firefox, Safari
  - Test responsive design
  - _Requirements: 7.4_

- [ ] 18.5 Fix bugs từ testing
  - Prioritize critical bugs
  - Fix và verify fixes
  - _Requirements: All_

### 19. Performance Testing và Optimization

- [ ] 19.1 Load testing với JMeter
  - Test với 100 concurrent users
  - Measure response times
  - _Requirements: 1.1, 2.1, 3.1_

- [ ] 19.2 Database query profiling
  - Identify slow queries
  - Add missing indexes
  - _Requirements: 1.1, 2.1, 3.1_

- [ ] 19.3 Frontend optimization
  - Minify CSS và JavaScript
  - Optimize images
  - Enable Gzip compression
  - _Requirements: 7.4_

- [ ]* 19.4 Verify performance targets
  - Page load < 2s
  - Search response < 500ms
  - Cart operations < 200ms
  - _Requirements: 1.1, 2.1_

### 20. Final Checkpoint - Production Ready

- [ ] 20.1 Verify tất cả tests pass
  - Unit tests > 80% coverage
  - Integration tests pass
  - All 17 property tests pass
  - _Requirements: All_

- [ ] 20.2 Code review và cleanup
  - Remove debug code
  - Clean up comments
  - Format code
  - _Requirements: All_

- [ ] 20.3 Documentation
  - Update README.md
  - API documentation
  - Deployment guide
  - _Requirements: All_

- [ ] 20.4 Production deployment
  - Deploy to production server
  - Verify application health
  - Monitor logs
  - _Requirements: 9.4, 10.5, 10.6_

## Notes

- Tasks đánh dấu `*` là optional và có thể skip để MVP nhanh hơn
- Mỗi task reference đến requirements cụ thể để traceability
- Checkpoints đảm bảo validation tăng dần
- Property tests validate universal correctness properties
- Unit tests validate specific examples và edge cases

---

**Phiên Bản**: 1.0  
**Tạo Ngày**: 19 Tháng 1, 2026  
**Trạng Thái**: Sẵn Sàng Để Thực Thi
