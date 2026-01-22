-- ============================================
-- MIGRATION: Make password nullable for OAuth2 users
-- ============================================
-- Mục đích: Cho phép user đăng nhập qua OAuth2 (Google, Facebook)
-- không cần password trong database
-- ============================================

USE java5_asm;

-- Thay đổi cột password từ NOT NULL thành NULL
ALTER TABLE users MODIFY COLUMN password VARCHAR(255) NULL;

-- Verify thay đổi
DESCRIBE users;