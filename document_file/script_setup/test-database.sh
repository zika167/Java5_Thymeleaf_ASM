#!/bin/bash

echo "=========================================="
echo "TESTING DATABASE CONNECTION"
echo "=========================================="
echo ""

echo "📊 Container Status:"
docker-compose ps
echo ""

echo "🔐 Testing connection with correct password..."
docker exec coffee_shop_db mariadb -uroot -prootpassword java5_asm -e "SELECT 'Connection successful!' as Status;"
echo ""

echo "📋 Listing all tables:"
docker exec coffee_shop_db mariadb -uroot -prootpassword java5_asm -e "SHOW TABLES;"
echo ""

echo "👤 Checking users:"
docker exec coffee_shop_db mariadb -uroot -prootpassword java5_asm -e "SELECT id, username, email, role, login_count FROM users;"
echo ""

echo "📦 Checking products count:"
docker exec coffee_shop_db mariadb -uroot -prootpassword java5_asm -e "SELECT COUNT(*) as total_products FROM products;"
echo ""

echo "🛒 Checking orders:"
docker exec coffee_shop_db mariadb -uroot -prootpassword java5_asm -e "SELECT order_number, status, payment_status, total_amount FROM orders;"
echo ""

echo "✅ Database test completed!"
