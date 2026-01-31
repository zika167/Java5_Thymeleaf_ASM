# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 1.x.x   | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

Chúng tôi rất coi trọng vấn đề bảo mật. Nếu bạn phát hiện lỗ hổng bảo mật, vui lòng:

### 1. KHÔNG công khai lỗ hổng
- Không tạo public GitHub issue
- Không đăng lên mạng xã hội hoặc forum

### 2. Báo cáo qua email
- **Email:** security@fatc-grocery.com
- **Subject:** [SECURITY] Mô tả ngắn gọn

### 3. Nội dung báo cáo cần có
- Mô tả chi tiết lỗ hổng
- Các bước để reproduce
- Phiên bản bị ảnh hưởng
- Mức độ nghiêm trọng (Critical/High/Medium/Low)
- Đề xuất cách khắc phục (nếu có)

### 4. Thời gian phản hồi
- **Xác nhận nhận được:** 24-48 giờ
- **Đánh giá ban đầu:** 3-5 ngày làm việc
- **Cập nhật tiến độ:** Mỗi 7 ngày

### 5. Quy trình xử lý
1. Xác nhận và đánh giá lỗ hổng
2. Phát triển bản vá
3. Kiểm tra nội bộ
4. Phát hành bản cập nhật
5. Công bố thông tin (sau khi đã vá)

## Security Best Practices

### Cho Developers
- Không commit secrets vào repository
- Sử dụng `.env` cho environment variables
- Review code trước khi merge
- Cập nhật dependencies thường xuyên

### Cho Users
- Sử dụng mật khẩu mạnh
- Bật xác thực 2 yếu tố (nếu có)
- Không chia sẻ thông tin đăng nhập
- Báo cáo hoạt động đáng ngờ

## Acknowledgments

Chúng tôi cảm ơn những người đã báo cáo lỗ hổng bảo mật một cách có trách nhiệm.
