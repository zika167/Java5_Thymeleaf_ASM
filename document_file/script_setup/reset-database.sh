#!/bin/bash

# ============================================
# RESET DATABASE SCRIPT
# ============================================
# This script will:
# 1. Stop all containers
# 2. Remove volumes (delete all data)
# 3. Start containers with fresh database
# ============================================

echo "🛑 Stopping Docker containers..."
docker-compose down

echo "🗑️  Removing volumes and data..."
docker-compose down -v
rm -rf mariadb_data/*

echo "🚀 Starting Docker containers with fresh database..."
docker-compose up -d

echo "⏳ Waiting for MariaDB to be ready..."
sleep 10

echo "✅ Database reset complete!"
echo ""
echo "📊 Check database status:"
echo "   docker-compose ps"
echo ""
echo "🔍 View logs:"
echo "   docker-compose logs -f mariadb"
echo ""
echo "💻 Connect to database:"
echo "   docker exec -it mariadb mysql -uroot -proot java5_asm"
