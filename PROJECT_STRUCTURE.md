# 📁 CẤU TRÚC DỰ ÁN - GROCERY STORE

```
java5_asm/
│
├── 📄 Configuration Files
│   ├── .gitignore                          # Git ignore rules
│   ├── .gitattributes                      # Git attributes
│   ├── docker-compose.yml                  # Docker configuration
│   ├── pom.xml                             # Maven dependencies
│   ├── mvnw                                # Maven wrapper (Unix)
│   └── mvnw.cmd                            # Maven wrapper (Windows)
│
├── 📚 Documentation
│   ├── README.md                           # Project overview
│   ├── 1.REFACTOR_REPORT.md               # FE refactor report
│   ├── 2.INTERFACE_FIX_REPORT.md          # Interface fix report
│   ├── 3.DATABASE_ANALYSIS_REPORT.md      # Database analysis
│   ├── DATABASE_DESIGN.md                  # Database design doc
│   ├── DATABASE_SETUP.md                   # Setup instructions
│   ├── PROJECT_STATISTICS.md               # Full statistics
│   └── QUICK_STATS.md                      # Quick overview
│
├── 🐳 Docker & Database
│   └── mariadb_init/                       # Auto-init SQL scripts
│       ├── 01-schema.sql                   # Database schema (15 tables)
│       ├── 02-data.sql                     # Sample data (90+ records)
│       └── README.md                       # Init folder guide
│
├── 📦 Source Code
│   └── src/
│       ├── main/
│       │   ├── java/poly/edu/java5_asm/
│       │   │   ├── Java5AsmApplication.java        # Main Spring Boot app
│       │   │   └── controller/
│       │   │       └── HomeController.java         # Home controller
│       │   │
│       │   └── resources/
│       │       ├── application.properties          # Spring Boot config
│       │       ├── schema.sql                      # Backup schema
│       │       │
│       │       ├── 🎨 templates/                   # Thymeleaf templates
│       │       │   ├── fragments/                  # Reusable fragments
│       │       │   │   ├── head.html              # <head> section
│       │       │   │   ├── header.html            # Navigation (1,701 lines)
│       │       │   │   └── footer.html            # Footer
│       │       │   │
│       │       │   ├── index.html                 # Home page
│       │       │   ├── sign-in.html               # Login page
│       │       │   ├── sign-up.html               # Register page
│       │       │   ├── reset-password.html        # Forgot password
│       │       │   ├── reset-password-emailed.html # Reset confirmation
│       │       │   ├── category.html              # Product listing
│       │       │   ├── product-detail.html        # Product detail (920 lines)
│       │       │   ├── checkout.html              # Shopping cart
│       │       │   ├── shipping.html              # Shipping info (693 lines)
│       │       │   ├── payment.html               # Payment method (570 lines)
│       │       │   ├── profile.html               # User profile (436 lines)
│       │       │   ├── edit-personal-info.html    # Edit profile (358 lines)
│       │       │   ├── favourite.html             # Wishlist (302 lines)
│       │       │   └── add-new-card.html          # Add payment card (438 lines)
│       │       │
│       │       ├── 🎨 static/assets/              # Static resources
│       │       │   ├── css/                       # Compiled CSS
│       │       │   │   ├── main.css              # Main stylesheet (4,808 lines)
│       │       │   │   ├── main.css.map          # Source map
│       │       │   │   └── slideshow.css         # Slideshow styles
│       │       │   │
│       │       │   ├── js/                       # JavaScript files
│       │       │   │   ├── scripts.js            # Main scripts
│       │       │   │   ├── slideshow.js          # Banner carousel
│       │       │   │   └── products.js           # Product interactions
│       │       │   │
│       │       │   ├── fonts/                    # Web fonts (21 files)
│       │       │   │   ├── Gordita-*.woff        # Font files
│       │       │   │   ├── Gordita-*.woff2       # Font files
│       │       │   │   └── stylesheet.css        # Font declarations
│       │       │   │
│       │       │   ├── icon/                     # SVG icons (48+ files)
│       │       │   │   ├── logo.svg
│       │       │   │   ├── search.svg
│       │       │   │   ├── heart.svg
│       │       │   │   ├── buy.svg
│       │       │   │   ├── profile.svg
│       │       │   │   ├── payment methods/      # Payment icons
│       │       │   │   │   ├── visa.svg
│       │       │   │   │   ├── mastercard.svg
│       │       │   │   │   ├── paypal.svg
│       │       │   │   │   └── ...
│       │       │   │   └── ...
│       │       │   │
│       │       │   ├── img/                      # Images
│       │       │   │   ├── avatar.jpg
│       │       │   │   ├── auth/                 # Auth page images
│       │       │   │   │   ├── intro.svg
│       │       │   │   │   └── forgot-password.png
│       │       │   │   ├── avatar/               # User avatars
│       │       │   │   │   ├── avatar-1.png
│       │       │   │   │   ├── avatar-2.png
│       │       │   │   │   └── avatar-3.png
│       │       │   │   ├── card/                 # Card backgrounds
│       │       │   │   │   ├── leaf.svg
│       │       │   │   │   ├── leaf-bg.svg
│       │       │   │   │   ├── plane.svg
│       │       │   │   │   └── plane-bg.svg
│       │       │   │   ├── category/             # Category icons (38 files)
│       │       │   │   │   ├── cate-1.1.svg
│       │       │   │   │   ├── cate-1.2.svg
│       │       │   │   │   └── ...
│       │       │   │   ├── category-item/        # Category items
│       │       │   │   │   ├── item-1.png
│       │       │   │   │   ├── item-2.png
│       │       │   │   │   └── item-3.png
│       │       │   │   ├── payment/              # Payment images
│       │       │   │   │   ├── delivery-1.png
│       │       │   │   │   └── delivery-2.png
│       │       │   │   ├── product/              # Product images
│       │       │   │   │   ├── item-1.png
│       │       │   │   │   ├── item-2.png
│       │       │   │   │   ├── item-3.png
│       │       │   │   │   ├── item-4.png
│       │       │   │   │   ├── item-5.png
│       │       │   │   │   ├── item-6.png
│       │       │   │   │   ├── item-7.png
│       │       │   │   │   └── item-8.png
│       │       │   │   ├── profile/              # Profile images
│       │       │   │   │   └── cover.jpg
│       │       │   │   └── slideshow/            # Homepage banners
│       │       │   │       ├── item-1.png
│       │       │   │       ├── item-1-md.png
│       │       │   │       ├── slider-2.png
│       │       │   │       ├── slider-3.png
│       │       │   │       ├── slider-4.png
│       │       │   │       └── slider-5.png
│       │       │   │
│       │       │   └── favicon/                  # Favicon files (9 files)
│       │       │       ├── favicon.ico
│       │       │       ├── favicon-16x16.png
│       │       │       ├── favicon-32x32.png
│       │       │       ├── apple-touch-icon.png
│       │       │       ├── android-chrome-96x96.png
│       │       │       ├── safari-pinned-tab.svg
│       │       │       ├── site.webmanifest
│       │       │       ├── browserconfig.xml
│       │       │       └── mstile-150x150.png
│       │       │
│       │       └── 🎨 scss/                      # SCSS source files
│       │           ├── main.scss                 # Main entry point
│       │           ├── package.json              # NPM config
│       │           ├── package-lock.json         # NPM lock
│       │           ├── README.md                 # SCSS guide
│       │           │
│       │           ├── abstracts/                # Variables & Mixins
│       │           │   ├── _index.scss
│       │           │   └── _mixins.scss
│       │           │
│       │           ├── base/                     # Base styles
│       │           │   ├── _index.scss
│       │           │   ├── _reset.scss           # CSS reset
│       │           │   ├── _base.scss            # Base elements
│       │           │   ├── _grid.scss            # Grid system
│       │           │   ├── _utils.scss           # Utility classes
│       │           │   └── _animation.scss       # Animations
│       │           │
│       │           ├── components/               # UI Components (18 files)
│       │           │   ├── _index.scss
│       │           │   ├── _buttons.scss
│       │           │   ├── _forms.scss
│       │           │   ├── _modal.scss
│       │           │   ├── _dropdown.scss
│       │           │   ├── _logo.scss
│       │           │   ├── _search-bar.scss
│       │           │   ├── _product-card.scss
│       │           │   ├── _cate-item.scss
│       │           │   ├── _slideshow.scss
│       │           │   ├── _breadcrumbs.scss
│       │           │   ├── _filter-form.scss
│       │           │   ├── _review-card.scss
│       │           │   ├── _favourite-item.scss
│       │           │   ├── _payment-card.scss
│       │           │   ├── _account-info.scss
│       │           │   ├── _messages.scss
│       │           │   └── _text-content.scss
│       │           │
│       │           ├── layout/                   # Layout components
│       │           │   ├── _index.scss
│       │           │   ├── _header.scss          # Header styles
│       │           │   └── _footer.scss          # Footer styles
│       │           │
│       │           ├── pages/                    # Page-specific styles
│       │           │   ├── _index.scss
│       │           │   ├── _home.scss            # Homepage
│       │           │   ├── _auth.scss            # Login/Register
│       │           │   ├── _checkout.scss        # Cart/Checkout
│       │           │   ├── _product-detail.scss  # Product page
│       │           │   ├── _profile.scss         # User profile
│       │           │   └── _add-new-card.scss    # Add card page
│       │           │
│       │           └── theme/                    # Theme styles
│       │               ├── _index.scss
│       │               ├── _light.scss           # Light mode
│       │               └── _dark.scss            # Dark mode
│       │
│       └── test/java/poly/edu/java5_asm/
│           └── Java5AsmApplicationTests.java     # Unit tests
│
├── 🗂️ IDE & Build
│   ├── .vscode/                            # VS Code settings
│   │   └── settings.json
│   ├── .idea/                              # IntelliJ IDEA settings (gitignored)
│   └── .mvn/                               # Maven wrapper files
│       └── wrapper/
│           └── maven-wrapper.properties
│
└── 📊 Generated (gitignored)
    ├── target/                             # Maven build output
    └── mariadb_data/                       # Docker MariaDB data
```

