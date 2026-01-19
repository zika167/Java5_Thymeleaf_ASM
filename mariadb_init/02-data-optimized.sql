-- ============================================
-- OPTIMIZED SAMPLE DATA FOR GROCERY STORE
-- ============================================

USE java5_asm;

-- ============================================
-- 1. USERS
-- ============================================
INSERT INTO users (username, email, password, full_name, phone, registered_date, role, last_login_at, login_count) VALUES
('admin', 'admin@grocerystore.com', '$2a$10$1/JeMi6P0zukrKEYUdqNFudxFiMDyLjiEK8.rQsY8D362jamJwRwC', 'Admin User', '0123456789', '2022-01-01', 'ADMIN', '2026-01-19 10:00:00', 150),
('imrankhan', 'imran@example.com', '$2a$10$1/JeMi6P0zukrKEYUdqNFudxFiMDyLjiEK8.rQsY8D362jamJwRwC', 'Imran Khan', '0987654321', '2022-05-17', 'USER', '2026-01-18 15:30:00', 45),
('johnsmith', 'john@example.com', '$2a$10$1/JeMi6P0zukrKEYUdqNFudxFiMDyLjiEK8.rQsY8D362jamJwRwC', 'John Smith', '0912345678', '2023-03-10', 'USER', '2026-01-17 09:20:00', 28),
('maryjane', 'mary@example.com', '$2a$10$1/JeMi6P0zukrKEYUdqNFudxFiMDyLjiEK8.rQsY8D362jamJwRwC', 'Mary Jane', '0923456789', '2023-06-20', 'USER', '2026-01-16 14:45:00', 32);
-- Password for all: password123

-- ============================================
-- 2. ADDRESSES
-- ============================================
INSERT INTO addresses (user_id, recipient_name, phone, address_line1, city, state, postal_code, is_default) VALUES
(2, 'Imran Khan', '0987654321', '123 Main Street, Apt 4B', 'Ho Chi Minh', 'District 1', '700000', TRUE),
(2, 'Imran Khan', '0987654321', '456 Second Ave', 'Hanoi', 'Ba Dinh', '100000', FALSE),
(3, 'John Smith', '0912345678', '789 Third Road', 'Da Nang', 'Hai Chau', '550000', TRUE),
(4, 'Mary Jane', '0923456789', '321 Fourth Street', 'Ho Chi Minh', 'District 3', '700000', TRUE);

-- ============================================
-- 3. CATEGORIES
-- ============================================
INSERT INTO categories (name, slug, description, display_order, parent_id) VALUES
-- Parent categories
('Departments', 'departments', 'All departments', 1, NULL),
('Grocery', 'grocery', 'Grocery items', 2, NULL),
('Beauty', 'beauty', 'Beauty products', 3, NULL),

-- Sub-categories under Departments
('Coffee', 'coffee', 'Coffee products', 1, 1),
('Electronics', 'electronics', 'Electronic devices', 2, 1),
('Clothing', 'clothing', 'Clothing and accessories', 3, 1),

-- Sub-categories under Coffee
('Coffee Beans', 'coffee-beans', 'Whole coffee beans', 1, 4),
('Ground Coffee', 'ground-coffee', 'Pre-ground coffee', 2, 4),
('Instant Coffee', 'instant-coffee', 'Instant coffee', 3, 4);

-- ============================================
-- 4. BRANDS
-- ============================================
INSERT INTO brands (name, slug, description) VALUES
('Lavazza', 'lavazza', 'Italian coffee brand'),
('welikecoffee', 'welikecoffee', 'Premium coffee brand'),
('Starbucks', 'starbucks', 'Global coffee chain'),
('Nescafe', 'nescafe', 'Instant coffee brand'),
('Trung Nguyen', 'trung-nguyen', 'Vietnamese coffee brand');

-- ============================================
-- 5. PRODUCTS (with image_url merged)
-- ============================================
INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, low_stock_threshold, is_out_of_stock, image_url, sku, weight, is_featured, view_count) VALUES
-- Coffee Beans
('Coffee Beans - Espresso Arabica and Robusta Beans', 'coffee-beans-espresso-arabica-robusta', 
'Premium blend of Arabica and Robusta coffee beans, perfect for espresso lovers. Rich, full-bodied flavor with notes of chocolate and caramel.', 
'Premium espresso blend with rich flavor', 
47.00, NULL, 7, 1, 100, 10, FALSE, '/assets/img/product/item-1.png', 'CB-ESP-001', '500g', TRUE, 245),

('Lavazza Coffee Blends - Try the Italian Espresso', 'lavazza-coffee-blends-italian-espresso',
'Authentic Italian espresso blend from Lavazza. Medium roast with balanced acidity and smooth finish.',
'Authentic Italian espresso blend',
53.00, 49.00, 7, 1, 80, 10, FALSE, '/assets/img/product/item-2.png', 'CB-LAV-002', '500g', TRUE, 189),

('Lavazza - Caffè Espresso Black Tin - Ground coffee', 'lavazza-caffe-espresso-black-tin',
'Classic Lavazza espresso in convenient ground form. Intense flavor with a velvety crema.',
'Classic ground espresso coffee',
99.99, NULL, 8, 1, 50, 10, FALSE, '/assets/img/product/item-3.png', 'GC-LAV-003', '250g', TRUE, 156),

