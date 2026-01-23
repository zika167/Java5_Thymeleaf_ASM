# MariaDB Initialization Scripts

This directory contains SQL scripts that are automatically executed when the MariaDB container is first created.

## Execution Order

Scripts are executed in alphabetical order:

1. `01-schema.sql` - Creates database schema (tables, indexes, constraints)
2. `02-data.sql` - Inserts sample data

## Files

### 01-schema.sql (UPDATED - 23/01/2026)
- Creates all database tables (13 tables)
- Defines relationships and foreign keys
- Sets up indexes for performance
- Configures character sets and collations
- **Optimized schema**: Removed payment_methods, product_variants, banners, product_images
- **OAuth2 Support**: 
  - `provider` column (local, google, facebook) with DEFAULT 'local'
  - `provider_id` column for OAuth2 user ID
  - `password` column is NULLABLE for OAuth2 users
  - Indexes for OAuth2 queries (idx_provider, idx_provider_id, idx_provider_provider_id)

### 02-data.sql (FIXED - 23/01/2026)
- ✅ Inserts sample users (admin, test users) with provider='local'
- ✅ Adds addresses for users
- ✅ Creates product categories (9 categories)
- ✅ Adds brands (5 brands)
- ✅ Inserts products (10 coffee products) with image_url
- ✅ Adds sample reviews
- ✅ Creates wishlists
- ✅ Adds sample carts and cart items
- ✅ Creates sample orders with payment_method (VNPAY, MOMO, COD)
- ✅ Adds order items
- ❌ REMOVED: payment_methods, product_images, product_variants, banners (tables don't exist)

## Usage

These scripts run automatically when you:
```bash
docker-compose up -d
```

To reset the database with fixed data:
```bash
./reset-database.sh
```

## Notes

- Scripts only run on first container creation
- To re-run scripts, you must delete the volume:
  ```bash
  docker-compose down -v
  rm -rf mariadb_data/*
  docker-compose up -d
  ```
- All passwords are BCrypt hashed
- Default admin password: `password123`
- OAuth2 users don't need password

## Changes Log

### 23/01/2026 - Major Cleanup & OAuth2 Integration
**Schema Changes (01-schema.sql):**
- ✅ Added `provider` column with DEFAULT 'local' (local, google, facebook)
- ✅ Added `provider_id` column for OAuth2 user ID
- ✅ Changed `password` to NULLABLE for OAuth2 users
- ✅ Added indexes: idx_provider, idx_provider_id, idx_provider_provider_id
- ✅ Removed `theme_preference` column (not used in backend, only localStorage)
- ✅ Removed `idx_theme` index

**Data Changes (02-data.sql):**
- ❌ Removed inserts to non-existent tables (payment_methods, product_images, product_variants, banners)
- ✅ Fixed cart_items (removed variant_id column)
- ✅ Fixed order_items (removed variant_id, variant_name columns)
- ✅ Fixed orders (removed payment_method_id, added payment_method)
- ✅ Added image_url to products
- ✅ Added provider='local' to all users
- ✅ Added INSERT IGNORE to prevent duplicate errors

**Removed Files:**
- ❌ 03-migration-optimize.sql (not needed, schema already optimized)
- ❌ 04-oauth2-migration.sql (merged into 01-schema.sql)
- ❌ 05-make-password-nullable.sql (duplicate, merged into 01-schema.sql)