---

## 📊 THỐNG KÊ

### Tổng quan
- **Total Files:** 146 files
- **Total Lines:** 37,308 lines
- **Total Size:** 1.24 MB

### Phân loại theo thư mục

| Directory | Files | Lines | Description |
|-----------|-------|-------|-------------|
| `templates/` | 17 | 6,979 | Thymeleaf HTML templates |
| `scss/` | 80 | 8,350 | SCSS source files |
| `static/assets/css/` | 3 | 4,967 | Compiled CSS |
| `static/assets/js/` | 3 | 1,723 | JavaScript files |
| `static/assets/fonts/` | 21 | 90 | Web fonts |
| `static/assets/icon/` | 48+ | - | SVG icons |
| `static/assets/img/` | 40+ | - | Images |
| `static/assets/favicon/` | 9 | 9 | Favicon files |
| `java/` | 3 | 107 | Java source code |
| `mariadb_init/` | 3 | 668 | SQL scripts |
| `docs/` | 8 | 2,627 | Documentation |

### Phân loại theo loại file

| Type | Count | Description |
|------|-------|-------------|
| `.html` | 35 | Thymeleaf templates |
| `.scss` | 80 | SCSS source files |
| `.css` | 6 | Compiled CSS |
| `.js` | 6 | JavaScript files |
| `.java` | 3 | Java source code |
| `.sql` | 3 | Database scripts |
| `.md` | 8 | Documentation |
| `.svg` | 48+ | Vector icons |
| `.png/.jpg` | 40+ | Raster images |
| `.woff/.woff2` | 21 | Web fonts |
| `.xml` | 3 | Configuration |

