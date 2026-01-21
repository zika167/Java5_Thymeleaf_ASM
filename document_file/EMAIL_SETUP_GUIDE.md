# Email System Setup Guide

## 📧 Gmail Configuration for Email System

### Prerequisites
- Gmail account
- 2-Factor Authentication enabled

---

## Step 1: Enable 2-Factor Authentication

1. Go to your Google Account: https://myaccount.google.com/
2. Navigate to **Security** section
3. Find **2-Step Verification** and click **Get Started**
4. Follow the prompts to enable 2FA

---

## Step 2: Generate App Password

1. Go to: https://myaccount.google.com/apppasswords
2. Select **Mail** as the app
3. Select **Other (Custom name)** as the device
4. Enter name: `Fat C Grocery Store`
5. Click **Generate**
6. Copy the 16-character password (format: `xxxx xxxx xxxx xxxx`)

**Important**: Save this password securely. You won't be able to see it again.

---

## Step 3: Configure Application

### Option 1: Environment Variables (Recommended for Production)

**macOS/Linux:**
```bash
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=xxxx-xxxx-xxxx-xxxx
```

**Windows (Command Prompt):**
```cmd
set MAIL_USERNAME=your-email@gmail.com
set MAIL_PASSWORD=xxxx-xxxx-xxxx-xxxx
```

**Windows (PowerShell):**
```powershell
$env:MAIL_USERNAME="your-email@gmail.com"
$env:MAIL_PASSWORD="xxxx-xxxx-xxxx-xxxx"
```

### Option 2: application.properties (For Development Only)

Edit `src/main/resources/application.properties`:

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=xxxx-xxxx-xxxx-xxxx
```

**⚠️ Warning**: Never commit credentials to Git! Add to `.gitignore` if using this method.

---

## Step 4: Test Email System

### 1. Start the Application

```bash
./mvnw spring-boot:run
```

### 2. Create a Test Order

**Using Postman or curl:**

```bash
curl -X POST http://localhost:8080/api/orders/checkout \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "shippingMethod": "standard",
    "paymentMethod": "COD",
    "customerNote": "Test order"
  }'
```

### 3. Check Your Email

- Check inbox for order confirmation email
- Verify email formatting and content
- Check spam folder if not received

### 4. Test Status Update

```bash
curl -X PUT http://localhost:8080/api/orders/{orderId}/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '"CONFIRMED"'
```

---

## Troubleshooting

### Issue: "Authentication failed"

**Solution:**
- Verify 2FA is enabled
- Regenerate App Password
- Check for typos in email/password
- Remove spaces from App Password

### Issue: "Connection timeout"

**Solution:**
- Check internet connection
- Verify firewall settings
- Try different network (corporate networks may block SMTP)

### Issue: "Email not received"

**Solution:**
- Check spam/junk folder
- Verify email address is correct
- Check Gmail's "Sent" folder
- Review application logs for errors

### Issue: "535-5.7.8 Username and Password not accepted"

**Solution:**
- Use App Password, not regular Gmail password
- Ensure 2FA is enabled
- Regenerate App Password

---

## Email Configuration Details

### Current Settings (application.properties)

```properties
# Gmail SMTP Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your-app-password}

# SMTP Properties
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

# Sender Info
spring.mail.from=${MAIL_FROM:Fat C Grocery Store <noreply@fatcgrocery.com>}
```

### Features Implemented

- ✅ **Async Sending**: Emails sent in background (non-blocking)
- ✅ **Retry Logic**: Max 3 attempts with exponential backoff
- ✅ **Error Handling**: Graceful failure without breaking order flow
- ✅ **HTML Templates**: Professional Thymeleaf templates
- ✅ **Logging**: Detailed logs for debugging

---

## Testing Checklist

- [ ] 2FA enabled on Gmail account
- [ ] App Password generated
- [ ] Environment variables set
- [ ] Application starts without errors
- [ ] Order confirmation email received
- [ ] Status update email received
- [ ] Email formatting looks correct
- [ ] Links in email work
- [ ] Vietnamese characters display correctly

---

## Production Considerations

### Security
- ✅ Use environment variables (never hardcode)
- ✅ Use App Passwords (not regular passwords)
- ✅ Enable TLS/STARTTLS
- ✅ Rotate passwords regularly

### Performance
- ✅ Async sending (non-blocking)
- ✅ Connection pooling
- ✅ Timeout configurations
- ✅ Retry mechanism

### Monitoring
- ✅ Log all email attempts
- ✅ Track success/failure rates
- ✅ Alert on repeated failures
- ✅ Monitor email queue

### Alternatives to Gmail

For production, consider:
- **SendGrid** - Reliable, scalable
- **Amazon SES** - Cost-effective
- **Mailgun** - Developer-friendly
- **Postmark** - Transactional emails

---

## Example Email Templates

### Order Confirmation Email
- ✅ Order number and date
- ✅ Order status
- ✅ Product items with quantities
- ✅ Price breakdown (subtotal, shipping, tax, total)
- ✅ Customer notes
- ✅ Call-to-action button

### Order Status Update Email
- ✅ Status timeline
- ✅ Current status highlight
- ✅ Order details
- ✅ Payment information
- ✅ Call-to-action button

---

## Support

If you encounter issues:

1. Check application logs: `logs/spring.log`
2. Enable debug logging:
   ```properties
   logging.level.org.springframework.mail=DEBUG
   ```
3. Test SMTP connection manually
4. Review Gmail's security settings
5. Contact team lead if issues persist

---

## Quick Start Commands

```bash
# Set environment variables
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password

# Run application
./mvnw spring-boot:run

# Test email (after creating order)
# Check your inbox!
```

---

**Last Updated**: 21/01/2026  
**Version**: 1.0  
**Status**: ✅ Ready for Testing
