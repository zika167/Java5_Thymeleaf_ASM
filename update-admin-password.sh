#!/bin/bash
# ============================================
# UPDATE ADMIN PASSWORD SCRIPT
# ============================================
# Script này CHỈ update password admin
# KHÔNG xóa dữ liệu khác trong database
# ============================================

echo ""
echo "============================================"
echo "  UPDATE ADMIN PASSWORD"
echo "============================================"
echo ""

# Kiểm tra Docker đang chạy
if ! docker info > /dev/null 2>&1; then
    echo "[ERROR] Docker is not running!"
    echo "Please start Docker first."
    echo ""
    exit 1
fi

# Kiểm tra database container đang chạy
if ! docker ps | grep -q coffee_shop_db; then
    echo "[ERROR] Database container is not running!"
    echo "Please start it with: docker-compose up -d mariadb"
    echo ""
    exit 1
fi

echo "Updating admin password to: password123"
echo ""

docker exec coffee_shop_db mariadb -ujava5_user -pjava5_password java5_asm -e "UPDATE users SET password='\$2a\$10\$547jHD5iIaAp/7g1OKx3rO4cRul78RDFuaeeZT60rHCvyfnLSB2WO' WHERE username='admin';"

if [ $? -ne 0 ]; then
    echo "[ERROR] Failed to update password!"
    exit 1
fi

echo ""
echo "Verifying admin account..."
docker exec coffee_shop_db mariadb -ujava5_user -pjava5_password java5_asm -e "SELECT username, email, role FROM users WHERE username='admin';"

echo ""
echo "============================================"
echo "  PASSWORD UPDATE COMPLETED!"
echo "============================================"
echo ""
echo "Admin Login Credentials:"
echo "  Username: admin"
echo "  Email: admin@grocerystore.com"
echo "  Password: password123"
echo ""
echo "============================================"
