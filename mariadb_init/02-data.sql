-- ============================================
-- SAMPLE DATA FOR GROCERY STORE (FIXED)
-- ============================================

USE java5_asm;

-- ============================================
-- 1. USERS
-- ============================================
INSERT IGNORE INTO users (username, email, password, full_name, phone, registered_date, role, provider) VALUES
('admin', 'admin@grocerystore.com', '$2a$10$547jHD5iIaAp/7g1OKx3rO4cRul78RDFuaeeZT60rHCvyfnLSB2WO', 'Admin User', '0123456789', '2022-01-01', 'ADMIN', 'local'),
('imrankhan', 'imran@example.com', '$2a$10$547jHD5iIaAp/7g1OKx3rO4cRul78RDFuaeeZT60rHCvyfnLSB2WO', 'Imran Khan', '0987654321', '2022-05-17', 'USER', 'local'),
('johnsmith', 'john@example.com', '$2a$10$547jHD5iIaAp/7g1OKx3rO4cRul78RDFuaeeZT60rHCvyfnLSB2WO', 'John Smith', '0912345678', '2023-03-10', 'USER', 'local'),
('maryjane', 'mary@example.com', '$2a$10$547jHD5iIaAp/7g1OKx3rO4cRul78RDFuaeeZT60rHCvyfnLSB2WO', 'Mary Jane', '0923456789', '2023-06-20', 'USER', 'local');
-- Password for all: password123

-- ============================================
-- 2. ADDRESSES
-- ============================================
INSERT IGNORE INTO addresses (user_id, recipient_name, phone, address_line1, city, state, postal_code, is_default) VALUES
(2, 'Imran Khan', '0987654321', '123 Main Street, Apt 4B', 'Ho Chi Minh', 'District 1', '700000', TRUE),
(2, 'Imran Khan', '0987654321', '456 Second Ave', 'Hanoi', 'Ba Dinh', '100000', FALSE),
(3, 'John Smith', '0912345678', '789 Third Road', 'Da Nang', 'Hai Chau', '550000', TRUE),
(4, 'Mary Jane', '0923456789', '321 Fourth Street', 'Ho Chi Minh', 'District 3', '700000', TRUE);

-- ============================================
-- 3. CATEGORIES
-- ============================================
INSERT IGNORE INTO categories (name, slug, description, display_order, parent_id) VALUES
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
INSERT IGNORE INTO brands (name, slug, description) VALUES
('Lavazza', 'lavazza', 'Italian coffee brand'),
('welikecoffee', 'welikecoffee', 'Premium coffee brand'),
('Starbucks', 'starbucks', 'Global coffee chain'),
('Nescafe', 'nescafe', 'Instant coffee brand'),
('Trung Nguyen', 'trung-nguyen', 'Vietnamese coffee brand');

-- ============================================
-- 5. PRODUCTS
-- ============================================
INSERT IGNORE INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured) VALUES
-- Coffee Beans
('Coffee Beans - Espresso Arabica and Robusta Beans', 'coffee-beans-espresso-arabica-robusta', 
'Premium blend of Arabica and Robusta coffee beans, perfect for espresso lovers. Rich, full-bodied flavor with notes of chocolate and caramel.', 
'Premium espresso blend with rich flavor', 
47.00, NULL, 7, 1, 100, 'CB-ESP-001', '/assets/img/product/item-1.png', TRUE),

('Lavazza Coffee Blends - Try the Italian Espresso', 'lavazza-coffee-blends-italian-espresso',
'Authentic Italian espresso blend from Lavazza. Medium roast with balanced acidity and smooth finish.',
'Authentic Italian espresso blend',
53.00, 49.00, 7, 1, 80, 'CB-LAV-002', '/assets/img/product/item-2.png', TRUE),

('Lavazza - Caffè Espresso Black Tin - Ground coffee', 'lavazza-caffe-espresso-black-tin',
'Classic Lavazza espresso in convenient ground form. Intense flavor with a velvety crema.',
'Classic ground espresso coffee',
99.99, NULL, 8, 1, 50, 'GC-LAV-003', '/assets/img/product/item-3.png', TRUE),

('Starbucks Pike Place Roast', 'starbucks-pike-place-roast',
'Smooth and balanced medium roast coffee. Perfect for everyday brewing.',
'Smooth medium roast coffee',
32.00, 28.00, 7, 3, 120, 'CB-STA-004', '/assets/img/product/item-4.png', FALSE),

('Trung Nguyen Creative 3', 'trung-nguyen-creative-3',
'Vietnamese premium coffee blend. Bold and aromatic with unique flavor profile.',
'Vietnamese premium blend',
45.00, NULL, 7, 5, 90, 'CB-TRN-005', '/assets/img/product/item-5.png', TRUE),

('Nescafe Gold Instant Coffee', 'nescafe-gold-instant-coffee',
'Premium instant coffee with rich aroma. Quick and convenient.',
'Premium instant coffee',
24.00, NULL, 9, 4, 200, 'IC-NES-006', '/assets/img/product/item-6.png', FALSE),

('Lavazza Qualità Rossa', 'lavazza-qualita-rossa',
'Medium roast blend with chocolate notes. Perfect for moka pot.',
'Medium roast with chocolate notes',
38.00, 35.00, 8, 1, 75, 'GC-LAV-007', '/assets/img/product/item-7.png', FALSE),

