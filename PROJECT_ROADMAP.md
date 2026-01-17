# Fat C Grocery Store - Lộ Trình Phát Triển 4 Tuần

## 📊 BẢNG PHÂN CHIA TASK CHI TIẾT

### TUẦN 1: FOUNDATION & CORE FEATURES (Ngày 1-7)

| Người | Vai Trò | Tasks | Deliverables | Ưu Tiên |
|-------|---------|-------|--------------|---------|
| **Người 1** | Team Leader/DevOps | • Setup GitHub repository structure<br>• Tạo Docker Compose cho dev environment<br>• Setup CI/CD pipeline cơ bản (GitHub Actions)<br>• Tạo Dockerfile cho Spring Boot app<br>• Code review guidelines | • docker-compose.yml<br>• Dockerfile<br>• .github/workflows/ci.yml<br>• README.md với hướng dẫn setup | 🔴 Cao |
| **Người 2** | Backend Core | • Optimize database schema (chạy migration)<br>• Tạo Entity classes (Product, Category, Brand, Cart)<br>• Implement Search API với pagination<br>• Implement Filter API (brand, category)<br>• Tạo ProductService và CategoryService | • Entity classes<br>• SearchController<br>• ProductService<br>• Unit tests cho Search | 🔴 Cao |
| **Người 3** | Backend Logic/Admin | • Tạo Entity classes (User, Order, Address)<br>• Implement UserService<br>• Setup Spring Security cơ bản<br>• Tạo cấu trúc Admin controller<br>• Setup Spring Mail configuration | • User entities<br>• UserService<br>• SecurityConfig<br>• MailConfig | 🟡 Trung bình |
| **Người 4** | Frontend/Thymeleaf | • Phân tích cấu trúc HTML hiện tại<br>• Tạo layout chính (layout.html)<br>• Tạo fragments (header, footer, nav, sidebar)<br>• Convert trang Home sang Thymeleaf<br>• Convert trang Product List sang Thymeleaf | • layout.html<br>• fragments/*.html<br>• home.html<br>• product-list.html | 🔴 Cao |

**Checkpoint Tuần 1:**
- ✅ Docker environment chạy được
- ✅ Database schema đã optimize
- ✅ Search API hoạt động với pagination
- ✅ Layout Thymeleaf cơ bản hoàn thành

---

### TUẦN 2: BUSINESS LOGIC & INTEGRATION (Ngày 8-14)

| Người | Vai Trò | Tasks | Deliverables | Ưu Tiên |
|-------|---------|-------|--------------|---------|
| **Người 1** | Team Leader/DevOps | • Hoàn thiện CI/CD pipeline (build + test + deploy)<br>• Setup DockerHub integration<br>• Configure deployment cho Render/Railway<br>• Setup environment variables management<br>• Monitor và fix integration issues | • Complete CI/CD workflow<br>• DockerHub auto-push<br>• Deployment scripts<br>• .env.example | 🔴 Cao |
| **Người 2** | Backend Core | • Implement Cart Session management<br>• Implement Cart Database persistence<br>• Tạo CartService với merge logic<br>• Implement Add/Update/Remove cart items<br>• Tạo Cart API endpoints | • CartService<br>• CartController<br>• Session management<br>• Cart merge logic<br>• Unit tests | 🔴 Cao |
| **Người 3** | Backend Logic/Admin | • Implement Checkout flow<br>• Tạo OrderService<br>• Implement order creation logic<br>• Setup Email templates<br>• Implement email sending service<br>• Tạo bảng user_activity_logs | • OrderService<br>• CheckoutController<br>• EmailService<br>• Email templates<br>• Activity logging | 🔴 Cao |
| **Người 4** | Frontend/Thymeleaf | • Convert trang Product Detail<br>• Convert trang Cart<br>• Implement Theme Switcher UI<br>• Tạo JavaScript cho theme switching<br>• Integrate với localStorage<br>• Convert trang Login/Register | • product-detail.html<br>• cart.html<br>• theme-switcher.js<br>• login.html<br>• register.html | 🔴 Cao |

**Checkpoint Tuần 2:**
- ✅ Cart hoạt động đầy đủ (Session + DB)
- ✅ Checkout flow cơ bản hoàn thành
- ✅ Email notification gửi được
- ✅ Theme switching hoạt động
- ✅ CI/CD pipeline deploy được lên staging

---

### TUẦN 3: ADMIN FEATURES & POLISH (Ngày 15-21)

| Người | Vai Trò | Tasks | Deliverables | Ưu Tiên |
|-------|---------|-------|--------------|---------|
| **Người 1** | Team Leader/DevOps | • Performance testing và optimization<br>• Setup monitoring (logs, errors)<br>• Database backup strategy<br>• Security audit<br>• Code review toàn bộ dự án | • Performance report<br>• Monitoring setup<br>• Backup scripts<br>• Security checklist | 🟡 Trung bình |
| **Người 2** | Backend Core | • Optimize Search queries<br>• Implement caching cho products<br>• Add product view count tracking<br>• Implement Wishlist feature<br>• Performance tuning | • Cached queries<br>• View tracking<br>• WishlistService<br>• Performance improvements | 🟡 Trung bình |
| **Người 3** | Backend Logic/Admin | • Implement Traffic Monitoring Service<br>• Tạo ActivityLogService<br>• Implement Admin Dashboard APIs<br>• Tạo Statistics Service (User stats)<br>• Implement Traffic analytics queries<br>• Integrate Chart.js data endpoints | • ActivityLogService<br>• AdminDashboardController<br>• StatisticsService<br>• Traffic analytics APIs<br>• Chart data endpoints | 🔴 Cao |
| **Người 4** | Frontend/Thymeleaf | • Convert trang Checkout<br>• Convert trang Order Confirmation<br>• Tạo Admin Dashboard UI<br>• Integrate Chart.js cho statistics<br>• Convert trang User Profile<br>• Polish UI/UX toàn bộ site | • checkout.html<br>• order-confirmation.html<br>• admin-dashboard.html<br>• Charts integration<br>• profile.html | 🔴 Cao |

**Checkpoint Tuần 3:**
- ✅ Admin Dashboard hiển thị đầy đủ statistics
- ✅ Traffic monitoring hoạt động
- ✅ Charts hiển thị đúng dữ liệu
- ✅ Toàn bộ pages đã convert sang Thymeleaf
- ✅ Theme switching hoạt động trên tất cả pages

---

### TUẦN 4: TESTING & DEPLOYMENT (Ngày 22-28)

| Người | Vai Trò | Tasks | Deliverables | Ưu Tiên |
|-------|---------|-------|--------------|---------|
| **Người 1** | Team Leader/DevOps | • Production deployment<br>• Domain setup và SSL<br>• Final security checks<br>• Backup và rollback plan<br>• Documentation finalization<br>• Team presentation preparation | • Production deployment<br>• SSL certificate<br>• Deployment docs<br>• Presentation slides | 🔴 Cao |
| **Người 2** | Backend Core | • Integration testing<br>• Fix bugs từ testing<br>• API documentation<br>• Performance optimization<br>• Code cleanup | • Integration tests<br>• API docs<br>• Bug fixes<br>• Clean code | 🔴 Cao |
| **Người 3** | Backend Logic/Admin | • End-to-end testing (Checkout flow)<br>• Email testing với real SMTP<br>• Admin features testing<br>• Fix bugs từ testing<br>• Data validation improvements | • E2E tests<br>• Email testing report<br>• Bug fixes<br>• Validation rules | 🔴 Cao |
| **Người 4** | Frontend/Thymeleaf | • Cross-browser testing<br>• Responsive design fixes<br>• Accessibility improvements<br>• UI/UX polish<br>• User guide creation | • Browser compatibility<br>• Mobile responsive<br>• UI polish<br>• User guide | 🔴 Cao |

**Checkpoint Tuần 4:**
- ✅ Toàn bộ features hoạt động ổn định
- ✅ Application deployed lên production
- ✅ Không có critical bugs
- ✅ Documentation hoàn chỉnh
- ✅ Ready for demo/presentation

---

## 🎯 TRAFFIC MONITORING - ĐỀ XUẤT TRIỂN KHAI

### Phương Án Được Chọn: **Database Logging (MVP)**

| Tiêu Chí | Database Logging | Third-party (GA4/Mixpanel) |
|----------|------------------|----------------------------|
| **Độ phức tạp** | 🟢 Thấp | 🔴 Cao |
| **Thời gian triển khai** | 🟢 2-3 ngày | 🟡 5-7 ngày |
| **Chi phí** | 🟢 $0 | 🔴 $0-$200/tháng |
| **Phù hợp deadline** | 🟢 Có | 🔴 Không |
| **Tùy chỉnh** | 🟢 Cao | 🟡 Trung bình |
| **Performance** | 🟡 Tốt (< 10k req/day) | 🟢 Rất tốt |
| **Ownership** | 🟢 100% | 🔴 Phụ thuộc bên thứ 3 |

### Implementation Plan (Tuần 2-3)

#### Week 2: Basic Logging
```java
// ActivityLogService.java
public void logActivity(ActivityType type, Long userId, String sessionId, 
                       String ipAddress, String pageUrl) {
    UserActivityLog log = new UserActivityLog();
    log.setActivityType(type);
    log.setUserId(userId);
    log.setSessionId(sessionId);
    log.setIpAddress(ipAddress);
    log.setPageUrl(pageUrl);
    log.setCreatedAt(LocalDateTime.now());
    activityLogRepository.save(log);
}
```

#### Week 3: Admin Dashboard
```java
// StatisticsService.java
public TrafficStats getTrafficStats(LocalDate startDate, LocalDate endDate) {
    return TrafficStats.builder()
        .totalPageViews(getTotalPageViews(startDate, endDate))
        .uniqueVisitors(getUniqueVisitors(startDate, endDate))
        .topPages(getTopPages(startDate, endDate))
        .peakHours(getPeakHours(startDate, endDate))
        .build();
}
```

### Metrics to Track

| Metric | Description | Query Complexity |
|--------|-------------|------------------|
| **Total Page Views** | Tổng số lượt xem trang | 🟢 Đơn giản |
| **Unique Visitors** | Số người dùng unique (by session_id) | 🟢 Đơn giản |
| **Login Count** | Số lần đăng nhập | 🟢 Đơn giản |
| **Product Views** | Sản phẩm được xem nhiều nhất | 🟢 Đơn giản |
| **Peak Hours** | Giờ cao điểm truy cập | 🟡 Trung bình |
| **User Journey** | Hành trình người dùng | 🔴 Phức tạp |

---

## 📋 DAILY STANDUP TEMPLATE

```
🗓️ Date: [DD/MM/YYYY]

👤 Người 1 (DevOps):
✅ Completed: 
🔄 In Progress: 
🚧 Blockers: 

👤 Người 2 (Backend Core):
✅ Completed: 
🔄 In Progress: 
🚧 Blockers: 

👤 Người 3 (Backend Logic):
✅ Completed: 
🔄 In Progress: 
🚧 Blockers: 

👤 Người 4 (Frontend):
✅ Completed: 
🔄 In Progress: 
🚧 Blockers: 
```

---

## 🚀 DEPLOYMENT CHECKLIST

### Pre-Deployment
- [ ] All tests passing
- [ ] Database migrations tested
- [ ] Environment variables configured
- [ ] SSL certificate ready
- [ ] Backup strategy in place

### Deployment
- [ ] Build Docker image
- [ ] Push to DockerHub
- [ ] Deploy to Render/Railway
- [ ] Run database migrations
- [ ] Verify application health

### Post-Deployment
- [ ] Smoke testing
- [ ] Monitor logs for errors
- [ ] Test critical flows (Search, Cart, Checkout)
- [ ] Verify email sending
- [ ] Check admin dashboard

---

## 📞 COMMUNICATION CHANNELS

- **Daily Standup:** 9:00 AM (15 phút)
- **Code Review:** Mỗi Pull Request
- **Weekly Review:** Cuối mỗi tuần (1 giờ)
- **Emergency:** Slack/Discord channel

---

## 🎓 LEARNING RESOURCES

### For Backend Developers
- Spring Boot Documentation
- Spring Data JPA Best Practices
- Docker for Java Developers

### For Frontend Developer
- Thymeleaf Documentation
- Bootstrap 5 Components
- JavaScript ES6+ Features

### For DevOps
- GitHub Actions Documentation
- Docker Compose Best Practices
- Render/Railway Deployment Guides

---

## ⚠️ RISK MANAGEMENT

| Risk | Impact | Mitigation |
|------|--------|------------|
| **Deadline pressure** | 🔴 Cao | Prioritize MVP features, cut nice-to-have |
| **Integration issues** | 🟡 Trung bình | Daily integration, early testing |
| **Performance problems** | 🟡 Trung bình | Load testing in Week 3, caching strategy |
| **Deployment failures** | 🔴 Cao | Staging environment, rollback plan |
| **Team member unavailable** | 🟡 Trung bình | Cross-training, documentation |

---

## 🏆 SUCCESS METRICS

- ✅ All 10 requirements completed
- ✅ 0 critical bugs in production
- ✅ < 2s page load time
- ✅ 100% Thymeleaf conversion
- ✅ CI/CD pipeline success rate > 95%
- ✅ Admin dashboard fully functional
- ✅ Email delivery rate > 98%
- ✅ Mobile responsive on all pages

---

**Generated:** January 2026  
**Team Size:** 4 members  
**Duration:** 4 weeks  
**Deadline:** End of month
