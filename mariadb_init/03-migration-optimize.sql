-- ============================================
-- DATABASE OPTIMIZATION MIGRATION
-- ============================================
-- Purpose: 
-- 1. Add missing columns for traffic monitoring and user preferences
-- 2. Remove unnecessary tables (payment_methods, product_variants, banners, product_images)
-- 3. Merge product_images into products table
-- ============================================

USE java5_asm;

-- ============================================
-- STEP 1: BACKUP DATA BEFORE MIGRATION
-- ============================================

-- Backup product images to merge into products
CREATE TEMPORARY TABLE temp_product_images AS
SELECT product_id, image_url 
FROM product_images 
WHERE is_primary = TRUE;

-- ============================================
-- STEP 2: DROP FOREIGN KEY CONSTRAINTS
-- ============================================

-- Drop foreign keys referencing payment_methods
ALTER TABLE orders DROP FOREIGN KEY IF EXISTS orders_ibfk_3;

-- Drop foreign keys referencing product_variants
ALTER TABLE cart_items DROP FOREIGN KEY IF EXISTS cart_items_ibfk_3;
ALTER TABLE order_items DROP FOREIGN KEY IF EXISTS order_items_ibfk_3;

-- ============================================
-- STEP 3: ADD NEW COLUMNS TO EXISTING TABLES
-- ============================================

-- Add traffic monitoring columns to users table
ALTER TABLE users
ADD COLUMN last_login_at TIMESTAMP NULL AFTER updated_at,
ADD COLUMN login_count INT DEFAULT 0 AFTER last_login_at;

-- Add inventory management columns to products table
ALTER TABLE products
ADD COLUMN low_stock_threshold INT DEFAULT 10 AFTER stock_quantity,
ADD COLUMN is_out_of_stock BOOLEAN DEFAULT FALSE AFTER low_stock_threshold,
ADD COLUMN image_url VARCHAR(255) DEFAULT '/assets/img/products/default.jpg' AFTER is_out_of_stock,
ADD COLUMN search_keywords TEXT AFTER image_url,
ADD COLUMN tags VARCHAR(500) AFTER search_keywords;

-- Add index for better search performance
ALTER TABLE products
ADD FULLTEXT INDEX idx_search (name, description);

-- ============================================
-- STEP 4: MIGRATE PRODUCT IMAGES DATA
-- ============================================

-- Update products with their primary images
UPDATE products p
INNER JOIN temp_product_images t ON p.id = t.product_id
SET p.image_url = t.image_url;

-- ============================================
-- STEP 5: REMOVE VARIANT_ID FROM DEPENDENT TABLES
-- ============================================

-- Remove variant_id from cart_items
ALTER TABLE cart_items
DROP COLUMN variant_id;

-- Remove variant columns from order_items
ALTER TABLE order_items
DROP COLUMN variant_id,
DROP COLUMN variant_name;

-- ============================================
-- STEP 6: REMOVE PAYMENT_METHOD_ID FROM ORDERS
-- ============================================

-- Add payment_method column to store payment gateway type
ALTER TABLE orders
ADD COLUMN payment_method VARCHAR(50) DEFAULT 'COD' AFTER payment_method_id,
ADD COLUMN payment_transaction_id VARCHAR(255) AFTER payment_method,
ADD COLUMN payment_gateway_response TEXT AFTER payment_transaction_id;

-- Migrate existing payment method data (if any)
UPDATE orders o
LEFT JOIN payment_methods pm ON o.payment_method_id = pm.id
SET o.payment_method = COALESCE(pm.card_type, 'COD');

-- Remove payment_method_id column
ALTER TABLE orders
DROP COLUMN payment_method_id;

-- ============================================
-- STEP 7: DROP UNNECESSARY TABLES
-- ============================================

DROP TABLE IF EXISTS product_images;
DROP TABLE IF EXISTS product_variants;
DROP TABLE IF EXISTS payment_methods;
DROP TABLE IF EXISTS banners;

-- ============================================
-- STEP 8: CREATE USER ACTIVITY LOGS TABLE
-- ============================================

CREATE TABLE user_activity_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    session_id VARCHAR(255),
    activity_type ENUM('LOGIN', 'LOGOUT', 'PAGE_VIEW', 'PRODUCT_VIEW', 'SEARCH', 'ADD_TO_CART', 'CHECKOUT') NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    page_url VARCHAR(500),
    metadata JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user (user_id),
    INDEX idx_session (session_id),
    INDEX idx_activity (activity_type),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- STEP 9: ADD INDEXES FOR PERFORMANCE
-- ============================================

-- Add index for out of stock products
ALTER TABLE products
ADD INDEX idx_stock_status (is_out_of_stock, is_active);

-- ============================================
-- STEP 10: CLEANUP TEMPORARY TABLES
-- ============================================

DROP TEMPORARY TABLE IF EXISTS temp_product_images;

-- ============================================
-- MIGRATION COMPLETED
-- ============================================
-- Summary of changes:
-- ✅ Added: last_login_at, login_count to users
-- ✅ Added: low_stock_threshold, is_out_of_stock, image_url, search_keywords, tags to products
-- ✅ Added: payment_method, payment_transaction_id, payment_gateway_response to orders
-- ✅ Created: user_activity_logs table for traffic monitoring
-- ✅ Removed: payment_methods, product_variants, banners, product_images tables
-- ✅ Removed: variant_id from cart_items and order_items
-- ✅ Removed: payment_method_id from orders
-- ============================================
