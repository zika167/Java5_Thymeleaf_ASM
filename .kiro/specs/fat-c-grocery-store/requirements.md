# Requirements Document: Fat C Grocery Store

## Introduction

Fat C Grocery Store là một ứng dụng e-commerce bán hàng tạp hóa được xây dựng bằng Java Spring Boot, Thymeleaf, MariaDB và Docker. Dự án được phát triển bởi nhóm 4 thành viên với deadline cuối tháng (4 tuần).

## Glossary

- **System**: Fat C Grocery Store Application
- **User**: Người dùng cuối (khách hàng)
- **Admin**: Quản trị viên hệ thống
- **Cart**: Giỏ hàng
- **Session**: Phiên làm việc của người dùng
- **Traffic**: Lưu lượng truy cập
- **Theme**: Chế độ giao diện (Sáng/Tối)
- **Fragment**: Thành phần Thymeleaf có thể tái sử dụng
- **CI/CD**: Continuous Integration/Continuous Deployment

## Requirements

### Requirement 1: Smart Search System

**User Story:** As a user, I want to search and filter products, so that I can quickly find items I need.

#### Acceptance Criteria

1. WHEN a user enters a search query, THE System SHALL return matching products with pagination
2. WHEN a user applies brand filter, THE System SHALL display only products from selected brands
3. WHEN a user applies category filter, THE System SHALL display only products from selected categories
4. WHEN search results exceed page size, THE System SHALL provide pagination controls
5. WHEN a user combines multiple filters, THE System SHALL apply all filters simultaneously
6. WHEN no products match the criteria, THE System SHALL display a "no results" message

### Requirement 2: Shopping Cart Management

**User Story:** As a user, I want to manage items in my cart, so that I can prepare my order before checkout.

#### Acceptance Criteria

1. WHEN a guest user adds items to cart, THE System SHALL store cart data in session
2. WHEN a logged-in user adds items to cart, THE System SHALL store cart data in database
3. WHEN a user updates item quantity, THE System SHALL recalculate cart totals immediately
4. WHEN a user removes an item, THE System SHALL update cart and display confirmation
5. WHEN a guest user logs in, THE System SHALL merge session cart with database cart
6. WHEN cart is empty, THE System SHALL display an empty cart message

### Requirement 3: Checkout and Order Processing

**User Story:** As a user, I want to complete my purchase, so that I can receive my ordered items.

#### Acceptance Criteria

1. WHEN a user proceeds to checkout, THE System SHALL validate cart items and stock availability
2. WHEN a user selects shipping address, THE System SHALL calculate shipping fee
3. WHEN a user selects payment method, THE System SHALL display payment options (COD, VNPay, Momo)
4. WHEN an order is created, THE System SHALL generate a unique order number
5. WHEN an order is confirmed, THE System SHALL send confirmation email to user
6. WHEN payment is completed, THE System SHALL update order status to PAID
7. WHEN stock is insufficient, THE System SHALL prevent order creation and notify user

### Requirement 4: Email Notification System

**User Story:** As a user, I want to receive order confirmation emails, so that I have proof of my purchase.

#### Acceptance Criteria

1. WHEN an order is created, THE System SHALL send email with order details
2. WHEN order status changes, THE System SHALL send status update email
3. WHEN email sending fails, THE System SHALL log error and retry up to 3 times
4. THE Email SHALL contain order number, items, total amount, and shipping address

### Requirement 5: Admin Dashboard - User Statistics

**User Story:** As an admin, I want to view user statistics, so that I can monitor user growth.

#### Acceptance Criteria

1. WHEN admin accesses dashboard, THE System SHALL display total registered users
2. WHEN admin selects date range, THE System SHALL show new user registrations for that period
3. THE System SHALL display user registration trend chart (daily/weekly/monthly)
4. THE System SHALL show active vs inactive users count
5. THE System SHALL display user role distribution (USER vs ADMIN)

### Requirement 6: Admin Dashboard - Traffic Monitoring

**User Story:** As an admin, I want to monitor website traffic, so that I can understand user behavior.

#### Acceptance Criteria

1. WHEN a user visits any page, THE System SHALL log the page view with timestamp
2. WHEN a user logs in, THE System SHALL record login event with IP address
3. WHEN a user views a product, THE System SHALL increment product view count
4. WHEN admin accesses traffic dashboard, THE System SHALL display total page views
5. WHEN admin selects date range, THE System SHALL show traffic statistics for that period
6. THE System SHALL display most viewed products
7. THE System SHALL show peak traffic hours
8. THE System SHALL display unique visitors vs returning visitors

### Requirement 7: Frontend - Thymeleaf Integration

**User Story:** As a developer, I want to convert HTML to Thymeleaf templates, so that we have dynamic server-side rendering.

#### Acceptance Criteria

