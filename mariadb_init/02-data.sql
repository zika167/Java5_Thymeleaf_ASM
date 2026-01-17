-- ============================================
-- SAMPLE DATA FOR GROCERY STORE
-- ============================================

USE java5_asm;

-- ============================================
-- 1. USERS
-- ============================================
INSERT INTO users (username, email, password, full_name, phone, registered_date, role) VALUES
('admin', 'admin@grocerystore.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin User', '0123456789', '2022-01-01', 'ADMIN'),
('imrankhan', 'imran@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Imran Khan', '0987654321', '2022-05-17', 'USER'),
('johnsmith', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Smith', '0912345678', '2023-03-10', 'USER'),
('maryjane', 'mary@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Mary Jane', '0923456789', '2023-06-20', 'USER');
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
-- 3. PAYMENT METHODS
-- ============================================
INSERT INTO payment_methods (user_id, card_type, card_number, card_holder_name, expiry_month, expiry_year, cvv, is_default) VALUES
(2, 'VISA', '4532123456789012', 'IMRAN KHAN', 12, 2026, '123', TRUE),
(2, 'MASTERCARD', '5425233430109903', 'IMRAN KHAN', 6, 2027, '456', FALSE),
(3, 'VISA', '4916338506082832', 'JOHN SMITH', 3, 2025, '789', TRUE),
(4, 'AMEX', '371449635398431', 'MARY JANE', 9, 2026, '1234', TRUE);

-- ============================================
-- 4. CATEGORIES
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
-- 5. BRANDS
-- ============================================
INSERT INTO brands (name, slug, description) VALUES
('Lavazza', 'lavazza', 'Italian coffee brand'),
('welikecoffee', 'welikecoffee', 'Premium coffee brand'),
('Starbucks', 'starbucks', 'Global coffee chain'),
('Nescafe', 'nescafe', 'Instant coffee brand'),
('Trung Nguyen', 'trung-nguyen', 'Vietnamese coffee brand');

-- ============================================
-- 6. PRODUCTS
-- ============================================
INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, is_featured) VALUES
-- Coffee Beans
('Coffee Beans - Espresso Arabica and Robusta Beans', 'coffee-beans-espresso-arabica-robusta', 
'Premium blend of Arabica and Robusta coffee beans, perfect for espresso lovers. Rich, full-bodied flavor with notes of chocolate and caramel.', 
'Premium espresso blend with rich flavor', 
47.00, NULL, 7, 1, 100, 'CB-ESP-001', TRUE),

('Lavazza Coffee Blends - Try the Italian Espresso', 'lavazza-coffee-blends-italian-espresso',
'Authentic Italian espresso blend from Lavazza. Medium roast with balanced acidity and smooth finish.',
'Authentic Italian espresso blend',
53.00, 49.00, 7, 1, 80, 'CB-LAV-002', TRUE),

('Lavazza - Caffè Espresso Black Tin - Ground coffee', 'lavazza-caffe-espresso-black-tin',
'Classic Lavazza espresso in convenient ground form. Intense flavor with a velvety crema.',
'Classic ground espresso coffee',
99.99, NULL, 8, 1, 50, 'GC-LAV-003', TRUE),

('Starbucks Pike Place Roast', 'starbucks-pike-place-roast',
'Smooth and balanced medium roast coffee. Perfect for everyday brewing.',
'Smooth medium roast coffee',
32.00, 28.00, 7, 3, 120, 'CB-STA-004', FALSE),

('Trung Nguyen Creative 3', 'trung-nguyen-creative-3',
'Vietnamese premium coffee blend. Bold and aromatic with unique flavor profile.',
'Vietnamese premium blend',
45.00, NULL, 7, 5, 90, 'CB-TRN-005', TRUE),

('Nescafe Gold Instant Coffee', 'nescafe-gold-instant-coffee',
'Premium instant coffee with rich aroma. Quick and convenient.',
'Premium instant coffee',
24.00, NULL, 9, 4, 200, 'IC-NES-006', FALSE),

