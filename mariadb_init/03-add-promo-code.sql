-- Thêm cột promo_code vào bảng carts
ALTER TABLE carts ADD COLUMN IF NOT EXISTS promo_code VARCHAR(50) NULL COMMENT 'Mã giảm giá đã áp dụng';

-- Thêm index cho promo_code để tối ưu truy vấn
CREATE INDEX IF NOT EXISTS idx_cart_promo_code ON carts(promo_code);
