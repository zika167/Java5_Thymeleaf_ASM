-- Thêm cột promo_code và discount_amount vào bảng orders
ALTER TABLE orders ADD COLUMN IF NOT EXISTS promo_code VARCHAR(50) NULL COMMENT 'Mã giảm giá đã áp dụng';
ALTER TABLE orders ADD COLUMN IF NOT EXISTS discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Số tiền giảm giá';

-- Thêm index cho promo_code để tối ưu truy vấn
CREATE INDEX IF NOT EXISTS idx_order_promo_code ON orders(promo_code);
