# Prompt: Tạo chức năng Payment Card

## Yêu cầu
Tạo đầy đủ backend để lưu thông tin thẻ thanh toán (credit/debit card) cho user.

## Cấu trúc cần tạo

### 1. Entity: PaymentCard
- Tạo file: `src/main/java/poly/edu/java5_asm/module/payment/entity/PaymentCard.java`
- Fields:
  - id (Long, auto-generated)
  - user (ManyToOne relationship với User)
  - cardHolderFirstName (String)
  - cardHolderLastName (String)
  - cardNumber (String, encrypted)
  - expirationDate (String, format MM/YY)
  - cvv (String, encrypted - chỉ lưu tạm, thực tế không nên lưu)
  - phoneNumber (String)
  - isDefault (Boolean)
  - cardType (String - VISA, MASTERCARD, etc.)
  - lastFourDigits (String - để hiển thị)
  - createdAt, updatedAt (timestamps)

### 2. Repository: PaymentCardRepository
- Tạo file: `src/main/java/poly/edu/java5_asm/module/payment/repository/PaymentCardRepository.java`
- Methods:
  - findByUser(User user)
  - findByUserAndIsDefaultTrue(User user)
  - findByIdAndUser(Long id, User user)

### 3. DTO Request/Response
- `PaymentCardRequest.java` - cho form submit
- `PaymentCardResponse.java` - cho API response

### 4. Service: PaymentCardService
- Tạo file: `src/main/java/poly/edu/java5_asm/module/payment/service/PaymentCardService.java`
- Methods:
  - addCard(User user, PaymentCardRequest request)
  - getCards(User user)
  - getDefaultCard(User user)
  - setDefaultCard(User user, Long cardId)
  - deleteCard(User user, Long cardId)
  - Encrypt/mask card number

### 5. Controller: PaymentCardController
- Tạo file: `src/main/java/poly/edu/java5_asm/module/payment/controller/PaymentCardController.java`
- Endpoints:
  - POST /api/payment-cards - thêm card mới
  - GET /api/payment-cards - lấy danh sách cards
  - PUT /api/payment-cards/{id}/default - set default card
  - DELETE /api/payment-cards/{id} - xóa card

### 6. Update Template
- Sửa form action trong `add-new-card.html` thành POST đến `/api/payment-cards`
- Thêm trang hiển thị danh sách cards đã lưu

## Lưu ý bảo mật
- KHÔNG lưu CVV trong database (chỉ dùng để validate rồi bỏ)
- Encrypt card number, chỉ lưu 4 số cuối để hiển thị
- Sử dụng HTTPS
- Trong production, nên dùng payment gateway (Stripe, PayPal, VNPay) thay vì tự lưu card

## Database Migration
```sql
CREATE TABLE payment_cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    card_holder_first_name VARCHAR(100) NOT NULL,
    card_holder_last_name VARCHAR(100) NOT NULL,
    card_number_encrypted VARCHAR(255) NOT NULL,
    last_four_digits VARCHAR(4) NOT NULL,
    expiration_date VARCHAR(5) NOT NULL,
    phone_number VARCHAR(20),
    card_type VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## Template hiện tại
- File: `src/main/resources/templates/module/payment/add-new-card.html`
- Form fields: firstName, lastName, cardNumber, expirationDate, cvv, phoneNumber, setDefaultCard
