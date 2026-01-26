#!/bin/bash
# ============================================
# REFACTOR TO MODULE-BASED ARCHITECTURE
# ============================================
# Script tự động di chuyển files sang cấu trúc module mới
# 
# CẢNH BÁO: Script này sẽ thay đổi cấu trúc toàn bộ dự án!
# Hãy commit code trước khi chạy!
# ============================================

set -e  # Exit on error

echo "============================================"
echo "  REFACTORING TO MODULE-BASED ARCHITECTURE"
echo "============================================"
echo ""
echo "⚠️  CẢNH BÁO: Script này sẽ thay đổi cấu trúc toàn bộ dự án!"
echo ""
read -p "Bạn đã commit code chưa? (yes/no): " confirm

if [ "$confirm" != "yes" ]; then
    echo "Vui lòng commit code trước khi chạy script!"
    exit 1
fi

BASE_PATH="src/main/java/poly/edu/java5_asm"

echo ""
echo "📁 Bước 1: Tạo cấu trúc thư mục mới..."
echo ""

# Create common structure
mkdir -p "$BASE_PATH/common/config"
mkdir -p "$BASE_PATH/common/exception"
mkdir -p "$BASE_PATH/common/security"
mkdir -p "$BASE_PATH/common/util"

# Create module structures
modules=(
    "auth"
    "user"
    "product"
    "category"
    "brand"
    "cart"
    "order"
    "payment"
    "review"
    "wishlist"
    "address"
    "email"
    "caffeine"
    "admin"
)

for module in "${modules[@]}"; do
    mkdir -p "$BASE_PATH/module/$module/controller"
    mkdir -p "$BASE_PATH/module/$module/dto/request"
    mkdir -p "$BASE_PATH/module/$module/dto/response"
    mkdir -p "$BASE_PATH/module/$module/entity"
    mkdir -p "$BASE_PATH/module/$module/repository"
    mkdir -p "$BASE_PATH/module/$module/service"
    mkdir -p "$BASE_PATH/module/$module/exception"
    echo "  ✓ Created module: $module"
done

echo ""
echo "📦 Bước 2: Di chuyển Common files..."
echo ""

