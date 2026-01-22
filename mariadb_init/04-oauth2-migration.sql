-- ============================================
-- OAuth2 Migration Script
-- Thêm các cột cần thiết cho Google OAuth2 Login
-- ============================================

USE java5_asm;

-- Kiểm tra và thêm cột provider nếu chưa có
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'java5_asm' 
    AND TABLE_NAME = 'users' 
    AND COLUMN_NAME = 'provider'
);

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE users ADD COLUMN provider VARCHAR(20) COMMENT ''OAuth2 provider: google, facebook, local''',
    'SELECT ''Column provider already exists'' AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Kiểm tra và thêm cột provider_id nếu chưa có
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'java5_asm' 
    AND TABLE_NAME = 'users' 
    AND COLUMN_NAME = 'provider_id'
);

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE users ADD COLUMN provider_id VARCHAR(100) COMMENT ''ID from OAuth2 provider''',
    'SELECT ''Column provider_id already exists'' AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Cập nhật password column để cho phép NULL (cho OAuth2 users)
ALTER TABLE users MODIFY COLUMN password VARCHAR(255) NULL COMMENT 'Mật khẩu (đã mã hóa) - nullable cho OAuth2 users';

-- Tạo index cho provider và provider_id để tăng tốc độ query
CREATE INDEX IF NOT EXISTS idx_users_provider ON users(provider);
CREATE INDEX IF NOT EXISTS idx_users_provider_id ON users(provider_id);
CREATE INDEX IF NOT EXISTS idx_users_provider_provider_id ON users(provider, provider_id);

-- Cập nhật existing users để set provider = 'local'
UPDATE users 
SET provider = 'local' 
WHERE provider IS NULL;

-- Log migration
SELECT 
    'OAuth2 migration completed successfully' AS status,
    NOW() AS timestamp;

-- Hiển thị cấu trúc bảng users sau migration
DESCRIBE users;