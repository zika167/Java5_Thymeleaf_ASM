# Hướng dẫn sử dụng SASS Compiler

## 📦 Cài đặt

SASS đã được cài đặt sẵn. Nếu cần cài lại:

```bash
cd src/main/resources/scss
npm install
```

## 🚀 Các lệnh compile

### 1. Compile một lần (production)

```bash
npm run sass
```

- Output: `../static/assets/css/main.css`
- Style: compressed (minified)

### 2. Watch mode (development)

```bash
npm run sass:watch
```

- Tự động compile khi file `.scss` thay đổi
- Nhấn `Ctrl+C` để dừng

### 3. Watch mode với source map

```bash
npm run sass:dev
```

- Tạo file `.map` để debug trong browser
- Style: expanded (dễ đọc)

## 📁 Cấu trúc thư mục SCSS

```
scss/
├── main.scss              # File chính - import tất cả
├── abstracts/             # Variables, mixins, functions
│   ├── _index.scss
│   └── _mixins.scss
├── base/                  # Reset, typography, grid
│   ├── _animation.scss
│   ├── _base.scss
│   ├── _grid.scss
│   ├── _index.scss
│   ├── _reset.scss
│   └── _utils.scss
├── components/            # Buttons, cards, forms, ...
│   ├── _buttons.scss
│   ├── _forms.scss
│   ├── _product-card.scss
│   └── ... (18 files)
├── layout/                # Header, footer
│   ├── _footer.scss
│   ├── _header.scss
│   └── _index.scss
├── pages/                 # Page-specific styles
│   ├── _auth.scss
│   ├── _checkout.scss
│   ├── _home.scss
│   └── ... (7 files)
└── theme/                 # Dark/Light themes
    ├── _dark.scss
    ├── _light.scss
    └── _index.scss
```

## ✏️ Cách sửa style

### Ví dụ 1: Đổi màu primary button

Mở file `components/_buttons.scss`:

```scss
.btn--primary {
    background: #ffb700;  // Đổi màu này
    color: #1a1a1a;
}
```

### Ví dụ 2: Đổi font size heading

Mở file `base/_base.scss`:

```scss
h1 {
    font-size: 2.4rem;  // Đổi size này
}
```

### Ví dụ 3: Thêm style mới cho trang

Tạo file mới trong `pages/`, ví dụ `_contact.scss`:

```scss
.contact-page {
    padding: 20px;
    
    &__title {
        font-size: 2rem;
        color: #333;
    }
}
```

Sau đó import vào `pages/_index.scss`:

```scss
@forward "contact";
```

## 🔧 Workflow khuyến nghị

1. Mở terminal, chạy watch mode:
   ```bash
   cd src/main/resources/scss
   npm run sass:watch
   ```

2. Sửa file `.scss` trong IDE

3. SASS tự động compile → CSS được cập nhật

4. Refresh browser để xem thay đổi

## ⚠️ Lưu ý

- **KHÔNG sửa trực tiếp** file `static/assets/css/main.css`
- Luôn sửa file `.scss` và compile lại
- Các deprecation warnings có thể bỏ qua (không ảnh hưởng output)