('Starbucks Pike Place Roast', 'starbucks-pike-place-roast',
'Smooth and balanced medium roast coffee. Perfect for everyday brewing.',
'Smooth medium roast coffee',
32.00, 28.00, 7, 3, 120, 15, FALSE, '/assets/img/product/item-4.png', 'CB-STA-004', '340g', FALSE, 98),

('Trung Nguyen Creative 3', 'trung-nguyen-creative-3',
'Vietnamese premium coffee blend. Bold and aromatic with unique flavor profile.',
'Vietnamese premium blend',
45.00, NULL, 7, 5, 90, 10, FALSE, '/assets/img/product/item-5.png', 'CB-TRN-005', '500g', TRUE, 134),

('Nescafe Gold Instant Coffee', 'nescafe-gold-instant-coffee',
'Premium instant coffee with rich aroma. Quick and convenient.',
'Premium instant coffee',
24.00, NULL, 9, 4, 200, 20, FALSE, '/assets/img/product/item-6.png', 'IC-NES-006', '200g', FALSE, 76),

('Lavazza Qualità Rossa', 'lavazza-qualita-rossa',
'Medium roast blend with chocolate notes. Perfect for moka pot.',
'Medium roast with chocolate notes',
38.00, 35.00, 8, 1, 75, 10, FALSE, '/assets/img/product/item-7.png', 'GC-LAV-007', '250g', FALSE, 112),

('Starbucks French Roast', 'starbucks-french-roast',
'Dark roast with smoky flavor. Intense and bold.',
'Dark roast, intense flavor',
35.00, NULL, 7, 3, 60, 10, FALSE, '/assets/img/product/item-8.png', 'CB-STA-008', '340g', FALSE, 89),

('Trung Nguyen Gourmet Blend', 'trung-nguyen-gourmet-blend',
'Premium Vietnamese coffee with unique processing method.',
'Premium Vietnamese gourmet',
52.00, 48.00, 7, 5, 45, 10, FALSE, '/assets/img/product/item-9.png', 'CB-TRN-009', '500g', TRUE, 167),

('Nescafe Classic Instant', 'nescafe-classic-instant',
'Classic instant coffee for everyday enjoyment.',
'Classic instant coffee',
18.00, NULL, 9, 4, 250, 25, FALSE, '/assets/img/product/item-10.png', 'IC-NES-010', '100g', FALSE, 54);

-- ============================================
-- 6. REVIEWS
-- ============================================
INSERT INTO reviews (product_id, user_id, rating, title, comment, is_verified_purchase) VALUES
(1, 2, 5, 'Excellent coffee!', 'Best espresso beans I have ever tried. Rich flavor and perfect crema.', TRUE),
(1, 3, 4, 'Very good', 'Great quality beans, slightly expensive but worth it.', TRUE),
(1, 4, 3, 'Good but not great', 'Decent coffee but I expected more for the price.', FALSE),

(2, 2, 4, 'Authentic Italian taste', 'Really captures the Italian espresso experience.', TRUE),
(2, 4, 5, 'Love it!', 'My daily coffee now. Cannot start the day without it.', TRUE),

(3, 3, 5, 'Perfect!', 'Convenient ground coffee with amazing taste.', TRUE),

(4, 2, 4, 'Smooth and balanced', 'Great for everyday drinking.', TRUE),

(5, 3, 5, 'Unique Vietnamese flavor', 'Bold and aromatic, love the unique taste.', TRUE),

(9, 2, 5, 'Best Vietnamese coffee', 'Absolutely love this gourmet blend!', TRUE),
(9, 4, 4, 'Very good', 'Strong and flavorful, highly recommend.', TRUE);

-- ============================================
-- 7. WISHLISTS
-- ============================================
INSERT INTO wishlists (user_id, product_id) VALUES
(2, 3),
(2, 5),
(2, 8),
(3, 1),
(3, 2),
(4, 1),
(4, 4),
(4, 9);

-- ============================================
-- 8. CARTS (Sample)
-- ============================================
INSERT INTO carts (user_id) VALUES
(2),
(3),
(4);

-- ============================================
-- 9. CART ITEMS (Sample - no variants)
-- ============================================
INSERT INTO cart_items (cart_id, product_id, quantity, price) VALUES
(1, 1, 2, 47.00),  -- User 2: 2x Coffee Beans
(1, 2, 1, 49.00),  -- User 2: 1x Lavazza Blend

(2, 3, 1, 99.99),  -- User 3: 1x Ground Coffee
(2, 4, 3, 28.00),  -- User 3: 3x Pike Place

(3, 1, 1, 47.00),  -- User 4: 1x Coffee Beans
(3, 9, 2, 48.00);  -- User 4: 2x Trung Nguyen Gourmet

