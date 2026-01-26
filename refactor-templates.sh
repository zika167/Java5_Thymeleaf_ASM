#!/bin/bash
# ============================================
# REFACTOR TEMPLATES TO MODULE STRUCTURE
# ============================================
# Script tự động tổ chức lại templates theo modules
# ============================================

set -e

echo "============================================"
echo "  REFACTORING TEMPLATES TO MODULE STRUCTURE"
echo "============================================"
echo ""

BASE_PATH="src/main/resources/templates"

echo "📁 Bước 1: Tạo cấu trúc thư mục mới..."
echo ""

# Create module template directories
mkdir -p "$BASE_PATH/module/auth"
mkdir -p "$BASE_PATH/module/user"
mkdir -p "$BASE_PATH/module/product"
mkdir -p "$BASE_PATH/module/cart"
mkdir -p "$BASE_PATH/module/order"
mkdir -p "$BASE_PATH/module/payment"
mkdir -p "$BASE_PATH/module/review"
mkdir -p "$BASE_PATH/module/wishlist"
mkdir -p "$BASE_PATH/module/address"
mkdir -p "$BASE_PATH/module/caffeine"

# Keep shared directories
mkdir -p "$BASE_PATH/shared/fragments"
mkdir -p "$BASE_PATH/shared/email"
mkdir -p "$BASE_PATH/shared/admin"

echo "  ✓ Created template directories"

echo ""
echo "📦 Bước 2: Di chuyển Shared templates..."
echo ""

# Move fragments (shared across all modules)
if [ -d "$BASE_PATH/fragments" ]; then
    for file in "$BASE_PATH/fragments"/*.html; do
        if [ -f "$file" ]; then
            filename=$(basename "$file")
            git mv "$file" "$BASE_PATH/shared/fragments/$filename" 2>/dev/null || mv "$file" "$BASE_PATH/shared/fragments/$filename"
            echo "  ✓ Moved fragment: $filename"
        fi
    done
    rmdir "$BASE_PATH/fragments" 2>/dev/null || true
fi

# Move email templates
if [ -d "$BASE_PATH/email" ]; then
    for file in "$BASE_PATH/email"/*.html; do
        if [ -f "$file" ]; then
            filename=$(basename "$file")
            git mv "$file" "$BASE_PATH/shared/email/$filename" 2>/dev/null || mv "$file" "$BASE_PATH/shared/email/$filename"
            echo "  ✓ Moved email: $filename"
        fi
    done
    rmdir "$BASE_PATH/email" 2>/dev/null || true
fi

# Move admin templates
if [ -d "$BASE_PATH/admin" ]; then
    for file in "$BASE_PATH/admin"/*.html; do
        if [ -f "$file" ]; then
            filename=$(basename "$file")
            git mv "$file" "$BASE_PATH/shared/admin/$filename" 2>/dev/null || mv "$file" "$BASE_PATH/shared/admin/$filename"
            echo "  ✓ Moved admin: $filename"
        fi
    done
    rmdir "$BASE_PATH/admin" 2>/dev/null || true
fi

echo ""
echo "📦 Bước 3: Di chuyển Module templates..."
echo ""

# AUTH MODULE
echo "  → Auth module..."
for file in sign-in.html sign-up.html reset-password.html reset-password-emailed.html; do
    if [ -f "$BASE_PATH/$file" ]; then
        git mv "$BASE_PATH/$file" "$BASE_PATH/module/auth/$file" 2>/dev/null || mv "$BASE_PATH/$file" "$BASE_PATH/module/auth/$file"
        echo "    ✓ $file"
    fi
done

# USER MODULE
echo "  → User module..."
for file in profile.html edit-personal-info.html; do
    if [ -f "$BASE_PATH/$file" ]; then
        git mv "$BASE_PATH/$file" "$BASE_PATH/module/user/$file" 2>/dev/null || mv "$BASE_PATH/$file" "$BASE_PATH/module/user/$file"
        echo "    ✓ $file"
    fi
done

# PRODUCT MODULE
echo "  → Product module..."
for file in product-detail.html category.html; do
    if [ -f "$BASE_PATH/$file" ]; then
        git mv "$BASE_PATH/$file" "$BASE_PATH/module/product/$file" 2>/dev/null || mv "$BASE_PATH/$file" "$BASE_PATH/module/product/$file"
        echo "    ✓ $file"
    fi
done

# CART MODULE
echo "  → Cart module..."
if [ -f "$BASE_PATH/cart.html" ]; then
    git mv "$BASE_PATH/cart.html" "$BASE_PATH/module/cart/cart.html" 2>/dev/null || mv "$BASE_PATH/cart.html" "$BASE_PATH/module/cart/cart.html"
    echo "    ✓ cart.html"
fi

# ORDER MODULE
echo "  → Order module..."
for file in checkout.html shipping.html my-orders.html order-detail.html order-confirmation.html; do
    if [ -f "$BASE_PATH/$file" ]; then
        git mv "$BASE_PATH/$file" "$BASE_PATH/module/order/$file" 2>/dev/null || mv "$BASE_PATH/$file" "$BASE_PATH/module/order/$file"
        echo "    ✓ $file"
    fi
done

# PAYMENT MODULE
echo "  → Payment module..."
for file in payment.html add-new-card.html payment-success.html payment-failure.html; do
    if [ -f "$BASE_PATH/$file" ]; then
        git mv "$BASE_PATH/$file" "$BASE_PATH/module/payment/$file" 2>/dev/null || mv "$BASE_PATH/$file" "$BASE_PATH/module/payment/$file"
        echo "    ✓ $file"
    fi
done

# WISHLIST MODULE
echo "  → Wishlist module..."
if [ -f "$BASE_PATH/favourite.html" ]; then
    git mv "$BASE_PATH/favourite.html" "$BASE_PATH/module/wishlist/favourite.html" 2>/dev/null || mv "$BASE_PATH/favourite.html" "$BASE_PATH/module/wishlist/favourite.html"
    echo "    ✓ favourite.html"
fi

# ADDRESS MODULE
echo "  → Address module..."
if [ -f "$BASE_PATH/addresses.html" ]; then
    git mv "$BASE_PATH/addresses.html" "$BASE_PATH/module/address/addresses.html" 2>/dev/null || mv "$BASE_PATH/addresses.html" "$BASE_PATH/module/address/addresses.html"
    echo "    ✓ addresses.html"
fi

# CAFFEINE MODULE
echo "  → Caffeine module..."
if [ -f "$BASE_PATH/cc-doctor.html" ]; then
    git mv "$BASE_PATH/cc-doctor.html" "$BASE_PATH/module/caffeine/cc-doctor.html" 2>/dev/null || mv "$BASE_PATH/cc-doctor.html" "$BASE_PATH/module/caffeine/cc-doctor.html"
    echo "    ✓ cc-doctor.html"
fi

# Keep index.html at root (home page)
echo ""
echo "  → Keeping index.html at root (home page)"

echo ""
echo "============================================"
echo "  ✅ TEMPLATE MIGRATION COMPLETED!"
echo "============================================"
echo ""
echo "📝 NEXT STEPS:"
echo ""
echo "1. Update controller methods to use new paths:"
echo "   - Old: return \"sign-in\""
echo "   - New: return \"module/auth/sign-in\""
echo ""
echo "2. Update Thymeleaf fragment references:"
echo "   - Old: th:replace=\"~{fragments/header :: header}\""
echo "   - New: th:replace=\"~{shared/fragments/header :: header}\""
echo ""
echo "3. Test all pages to ensure they load correctly"
echo ""
echo "4. Commit changes"
echo ""
echo "============================================"