1. WHEN converting HTML files, THE System SHALL use Thymeleaf syntax for dynamic content
2. WHEN creating layouts, THE System SHALL use Thymeleaf fragments for reusable components
3. THE System SHALL have separate fragments for header, footer, navigation, and sidebar
4. WHEN rendering pages, THE System SHALL inject data from Spring controllers
5. THE System SHALL use Thymeleaf expressions for conditional rendering
6. THE System SHALL use Thymeleaf iteration for lists and collections

### Requirement 8: CI/CD Pipeline

**User Story:** As a developer, I want automated deployment, so that we can release updates quickly and reliably.

#### Acceptance Criteria

1. WHEN code is pushed to main branch, THE System SHALL trigger GitHub Actions workflow
2. WHEN tests pass, THE System SHALL build Docker image
3. WHEN Docker image is built, THE System SHALL push to DockerHub
4. WHEN image is pushed, THE System SHALL deploy to production server (Render/Railway)
5. WHEN deployment fails, THE System SHALL send notification to team
6. THE System SHALL run automated tests before deployment

### Requirement 9: Docker Configuration

**User Story:** As a developer, I want containerized application, so that deployment is consistent across environments.

#### Acceptance Criteria

1. THE System SHALL provide Dockerfile for Spring Boot application
2. THE System SHALL provide docker-compose.yml for local development
3. WHEN containers start, THE System SHALL initialize database with schema
4. WHEN containers start, THE System SHALL load seed data
5. THE System SHALL expose application on port 8080
6. THE System SHALL expose MariaDB on port 3306

## Project Timeline (4 Weeks)

### Week 1: Foundation & Core Features
- Database optimization and migration
- Backend core setup (Search, Cart basics)
- Thymeleaf conversion starts
- CI/CD pipeline setup

### Week 2: Business Logic & Integration
- Complete Cart & Checkout logic
- Email notification system
- Admin dashboard foundation
- Frontend Thymeleaf integration

### Week 3: Admin Features & Polish
- Traffic monitoring system
- Admin statistics and charts
- Frontend refinement
- Integration testing

### Week 4: Testing & Deployment
- End-to-end testing
- Performance optimization
- Production deployment
- Documentation and handover

## Team Structure

### Person 1: Team Leader / DevOps Specialist
- Project coordination and task management
- CI/CD pipeline setup and maintenance
- Docker configuration and deployment
- Code review and quality assurance

### Person 2: Backend Core Developer
- Search system with pagination and filters
- Cart management (Session + Database)
- Product and category services
- API endpoints for frontend

### Person 3: Backend Logic / Admin Developer
- Checkout and order processing
- Email notification system
- Admin dashboard backend
- Traffic monitoring and statistics

### Person 4: Frontend / Thymeleaf Specialist
- HTML to Thymeleaf conversion
- Fragment creation and management
- Theme switching implementation
- UI/UX refinement and responsiveness

## Traffic Monitoring Strategy

### Recommended Approach: Hybrid Solution

**Phase 1 (MVP - Week 2-3):** Database Logging
- Use `user_activity_logs` table for basic tracking
- Log: LOGIN, PAGE_VIEW, PRODUCT_VIEW, ADD_TO_CART, CHECKOUT
- Advantages: Simple, no external dependencies, full control
- Suitable for: Small to medium traffic (< 10,000 requests/day)

**Phase 2 (Future Enhancement):** Third-party Integration
- Consider: Google Analytics 4, Mixpanel, or Plausible
- Advantages: Advanced analytics, real-time dashboards, no database overhead
- Suitable for: High traffic, advanced analytics needs

**Implementation Priority:**
1. Week 2: Basic database logging (LOGIN, PAGE_VIEW)
2. Week 3: Admin dashboard with charts (Chart.js or ApexCharts)
3. Week 4: Optimization and caching for performance

### Why Database Logging First?
- ✅ No external API dependencies
- ✅ Full data ownership and privacy
- ✅ Custom queries for specific business needs
- ✅ Works offline and in development
- ✅ No additional costs
- ✅ Meets project deadline requirements

## Technical Stack Confirmation

- **Backend:** Java 17+, Spring Boot 3.x, Spring Data JPA
- **Frontend:** Thymeleaf, Bootstrap 5, JavaScript (Vanilla or Alpine.js)
- **Database:** MariaDB 10.11+
- **Containerization:** Docker, Docker Compose
- **CI/CD:** GitHub Actions
- **Deployment:** Render or Railway
- **Email:** Spring Mail (SMTP)
- **Charts:** Chart.js or ApexCharts
- **Testing:** JUnit 5, Mockito, Spring Boot Test

## Success Criteria

1. All core features (Search, Cart, Checkout, Admin) are functional
2. Application is fully containerized and deployable
3. CI/CD pipeline is operational
4. All HTML pages are converted to Thymeleaf
5. Theme switching works across all pages
6. Admin can view user and traffic statistics
7. Email notifications are sent successfully
8. Application is deployed to production server
9. Code is well-documented and tested
10. Project is delivered by deadline (end of month)
