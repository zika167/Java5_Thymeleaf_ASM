#!/bin/bash
# ============================================
# UPDATE CONTROLLER TEMPLATE PATHS
# ============================================
# Script tự động update return paths trong controllers
# ============================================

set -e

echo "============================================"
echo "  UPDATING CONTROLLER TEMPLATE PATHS"
echo "============================================"
echo ""

BASE_PATH="src/main/java/poly/edu/java5_asm"

echo "📝 Updating controller return statements..."
echo ""

# Function to update return statements in a file
update_returns() {
    local file=$1
    local old_path=$2
    local new_path=$3
    
    if [ -f "$file" ]; then
        # Use sed to replace return statements
        # Match: return "old-path"
        # Replace with: return "new-path"
        sed -i.bak "s|return \"$old_path\"|return \"$new_path\"|g" "$file"
        rm -f "$file.bak"
        echo "  ✓ Updated: $file"
        echo "    $old_path → $new_path"
    fi
}

# AUTH MODULE
echo "→ Auth module..."
AUTH_CONTROLLER="$BASE_PATH/module/auth/controller/AuthController.java"
update_returns "$AUTH_CONTROLLER" "sign-in" "module/auth/sign-in"
update_returns "$AUTH_CONTROLLER" "sign-up" "module/auth/sign-up"
update_returns "$AUTH_CONTROLLER" "reset-password" "module/auth/reset-password"
update_returns "$AUTH_CONTROLLER" "reset-password-emailed" "module/auth/reset-password-emailed"

# USER MODULE
echo "→ User module..."
USER_CONTROLLER="$BASE_PATH/module/user/controller/ProfileController.java"
update_returns "$USER_CONTROLLER" "profile" "module/user/profile"
update_returns "$USER_CONTROLLER" "edit-personal-info" "module/user/edit-personal-info"

# PRODUCT MODULE
echo "→ Product module..."
PRODUCT_CONTROLLER="$BASE_PATH/module/product/controller/ProductRestController.java"
update_returns "$PRODUCT_CONTROLLER" "product-detail" "module/product/product-detail"
update_returns "$PRODUCT_CONTROLLER" "category" "module/product/category"

# CART MODULE
echo "→ Cart module..."
CART_CONTROLLER="$BASE_PATH/module/cart/controller/CartController.java"
update_returns "$CART_CONTROLLER" "cart" "module/cart/cart"

# ORDER MODULE
echo "→ Order module..."
ORDER_CONTROLLER="$BASE_PATH/module/order/controller/OrderController.java"
update_returns "$ORDER_CONTROLLER" "checkout" "module/order/checkout"
update_returns "$ORDER_CONTROLLER" "shipping" "module/order/shipping"
update_returns "$ORDER_CONTROLLER" "my-orders" "module/order/my-orders"
update_returns "$ORDER_CONTROLLER" "order-detail" "module/order/order-detail"
update_returns "$ORDER_CONTROLLER" "order-confirmation" "module/order/order-confirmation"

# PAYMENT MODULE
echo "→ Payment module..."
PAYMENT_CONTROLLER="$BASE_PATH/module/payment/controller/PaymentController.java"
update_returns "$PAYMENT_CONTROLLER" "payment" "module/payment/payment"
update_returns "$PAYMENT_CONTROLLER" "add-new-card" "module/payment/add-new-card"
update_returns "$PAYMENT_CONTROLLER" "payment-success" "module/payment/payment-success"
update_returns "$PAYMENT_CONTROLLER" "payment-failure" "module/payment/payment-failure"

# ADDRESS MODULE
echo "→ Address module..."
ADDRESS_CONTROLLER="$BASE_PATH/module/address/controller/AddressController.java"
update_returns "$ADDRESS_CONTROLLER" "addresses" "module/address/addresses"

# CAFFEINE MODULE
echo "→ Caffeine module..."
CAFFEINE_CONTROLLER="$BASE_PATH/module/caffeine/controller/CaffeineController.java"
update_returns "$CAFFEINE_CONTROLLER" "cc-doctor" "module/caffeine/cc-doctor"

# ADMIN MODULE (admin paths stay in shared)
echo "→ Admin module..."
ADMIN_CONTROLLER="$BASE_PATH/common/controller/AdminController.java"
if [ ! -f "$ADMIN_CONTROLLER" ]; then
    # Try module location
    ADMIN_CONTROLLER="$BASE_PATH/module/admin/controller/AdminController.java"
fi
update_returns "$ADMIN_CONTROLLER" "admin/dashboard" "shared/admin/dashboard"
update_returns "$ADMIN_CONTROLLER" "admin/users" "shared/admin/users"
update_returns "$ADMIN_CONTROLLER" "admin/products" "shared/admin/products"
update_returns "$ADMIN_CONTROLLER" "admin/orders" "shared/admin/orders"

ADMIN_STATS_CONTROLLER="$BASE_PATH/module/admin/controller/AdminStatisticsController.java"
update_returns "$ADMIN_STATS_CONTROLLER" "admin/dashboard" "shared/admin/dashboard"

echo ""
echo "============================================"
echo "  ✅ CONTROLLER PATHS UPDATED!"
echo "============================================"
echo ""
echo "📝 NEXT STEPS:"
echo ""
echo "1. Update fragment references in HTML files:"
echo "   Run: ./update-fragment-references.sh"
echo ""
echo "2. Build and test:"
echo "   ./mvnw clean compile"
echo "   ./mvnw spring-boot:run"
echo ""
echo "3. Test all pages manually"
echo ""
echo "4. Commit changes"
echo ""
echo "============================================"
