# ⚡ REVIEW FRONTEND - QUICK REFERENCE

## 🎯 CÁCH SỬ DỤNG

### Khi muốn tạo frontend, chỉ cần nói:

```
"Làm theo file REVIEW_FRONTEND_INTEGRATION_PROMPT.md"
```

Hoặc:

```
"Tạo frontend cho Review feature theo prompt đã chuẩn bị"
```

Hoặc:

```
"Implement review UI theo REVIEW_FRONTEND_INTEGRATION_PROMPT.md"
```

---

## 📋 CHECKLIST NHANH

Sau khi tạo frontend, check:

- [ ] Rating summary hiển thị đúng
- [ ] Reviews list hiển thị đúng
- [ ] Form tạo review hoạt động
- [ ] Star rating interactive
- [ ] Pagination hoạt động
- [ ] Delete review hoạt động
- [ ] Responsive trên mobile
- [ ] Authentication check đúng

---

## 🔗 API ENDPOINTS

```
GET    /api/reviews/products/{id}/rating
GET    /api/reviews/products/{id}
POST   /api/reviews/products/{id}
DELETE /api/reviews/{id}
GET    /api/reviews/products/{id}/check
GET    /api/reviews/my-reviews
```

---

## 🧪 TEST URL

```
http://localhost:8080/products/detail?id=1
```

---

## 📁 FILES CẦN TẠO

1. `reviews.css` - Styling
2. `reviews.js` - JavaScript logic
3. Update `product-detail.html` - Integration

---

## ⏱️ THỜI GIAN

**Ước tính**: 15-20 phút

---

## 📚 FULL DETAILS

Xem file: `REVIEW_FRONTEND_INTEGRATION_PROMPT.md`

---

✅ **Backend đã sẵn sàng, chỉ cần tạo UI!**