('Lavazza Qualità Rossa', 'lavazza-qualita-rossa',
'Medium roast blend with chocolate notes. Perfect for moka pot.',
'Medium roast with chocolate notes',
38.00, 35.00, 8, 1, 75, 'GC-LAV-007', FALSE),

('Starbucks French Roast', 'starbucks-french-roast',
'Dark roast with smoky flavor. Intense and bold.',
'Dark roast, intense flavor',
35.00, NULL, 7, 3, 60, 'CB-STA-008', FALSE);

-- ============================================
-- 7. PRODUCT IMAGES
-- ============================================
INSERT INTO product_images (product_id, image_url, is_primary, display_order) VALUES
-- Product 1
(1, '/assets/img/product/item-1.png', TRUE, 1),
(1, '/assets/img/product/item-2.png', FALSE, 2),
(1, '/assets/img/product/item-3.png', FALSE, 3),
(1, '/assets/img/product/item-4.png', FALSE, 4),

-- Product 2
(2, '/assets/img/product/item-2.png', TRUE, 1),
(2, '/assets/img/product/item-1.png', FALSE, 2),

-- Product 3
(3, '/assets/img/product/item-3.png', TRUE, 1),
(3, '/assets/img/product/item-4.png', FALSE, 2),

-- Product 4
(4, '/assets/img/product/item-4.png', TRUE, 1),

-- Product 5
(5, '/assets/img/product/item-5.png', TRUE, 1),

-- Product 6
(6, '/assets/img/product/item-6.png', TRUE, 1),

-- Product 7
(7, '/assets/img/product/item-7.png', TRUE, 1),

-- Product 8
(8, '/assets/img/product/item-8.png', TRUE, 1);

-- ============================================
-- 8. PRODUCT VARIANTS (Weight/Size)
-- ============================================
INSERT INTO product_variants (product_id, variant_name, variant_value, price_adjustment, stock_quantity, sku) VALUES
-- Product 1 variants
(1, 'Weight', '200g', 0.00, 50, 'CB-ESP-001-200G'),
(1, 'Weight', '300g', 5.00, 40, 'CB-ESP-001-300G'),
(1, 'Weight', '400g', 8.00, 30, 'CB-ESP-001-400G'),
(1, 'Weight', '500g', 10.00, 25, 'CB-ESP-001-500G'),
(1, 'Weight', '600g', 15.00, 20, 'CB-ESP-001-600G'),

-- Product 2 variants
(2, 'Weight', '250g', 0.00, 40, 'CB-LAV-002-250G'),
(2, 'Weight', '500g', 10.00, 30, 'CB-LAV-002-500G'),
(2, 'Weight', '1kg', 20.00, 20, 'CB-LAV-002-1KG'),

-- Product 3 variants
(3, 'Weight', '250g', 0.00, 25, 'GC-LAV-003-250G'),
(3, 'Weight', '500g', 15.00, 15, 'GC-LAV-003-500G');

-- ============================================
-- 9. REVIEWS
-- ============================================
INSERT INTO reviews (product_id, user_id, rating, title, comment, is_verified_purchase) VALUES
(1, 2, 5, 'Excellent coffee!', 'Best espresso beans I have ever tried. Rich flavor and perfect crema.', TRUE),
(1, 3, 4, 'Very good', 'Great quality beans, slightly expensive but worth it.', TRUE),
(1, 4, 3, 'Good but not great', 'Decent coffee but I expected more for the price.', FALSE),

(2, 2, 4, 'Authentic Italian taste', 'Really captures the Italian espresso experience.', TRUE),
(2, 4, 5, 'Love it!', 'My daily coffee now. Cannot start the day without it.', TRUE),

(3, 3, 5, 'Perfect!', 'Convenient ground coffee with amazing taste.', TRUE),

(4, 2, 4, 'Smooth and balanced', 'Great for everyday drinking.', TRUE),

(5, 3, 5, 'Unique Vietnamese flavor', 'Bold and aromatic, love the unique taste.', TRUE);

