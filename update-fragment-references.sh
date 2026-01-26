#!/bin/bash
# ============================================
# UPDATE FRAGMENT REFERENCES IN HTML FILES
# ============================================
# Script tự động update Thymeleaf fragment paths
# ============================================

set -e

echo "============================================"
echo "  UPDATING FRAGMENT REFERENCES"
echo "============================================"
echo ""

TEMPLATES_PATH="src/main/resources/templates"

echo "📝 Updating fragment references in all HTML files..."
echo ""

# Find all HTML files and update fragment references
find "$TEMPLATES_PATH" -name "*.html" -type f | while read file; do
    # Backup original file
    cp "$file" "$file.bak"
    
    # Update fragment references
    # fragments/head → shared/fragments/head
    sed -i '' 's|fragments/head|shared/fragments/head|g' "$file" 2>/dev/null || sed -i 's|fragments/head|shared/fragments/head|g' "$file"
    
    # fragments/header → shared/fragments/header
    sed -i '' 's|fragments/header|shared/fragments/header|g' "$file" 2>/dev/null || sed -i 's|fragments/header|shared/fragments/header|g' "$file"
    
    # fragments/footer → shared/fragments/footer
    sed -i '' 's|fragments/footer|shared/fragments/footer|g' "$file" 2>/dev/null || sed -i 's|fragments/footer|shared/fragments/footer|g' "$file"
    
    # fragments/admin-sidebar → shared/fragments/admin-sidebar
    sed -i '' 's|fragments/admin-sidebar|shared/fragments/admin-sidebar|g' "$file" 2>/dev/null || sed -i 's|fragments/admin-sidebar|shared/fragments/admin-sidebar|g' "$file"
    
    # fragments/reviews → shared/fragments/reviews
    sed -i '' 's|fragments/reviews|shared/fragments/reviews|g' "$file" 2>/dev/null || sed -i 's|fragments/reviews|shared/fragments/reviews|g' "$file"
    
    # Check if file was modified
    if ! cmp -s "$file" "$file.bak"; then
        echo "  ✓ Updated: $(basename $file)"
        rm "$file.bak"
    else
        # No changes, restore backup
        rm "$file.bak"
    fi
done

echo ""
echo "============================================"
echo "  ✅ FRAGMENT REFERENCES UPDATED!"
echo "============================================"
echo ""
echo "📝 NEXT STEPS:"
echo ""
echo "1. Build project:"
echo "   ./mvnw clean compile"
echo ""
echo "2. Run application:"
echo "   ./mvnw spring-boot:run"
echo ""
echo "3. Test all pages:"
echo "   - Home: http://localhost:8080/"
echo "   - Sign in: http://localhost:8080/sign-in"
echo "   - Profile: http://localhost:8080/profile"
echo "   - Cart: http://localhost:8080/cart"
echo "   - Admin: http://localhost:8080/admin/dashboard"
echo ""
echo "4. If all works, commit:"
echo "   git add -A"
echo "   git commit -m 'refactor: Reorganize templates by module'"
echo ""
echo "============================================"