# Move config files
if [ -d "$BASE_PATH/config" ]; then
    for file in "$BASE_PATH/config"/*.java; do
        if [ -f "$file" ]; then
            filename=$(basename "$file")
            git mv "$file" "$BASE_PATH/common/config/$filename" 2>/dev/null || mv "$file" "$BASE_PATH/common/config/$filename"
            echo "  ✓ Moved: config/$filename"
        fi
    done
fi

# Move exception files
if [ -d "$BASE_PATH/exception" ]; then
    for file in "$BASE_PATH/exception"/*.java; do
        if [ -f "$file" ]; then
            filename=$(basename "$file")
            git mv "$file" "$BASE_PATH/common/exception/$filename" 2>/dev/null || mv "$file" "$BASE_PATH/common/exception/$filename"
            echo "  ✓ Moved: exception/$filename"
        fi
    done
fi

# Move security files
if [ -d "$BASE_PATH/security" ]; then
    for file in "$BASE_PATH/security"/*.java; do
        if [ -f "$file" ]; then
            filename=$(basename "$file")
            git mv "$file" "$BASE_PATH/common/security/$filename" 2>/dev/null || mv "$file" "$BASE_PATH/common/security/$filename"
            echo "  ✓ Moved: security/$filename"
        fi
    done
fi

# Move util files
if [ -d "$BASE_PATH/util" ]; then
    for file in "$BASE_PATH/util"/*.java; do
        if [ -f "$file" ]; then
            filename=$(basename "$file")
            git mv "$file" "$BASE_PATH/common/util/$filename" 2>/dev/null || mv "$file" "$BASE_PATH/common/util/$filename"
            echo "  ✓ Moved: util/$filename"
        fi
    done
fi

echo ""
echo "📦 Bước 3: Di chuyển Module files..."
echo ""

# Function to move files to module
move_to_module() {
    local module=$1
    local pattern=$2
    local target_dir=$3
    
    for file in "$BASE_PATH"/$pattern; do
        if [ -f "$file" ]; then
            filename=$(basename "$file")
            git mv "$file" "$BASE_PATH/module/$module/$target_dir/$filename" 2>/dev/null || mv "$file" "$BASE_PATH/module/$module/$target_dir/$filename"
            echo "  ✓ Moved to $module: $filename"
        fi
    done
}

# AUTH MODULE
echo "  → Auth module..."
move_to_module "auth" "controller/AuthController.java" "controller"
move_to_module "auth" "service/AuthService.java" "service"
move_to_module "auth" "dto/request/RegisterRequest.java" "dto/request"

# USER MODULE
echo "  → User module..."
move_to_module "user" "entity/User.java" "entity"
move_to_module "user" "entity/UserActivityLog.java" "entity"
move_to_module "user" "repository/UserRepository.java" "repository"
move_to_module "user" "repository/UserActivityLogRepository.java" "repository"
move_to_module "user" "service/UserService.java" "service"
move_to_module "user" "controller/ProfileController.java" "controller"
move_to_module "user" "dto/request/ProfileUpdateRequest.java" "dto/request"

# PRODUCT MODULE
echo "  → Product module..."
move_to_module "product" "entity/Product.java" "entity"
move_to_module "product" "repository/ProductRepository.java" "repository"
move_to_module "product" "service/ProductService.java" "service"
move_to_module "product" "controller/ProductRestController.java" "controller"
move_to_module "product" "dto/request/ProductSearchRequest.java" "dto/request"
move_to_module "product" "dto/response/ProductResponse.java" "dto/response"
move_to_module "product" "dto/response/ProductListResponse.java" "dto/response"

# CATEGORY MODULE
echo "  → Category module..."
move_to_module "category" "entity/Category.java" "entity"
move_to_module "category" "repository/CategoryRepository.java" "repository"
move_to_module "category" "dto/response/CategoryResponse.java" "dto/response"

# BRAND MODULE
echo "  → Brand module..."
move_to_module "brand" "entity/Brand.java" "entity"
move_to_module "brand" "repository/BrandRepository.java" "repository"
move_to_module "brand" "dto/response/BrandResponse.java" "dto/response"

# CART MODULE
echo "  → Cart module..."
move_to_module "cart" "entity/Cart.java" "entity"
move_to_module "cart" "entity/CartItem.java" "entity"
move_to_module "cart" "repository/CartRepository.java" "repository"
move_to_module "cart" "repository/CartItemRepository.java" "repository"
move_to_module "cart" "service/CartService.java" "service"
move_to_module "cart" "controller/CartController.java" "controller"
move_to_module "cart" "dto/request/AddToCartRequest.java" "dto/request"
move_to_module "cart" "dto/request/UpdateCartItemRequest.java" "dto/request"
move_to_module "cart" "dto/response/CartResponse.java" "dto/response"
move_to_module "cart" "dto/response/CartItemResponse.java" "dto/response"

# ORDER MODULE
echo "  → Order module..."
move_to_module "order" "entity/Order.java" "entity"
move_to_module "order" "entity/OrderItem.java" "entity"
move_to_module "order" "repository/OrderRepository.java" "repository"
move_to_module "order" "repository/OrderItemRepository.java" "repository"
move_to_module "order" "service/OrderService.java" "service"
move_to_module "order" "controller/OrderController.java" "controller"
move_to_module "order" "dto/request/CheckoutRequest.java" "dto/request"
move_to_module "order" "dto/response/OrderResponse.java" "dto/response"
move_to_module "order" "dto/response/OrderItemResponse.java" "dto/response"

# PAYMENT MODULE
echo "  → Payment module..."
move_to_module "payment" "controller/PaymentController.java" "controller"
move_to_module "payment" "service/VNPayService.java" "service"
move_to_module "payment" "service/MomoService.java" "service"
move_to_module "payment" "service/impl/VNPayServiceImpl.java" "service"
move_to_module "payment" "service/impl/MomoServiceImpl.java" "service"
move_to_module "payment" "dto/response/VNPayResponse.java" "dto/response"
move_to_module "payment" "dto/response/MomoResponse.java" "dto/response"

# REVIEW MODULE
echo "  → Review module..."
move_to_module "review" "entity/Review.java" "entity"
move_to_module "review" "repository/ReviewRepository.java" "repository"
move_to_module "review" "service/ReviewService.java" "service"
move_to_module "review" "service/impl/ReviewServiceImpl.java" "service"
move_to_module "review" "controller/ReviewController.java" "controller"
move_to_module "review" "dto/request/CreateReviewRequest.java" "dto/request"
move_to_module "review" "dto/response/ReviewResponse.java" "dto/response"
move_to_module "review" "dto/response/ReviewListResponse.java" "dto/response"
move_to_module "review" "dto/response/ProductRatingResponse.java" "dto/response"

# WISHLIST MODULE
echo "  → Wishlist module..."
move_to_module "wishlist" "entity/Wishlist.java" "entity"
move_to_module "wishlist" "repository/WishlistRepository.java" "repository"
move_to_module "wishlist" "service/WishlistService.java" "service"
move_to_module "wishlist" "service/impl/WishlistServiceImpl.java" "service"
move_to_module "wishlist" "controller/WishlistController.java" "controller"
move_to_module "wishlist" "dto/response/WishlistResponse.java" "dto/response"
move_to_module "wishlist" "exception/WishlistException.java" "exception"
move_to_module "wishlist" "exception/WishlistDuplicateException.java" "exception"
move_to_module "wishlist" "exception/WishlistNotFoundException.java" "exception"

# ADDRESS MODULE
echo "  → Address module..."
move_to_module "address" "entity/Address.java" "entity"
move_to_module "address" "repository/AddressRepository.java" "repository"
move_to_module "address" "service/impl/AddressServiceImpl.java" "service"
move_to_module "address" "controller/AddressController.java" "controller"
move_to_module "address" "dto/request/CreateAddressRequest.java" "dto/request"
move_to_module "address" "dto/response/AddressResponse.java" "dto/response"

# EMAIL MODULE
echo "  → Email module..."
move_to_module "email" "service/EmailService.java" "service"
move_to_module "email" "service/impl/EmailServiceImpl.java" "service"

# CAFFEINE MODULE
echo "  → Caffeine module..."
move_to_module "caffeine" "controller/CaffeineController.java" "controller"
move_to_module "caffeine" "service/CaffeineService.java" "service"
move_to_module "caffeine" "service/impl/CaffeineServiceImpl.java" "service"
move_to_module "caffeine" "dto/request/CaffeineCalculationRequest.java" "dto/request"
move_to_module "caffeine" "dto/result/CaffeineCalculationResult.java" "dto/response"

# ADMIN MODULE
echo "  → Admin module..."
move_to_module "admin" "controller/AdminController.java" "controller"
move_to_module "admin" "controller/AdminStatisticsController.java" "controller"
move_to_module "admin" "service/AdminStatisticsService.java" "service"
move_to_module "admin" "dto/response/DashboardStatsResponse.java" "dto/response"
move_to_module "admin" "dto/response/UserRegistrationStatsResponse.java" "dto/response"
move_to_module "admin" "dto/response/TrafficStatsResponse.java" "dto/response"

# Move HomeController (shared)
if [ -f "$BASE_PATH/controller/HomeController.java" ]; then
    git mv "$BASE_PATH/controller/HomeController.java" "$BASE_PATH/common/controller/HomeController.java" 2>/dev/null || mv "$BASE_PATH/controller/HomeController.java" "$BASE_PATH/common/controller/HomeController.java"
    echo "  ✓ Moved: HomeController.java to common"
fi

echo ""
echo "🧹 Bước 4: Dọn dẹp thư mục cũ..."
echo ""

# Remove old empty directories
old_dirs=("controller" "entity" "repository" "service" "dto" "model")
for dir in "${old_dirs[@]}"; do
    if [ -d "$BASE_PATH/$dir" ]; then
        if [ -z "$(ls -A $BASE_PATH/$dir)" ]; then
            rmdir "$BASE_PATH/$dir"
            echo "  ✓ Removed empty directory: $dir"
        else
            echo "  ⚠ Directory not empty: $dir (contains $(ls -A $BASE_PATH/$dir | wc -l) files)"
        fi
    fi
done

echo ""
echo "============================================"
echo "  ✅ MIGRATION COMPLETED!"
echo "============================================"
echo ""
echo "📝 NEXT STEPS:"
echo ""
echo "1. Open project in IntelliJ IDEA"
echo "2. Right-click on 'src/main/java' → Refactor → Optimize Imports"
echo "3. Build project: ./mvnw clean compile"
echo "4. Fix any compilation errors"
echo "5. Run tests: ./mvnw test"
echo "6. Commit changes"
echo ""
echo "⚠️  IMPORTANT:"
echo "   - All imports need to be updated!"
echo "   - Use IntelliJ's 'Refactor → Move' feature for better results"
echo "   - Test thoroughly before committing"
echo ""
echo "============================================"