---

## 🎯 CÁC FILE QUAN TRỌNG

### Configuration
- `pom.xml` - Maven dependencies và build config
- `application.properties` - Spring Boot configuration
- `docker-compose.yml` - Docker MariaDB setup

### Main Application
- `Java5AsmApplication.java` - Spring Boot entry point
- `HomeController.java` - Main controller

### Database
- `mariadb_init/01-schema.sql` - Database schema (15 tables)
- `mariadb_init/02-data.sql` - Sample data (90+ records)

### Frontend Core
- `templates/fragments/header.html` - Navigation (1,701 lines)
- `templates/fragments/footer.html` - Footer
- `templates/fragments/head.html` - HTML head section
- `static/assets/css/main.css` - Main stylesheet (4,808 lines)
- `static/assets/js/scripts.js` - Main JavaScript

### Documentation
- `README.md` - Project overview
- `DATABASE_DESIGN.md` - Database design documentation
- `PROJECT_STATISTICS.md` - Full project statistics

---

## 🔍 NOTES

### Gitignored Directories
- `target/` - Maven build output
- `mariadb_data/` - Docker database data
- `.idea/` - IntelliJ IDEA settings
- `node_modules/` - NPM packages (if any)

### Frontend Source (Reference)
- `templates/F8-project-08-main/` - Original FE source (for reference)
  - Kept for comparison and backup
  - Not used in production

### SCSS Compilation
- Source: `src/main/resources/scss/`
- Output: `src/main/resources/static/assets/css/main.css`
- Use `npm run build` in scss/ folder to compile

---

*Cấu trúc dự án được tạo tự động - 17/01/2026*