-- ============================================
-- 10. ORDERS (Sample - using payment gateway)
-- ============================================
INSERT INTO orders (order_number, user_id, shipping_address_id, payment_method, payment_transaction_id, payment_status, subtotal, shipping_fee, tax, total_amount, status, ordered_at, confirmed_at, shipped_at) VALUES
('ORD-20260115-001', 2, 1, 'VNPAY', 'VNPAY-TXN-123456', 'PAID', 143.00, 5.00, 14.30, 162.30, 'DELIVERED', '2026-01-15 10:30:00', '2026-01-15 11:00:00', '2026-01-16 09:00:00'),
('ORD-20260116-002', 3, 3, 'COD', NULL, 'PENDING', 183.99, 5.00, 18.40, 207.39, 'SHIPPED', '2026-01-16 14:20:00', '2026-01-16 15:00:00', '2026-01-17 10:00:00'),
('ORD-20260117-003', 2, 1, 'MOMO', 'MOMO-TXN-789012', 'PAID', 94.00, 5.00, 9.40, 108.40, 'PROCESSING', '2026-01-17 09:15:00', '2026-01-17 10:00:00', NULL),
('ORD-20260118-004', 4, 4, 'VNPAY', 'VNPAY-TXN-345678', 'PAID', 143.00, 5.00, 14.30, 162.30, 'CONFIRMED', '2026-01-18 16:45:00', '2026-01-18 17:00:00', NULL);

-- ============================================
-- 11. ORDER ITEMS (no variants)
-- ============================================
INSERT INTO order_items (order_id, product_id, product_name, quantity, unit_price, subtotal) VALUES
-- Order 1
(1, 1, 'Coffee Beans - Espresso Arabica and Robusta Beans', 2, 47.00, 94.00),
(1, 2, 'Lavazza Coffee Blends - Try the Italian Espresso', 1, 49.00, 49.00),

-- Order 2
(2, 3, 'Lavazza - Caffè Espresso Black Tin - Ground coffee', 1, 99.99, 99.99),
(2, 4, 'Starbucks Pike Place Roast', 3, 28.00, 84.00),

-- Order 3
(3, 1, 'Coffee Beans - Espresso Arabica and Robusta Beans', 2, 47.00, 94.00),

-- Order 4
(4, 9, 'Trung Nguyen Gourmet Blend', 2, 48.00, 96.00),
(4, 5, 'Trung Nguyen Creative 3', 1, 47.00, 47.00);

-- ============================================
-- 12. USER ACTIVITY LOGS (Sample)
-- ============================================
INSERT INTO user_activity_logs (user_id, session_id, activity_type, ip_address, page_url, created_at) VALUES
-- Admin activities
(1, 'admin-session-001', 'LOGIN', '192.168.1.100', '/admin/login', '2026-01-19 10:00:00'),
(1, 'admin-session-001', 'PAGE_VIEW', '192.168.1.100', '/admin/dashboard', '2026-01-19 10:01:00'),
(1, 'admin-session-001', 'PAGE_VIEW', '192.168.1.100', '/admin/users', '2026-01-19 10:05:00'),

-- User 2 activities
(2, 'user2-session-001', 'LOGIN', '192.168.1.101', '/login', '2026-01-18 15:30:00'),
(2, 'user2-session-001', 'PAGE_VIEW', '192.168.1.101', '/', '2026-01-18 15:31:00'),
(2, 'user2-session-001', 'PRODUCT_VIEW', '192.168.1.101', '/product/1', '2026-01-18 15:35:00'),
(2, 'user2-session-001', 'ADD_TO_CART', '192.168.1.101', '/cart/add', '2026-01-18 15:36:00'),
(2, 'user2-session-001', 'SEARCH', '192.168.1.101', '/search?q=coffee', '2026-01-18 15:40:00'),

-- User 3 activities
(3, 'user3-session-001', 'LOGIN', '192.168.1.102', '/login', '2026-01-17 09:20:00'),
(3, 'user3-session-001', 'PAGE_VIEW', '192.168.1.102', '/', '2026-01-17 09:21:00'),
(3, 'user3-session-001', 'PRODUCT_VIEW', '192.168.1.102', '/product/3', '2026-01-17 09:25:00'),
(3, 'user3-session-001', 'CHECKOUT', '192.168.1.102', '/checkout', '2026-01-17 09:30:00'),

-- User 4 activities
(4, 'user4-session-001', 'LOGIN', '192.168.1.103', '/login', '2026-01-16 14:45:00'),
(4, 'user4-session-001', 'PAGE_VIEW', '192.168.1.103', '/', '2026-01-16 14:46:00'),
(4, 'user4-session-001', 'SEARCH', '192.168.1.103', '/search?q=vietnamese', '2026-01-16 14:50:00'),
(4, 'user4-session-001', 'PRODUCT_VIEW', '192.168.1.103', '/product/9', '2026-01-16 14:52:00');

-- ============================================
-- COMPLETED - OPTIMIZED DATA
-- ============================================
-- Total records:
-- - 4 users (1 admin, 3 regular users)
-- - 4 addresses
-- - 9 categories (with hierarchy)
-- - 5 brands
-- - 10 products (with images merged)
-- - 10 reviews
-- - 8 wishlist items
-- - 3 carts with 6 cart items
-- - 4 orders with 7 order items
-- - 16 activity logs
-- ============================================
