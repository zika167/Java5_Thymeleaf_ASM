#!/bin/bash

# ============================================
# SCRIPT TỰ ĐỘNG DỌN DẸP PROJECT
# ============================================

echo "🧹 BẮT ĐẦU DỌN DẸP PROJECT..."
echo ""

# Màu sắc
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Hàm hiển thị kích thước
show_size() {
    if [ -e "$1" ]; then
        du -sh "$1" 2>/dev/null | awk '{print $1}'
    else
        echo "0B"
    fi
}

# ============================================
# 1. BACKUP
# ============================================
echo "📦 Tạo backup..."
BACKUP_DIR="backup_$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BACKUP_DIR"

if [ -d "src/main/resources/templates/F8-project-08-main" ]; then
    echo "   Backup F8-project-08-main..."
    cp -r src/main/resources/templates/F8-project-08-main "$BACKUP_DIR/"
fi

if [ -d "src/main/resources/scss/node_modules" ]; then
    echo "   Backup node_modules info..."
    ls -la src/main/resources/scss/node_modules > "$BACKUP_DIR/node_modules_list.txt"
fi

if [ -f "src/main/resources/schema.sql" ]; then
    echo "   Backup schema.sql..."
    cp src/main/resources/schema.sql "$BACKUP_DIR/"
fi

echo -e "${GREEN}✅ Backup hoàn tất: $BACKUP_DIR${NC}"
echo ""

# ============================================
# 2. TÍNH TOÁN DUNG LƯỢNG TRƯỚC KHI XÓA
# ============================================
echo "📊 Phân tích dung lượng..."

F8_SIZE=$(show_size "src/main/resources/templates/F8-project-08-main")
NODE_SIZE=$(show_size "src/main/resources/scss/node_modules")
SCHEMA_SIZE=$(show_size "src/main/resources/schema.sql")

echo "   F8-project-08-main: $F8_SIZE"
echo "   node_modules: $NODE_SIZE"
echo "   schema.sql: $SCHEMA_SIZE"
echo ""

# ============================================
# 3. XÓA CÁC FILE KHÔNG CẦN THIẾT
# ============================================
echo "🗑️  Xóa các file không cần thiết..."

# 3.1. Xóa F8-project-08-main
if [ -d "src/main/resources/templates/F8-project-08-main" ]; then
    echo -n "   Xóa F8-project-08-main ($F8_SIZE)... "
    rm -rf src/main/resources/templates/F8-project-08-main/
    echo -e "${GREEN}✅${NC}"
else
    echo -e "   F8-project-08-main: ${YELLOW}Không tồn tại${NC}"
fi

# 3.2. Xóa node_modules
if [ -d "src/main/resources/scss/node_modules" ]; then
    echo -n "   Xóa node_modules ($NODE_SIZE)... "
    rm -rf src/main/resources/scss/node_modules/
    echo -e "${GREEN}✅${NC}"
else
    echo -e "   node_modules: ${YELLOW}Không tồn tại${NC}"
fi

# 3.3. Xóa schema.sql
if [ -f "src/main/resources/schema.sql" ]; then
    echo -n "   Xóa schema.sql ($SCHEMA_SIZE)... "
    rm src/main/resources/schema.sql
    echo -e "${GREEN}✅${NC}"
else
    echo -e "   schema.sql: ${YELLOW}Không tồn tại${NC}"
fi

# 3.4. Xóa .DS_Store
echo -n "   Xóa .DS_Store files... "
DS_COUNT=$(find . -name ".DS_Store" -type f | wc -l | tr -d ' ')
if [ "$DS_COUNT" -gt 0 ]; then
    find . -name ".DS_Store" -type f -delete
    echo -e "${GREEN}✅ ($DS_COUNT files)${NC}"
else
    echo -e "${YELLOW}Không tìm thấy${NC}"
fi

# 3.5. Xóa .git trong resources (nếu có)
echo -n "   Xóa .git folders trong resources... "
GIT_COUNT=$(find src/main/resources -name ".git" -type d 2>/dev/null | wc -l | tr -d ' ')
if [ "$GIT_COUNT" -gt 0 ]; then
    find src/main/resources -name ".git" -type d -exec rm -rf {} + 2>/dev/null
    echo -e "${GREEN}✅ ($GIT_COUNT folders)${NC}"
else
    echo -e "${YELLOW}Không tìm thấy${NC}"
fi

echo ""

# ============================================
# 4. CẬP NHẬT .gitignore
# ============================================
echo "📝 Cập nhật .gitignore..."

if ! grep -q ".DS_Store" .gitignore 2>/dev/null; then
    echo "" >> .gitignore
    echo "# macOS" >> .gitignore
    echo ".DS_Store" >> .gitignore
    echo -e "   ${GREEN}✅ Thêm .DS_Store${NC}"
fi

if ! grep -q "node_modules" .gitignore 2>/dev/null; then
    echo "" >> .gitignore
    echo "# Node modules" >> .gitignore
    echo "**/node_modules/" >> .gitignore
    echo -e "   ${GREEN}✅ Thêm node_modules${NC}"
fi

if ! grep -q "backup_" .gitignore 2>/dev/null; then
    echo "" >> .gitignore
    echo "# Backup folders" >> .gitignore
    echo "backup_*/" >> .gitignore
    echo -e "   ${GREEN}✅ Thêm backup folders${NC}"
fi

echo ""

# ============================================
# 5. TẠO CẤU TRÚC THƯ MỤC
# ============================================
echo "📁 Tạo cấu trúc thư mục chuẩn..."

# Tạo các package Java
mkdir -p src/main/java/poly/edu/java5_asm/{model,repository,service,dto,config,util}
echo -e "   ${GREEN}✅ Tạo Java packages${NC}"

# Tạo thư mục admin templates
mkdir -p src/main/resources/templates/admin
echo -e "   ${GREEN}✅ Tạo admin templates folder${NC}"

echo ""

# ============================================
# 6. TỔNG KẾT
# ============================================
echo "📊 TỔNG KẾT:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "✅ Đã xóa:"
echo "   • F8-project-08-main: $F8_SIZE"
echo "   • node_modules: $NODE_SIZE"
echo "   • schema.sql: $SCHEMA_SIZE"
echo "   • .DS_Store: $DS_COUNT files"
echo "   • .git folders: $GIT_COUNT folders"
echo ""
echo "✅ Đã tạo:"
echo "   • Java packages: model, repository, service, dto, config, util"
echo "   • Admin templates folder"
echo ""
echo "✅ Đã cập nhật:"
echo "   • .gitignore"
echo ""
echo "📦 Backup được lưu tại: $BACKUP_DIR"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo -e "${GREEN}🎉 HOÀN TẤT!${NC}"
echo ""
echo "📝 Các bước tiếp theo:"
echo "   1. Kiểm tra application: mvn spring-boot:run"
echo "   2. Test các pages: http://localhost:8080"
echo "   3. Nếu OK, commit changes: git add . && git commit -m 'Cleanup project'"
echo "   4. Nếu có vấn đề, restore từ: $BACKUP_DIR"
echo ""