('Starbucks French Roast', 'starbucks-french-roast',
'Dark roast with smoky flavor. Intense and bold.',
'Dark roast, intense flavor',
35.00, NULL, 7, 3, 60, 'CB-STA-008', '/assets/img/product/item-8.png', FALSE),

('Trung Nguyen Gourmet Blend', 'trung-nguyen-gourmet-blend',
'Premium Vietnamese coffee with unique processing method.',
'Premium Vietnamese gourmet',
52.00, NULL, 7, 5, 70, 'CB-TRN-009', '/assets/img/product/item-1.png', TRUE),

('Nescafe Classic Instant', 'nescafe-classic-instant',
'Classic instant coffee for quick preparation.',
'Classic instant coffee',
18.00, NULL, 9, 4, 250, 'IC-NES-010', '/assets/img/product/item-2.png', FALSE);

-- ============================================
-- 6. REVIEWS
-- ============================================
INSERT IGNORE INTO reviews (product_id, user_id, rating, title, comment, is_verified_purchase) VALUES
(1, 2, 5, 'Excellent coffee!', 'Best espresso beans I have ever tried. Rich flavor and perfect crema.', TRUE),
(1, 3, 4, 'Very good', 'Great quality beans, slightly expensive but worth it.', TRUE),
(1, 4, 3, 'Good but not great', 'Decent coffee but I expected more for the price.', FALSE),

(2, 2, 4, 'Authentic Italian taste', 'Really captures the Italian espresso experience.', TRUE),
(2, 4, 5, 'Love it!', 'My daily coffee now. Cannot start the day without it.', TRUE),

(3, 3, 5, 'Perfect!', 'Convenient ground coffee with amazing taste.', TRUE),

(4, 2, 4, 'Smooth and balanced', 'Great for everyday drinking.', TRUE),

(5, 3, 5, 'Unique Vietnamese flavor', 'Bold and aromatic, love the unique taste.', TRUE);

-- ============================================
-- 7. WISHLISTS
-- ============================================
INSERT IGNORE INTO wishlists (user_id, product_id) VALUES
(2, 3),
(2, 5),
(2, 8),
(3, 1),
(3, 2),
(4, 1),
(4, 4);

-- ============================================
-- 8. CARTS (Sample)
-- ============================================
INSERT IGNORE INTO carts (user_id) VALUES
(2),
(3),
(4);

-- ============================================
-- 9. CART ITEMS (Sample)
-- ============================================
INSERT IGNORE INTO cart_items (cart_id, product_id, quantity, price) VALUES
(1, 1, 2, 47.00),  -- User 2: 2x Coffee Beans
(1, 2, 1, 49.00),  -- User 2: 1x Lavazza Blend

(2, 3, 1, 99.99),  -- User 3: 1x Ground Coffee
(2, 4, 3, 28.00),  -- User 3: 3x Pike Place

(3, 1, 1, 47.00);  -- User 4: 1x Coffee Beans

-- ============================================
-- 10. ORDERS (Sample)
-- ============================================
INSERT IGNORE INTO orders (order_number, user_id, shipping_address_id, payment_method, subtotal, shipping_fee, tax, total_amount, status, payment_status, ordered_at) VALUES
('ORD-20260115-001', 2, 1, 'VNPAY', 143.00, 5.00, 14.30, 162.30, 'DELIVERED', 'PAID', '2026-01-15 10:30:00'),
('ORD-20260116-002', 3, 3, 'MOMO', 183.99, 5.00, 18.40, 207.39, 'SHIPPED', 'PAID', '2026-01-16 14:20:00'),
('ORD-20260117-003', 2, 1, 'COD', 94.00, 5.00, 9.40, 108.40, 'PROCESSING', 'PENDING', '2026-01-17 09:15:00');

-- ============================================
-- 11. ORDER ITEMS
-- ============================================
INSERT IGNORE INTO order_items (order_id, product_id, product_name, quantity, unit_price, subtotal) VALUES
-- Order 1
(1, 1, 'Coffee Beans - Espresso Arabica and Robusta Beans', 2, 47.00, 94.00),
(1, 2, 'Lavazza Coffee Blends - Try the Italian Espresso', 1, 49.00, 49.00),

-- Order 2
(2, 3, 'Lavazza - Caffè Espresso Black Tin - Ground coffee', 1, 99.99, 99.99),
(2, 4, 'Starbucks Pike Place Roast', 3, 28.00, 84.00),

-- Order 3
(3, 1, 'Coffee Beans - Espresso Arabica and Robusta Beans', 2, 47.00, 94.00);

-- ============================================
-- COMPLETED - FIXED VERSION
-- ============================================
-- Summary:
-- ✅ Removed: payment_methods inserts (table doesn't exist)
-- ✅ Removed: product_images inserts (merged into products)
-- ✅ Removed: product_variants inserts (table doesn't exist)
-- ✅ Removed: banners inserts (table doesn't exist)
-- ✅ Fixed: cart_items (removed variant_id)
-- ✅ Fixed: order_items (removed variant_id, variant_name)
-- ✅ Fixed: orders (removed payment_method_id, added payment_method)
-- ✅ Added: image_url to products
-- ✅ Added: provider to users
-- ✅ Added: INSERT IGNORE to prevent duplicates
-- ============================================