-- ============================================
-- 10. WISHLISTS
-- ============================================
INSERT INTO wishlists (user_id, product_id) VALUES
(2, 3),
(2, 5),
(2, 8),
(3, 1),
(3, 2),
(4, 1),
(4, 4);

-- ============================================
-- 11. BANNERS
-- ============================================
INSERT INTO banners (title, image_url, image_mobile_url, link_url, display_order, is_active, start_date, end_date) VALUES
('Summer Sale', '/assets/img/slideshow/item-1.png', '/assets/img/slideshow/item-1-md.png', '/category', 1, TRUE, '2026-01-01', '2026-12-31'),
('New Arrivals', '/assets/img/slideshow/slider-2.png', '/assets/img/slideshow/slider-2.png', '/category', 2, TRUE, '2026-01-01', '2026-12-31'),
('Premium Coffee', '/assets/img/slideshow/slider-3.png', '/assets/img/slideshow/slider-3.png', '/category', 3, TRUE, '2026-01-01', '2026-12-31'),
('Special Offer', '/assets/img/slideshow/slider-4.png', '/assets/img/slideshow/slider-4.png', '/category', 4, TRUE, '2026-01-01', '2026-12-31'),
('Best Sellers', '/assets/img/slideshow/slider-5.png', '/assets/img/slideshow/slider-5.png', '/category', 5, TRUE, '2026-01-01', '2026-12-31');

-- ============================================
-- 12. CARTS (Sample)
-- ============================================
INSERT INTO carts (user_id) VALUES
(2),
(3),
(4);

-- ============================================
-- 13. CART ITEMS (Sample)
-- ============================================
INSERT INTO cart_items (cart_id, product_id, variant_id, quantity, price) VALUES
(1, 1, 4, 2, 47.00),  -- User 2: 2x Coffee Beans 500g
(1, 2, NULL, 1, 49.00), -- User 2: 1x Lavazza Blend

(2, 3, NULL, 1, 99.99), -- User 3: 1x Ground Coffee
(2, 4, NULL, 3, 28.00), -- User 3: 3x Pike Place

(3, 1, 1, 1, 47.00);    -- User 4: 1x Coffee Beans 200g

-- ============================================
-- 14. ORDERS (Sample)
-- ============================================
INSERT INTO orders (order_number, user_id, shipping_address_id, payment_method_id, subtotal, shipping_fee, tax, total_amount, status, payment_status, ordered_at) VALUES
('ORD-20260115-001', 2, 1, 1, 143.00, 5.00, 14.30, 162.30, 'DELIVERED', 'PAID', '2026-01-15 10:30:00'),
('ORD-20260116-002', 3, 3, 3, 183.99, 5.00, 18.40, 207.39, 'SHIPPED', 'PAID', '2026-01-16 14:20:00'),
('ORD-20260117-003', 2, 1, 1, 94.00, 5.00, 9.40, 108.40, 'PROCESSING', 'PAID', '2026-01-17 09:15:00');

-- ============================================
-- 15. ORDER ITEMS
-- ============================================
INSERT INTO order_items (order_id, product_id, variant_id, product_name, variant_name, quantity, unit_price, subtotal) VALUES
-- Order 1
(1, 1, 4, 'Coffee Beans - Espresso Arabica and Robusta Beans', '500g', 2, 47.00, 94.00),
(1, 2, NULL, 'Lavazza Coffee Blends - Try the Italian Espresso', NULL, 1, 49.00, 49.00),

-- Order 2
(2, 3, NULL, 'Lavazza - Caffè Espresso Black Tin - Ground coffee', NULL, 1, 99.99, 99.99),
(2, 4, NULL, 'Starbucks Pike Place Roast', NULL, 3, 28.00, 84.00),

-- Order 3
(3, 1, 1, 'Coffee Beans - Espresso Arabica and Robusta Beans', '200g', 2, 47.00, 94.00);

-- ============================================
-- COMPLETED
-- ============================================
