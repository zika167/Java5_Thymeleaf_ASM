# 🚀 HƯỚNG DẪN DEPLOY

## Mục lục
1. [Deploy lên Render](#1-deploy-lên-render)
2. [Deploy lên Google Cloud Run](#2-deploy-lên-google-cloud-run)
3. [Deploy lên Google App Engine](#3-deploy-lên-google-app-engine)
4. [Cấu hình Database](#4-cấu-hình-database)

---

## 1. Deploy lên Render

### Bước 1: Tạo Database
Render không hỗ trợ MariaDB native. Có 2 lựa chọn:

**Option A: Dùng PostgreSQL của Render (Khuyến nghị)**
1. Vào [Render Dashboard](https://dashboard.render.com)
2. New → PostgreSQL
3. Chọn plan Free
4. Copy connection string

**Option B: Dùng External MariaDB**
- [PlanetScale](https://planetscale.com) - MySQL compatible, free tier
- [Railway](https://railway.app) - MariaDB, free tier
- [Aiven](https://aiven.io) - MariaDB, free trial

### Bước 2: Deploy Application
```bash
# Cách 1: Connect GitHub repo
# 1. Vào Render Dashboard → New → Web Service
# 2. Connect GitHub repository
# 3. Render tự detect render.yaml

# Cách 2: Manual deploy
# 1. New → Web Service → Build from Dockerfile
# 2. Chọn repository
# 3. Set environment variables
```

### Bước 3: Set Environment Variables
Trong Render Dashboard → Environment:
```
SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/dbname
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password
GOOGLE_CLIENT_ID=your-client-id
GOOGLE_CLIENT_SECRET=your-client-secret
JWT_SECRET=your-secret-key-minimum-32-characters
APP_BASE_URL=https://your-app.onrender.com
SPRING_PROFILES_ACTIVE=prod
```

### Lưu ý cho Render
- Free tier sẽ sleep sau 15 phút không có request
- Cold start mất ~30-60 giây
- Upgrade lên Starter ($7/month) để không sleep

---

## 2. Deploy lên Google Cloud Run

### Bước 1: Cài đặt Google Cloud CLI
```bash
# Windows (PowerShell)
(New-Object Net.WebClient).DownloadFile("https://dl.google.com/dl/cloudsdk/channels/rapid/GoogleCloudSDKInstaller.exe", "$env:Temp\GoogleCloudSDKInstaller.exe")
& $env:Temp\GoogleCloudSDKInstaller.exe

# Hoặc dùng winget
winget install Google.CloudSDK
```

### Bước 2: Login và setup project
```bash
gcloud auth login
gcloud config set project YOUR_PROJECT_ID
gcloud services enable run.googleapis.com containerregistry.googleapis.com cloudbuild.googleapis.com
```

### Bước 3: Tạo Database (Cloud SQL)
```bash
# Tạo Cloud SQL instance (MariaDB)
gcloud sql instances create fat-c-grocery-db \
  --database-version=MYSQL_8_0 \
  --tier=db-f1-micro \
  --region=asia-southeast1

# Tạo database
gcloud sql databases create java5_asm --instance=fat-c-grocery-db

# Tạo user
gcloud sql users create java5_user \
  --instance=fat-c-grocery-db \
  --password=YOUR_PASSWORD
```

### Bước 4: Deploy
```bash
# Cách 1: Dùng Cloud Build (CI/CD)
gcloud builds submit --config=cloudbuild.yaml

# Cách 2: Deploy trực tiếp
gcloud run deploy fat-c-grocery-store \
  --source . \
  --region asia-southeast1 \
  --allow-unauthenticated \
  --set-env-vars "SPRING_PROFILES_ACTIVE=prod" \
  --set-env-vars "SPRING_DATASOURCE_URL=jdbc:mysql://IP:3306/java5_asm" \
  --set-env-vars "SPRING_DATASOURCE_USERNAME=java5_user" \
  --set-secrets "SPRING_DATASOURCE_PASSWORD=db-password:latest" \
  --set-secrets "JWT_SECRET=jwt-secret:latest" \
  --set-secrets "GOOGLE_CLIENT_ID=google-client-id:latest" \
  --set-secrets "GOOGLE_CLIENT_SECRET=google-client-secret:latest"
```

### Bước 5: Lưu secrets vào Secret Manager
```bash
# Tạo secrets
echo -n "your-db-password" | gcloud secrets create db-password --data-file=-
echo -n "your-jwt-secret" | gcloud secrets create jwt-secret --data-file=-
echo -n "your-google-client-id" | gcloud secrets create google-client-id --data-file=-
echo -n "your-google-client-secret" | gcloud secrets create google-client-secret --data-file=-
```

---

## 3. Deploy lên Google App Engine

### Bước 1: Build JAR file
```bash
./mvnw clean package -DskipTests
```

### Bước 2: Deploy
```bash
gcloud app deploy app.yaml --project=YOUR_PROJECT_ID
```

### Bước 3: Xem logs
```bash
gcloud app logs tail -s default
```

---

## 4. Cấu hình Database

### Nếu dùng PostgreSQL (thay vì MariaDB)

Cần thêm dependency vào `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

Và đổi driver trong `application.yml`:
```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
```

### Import data
```bash
# PostgreSQL
psql -h HOST -U USER -d DATABASE -f mariadb_init/01-schema.sql
psql -h HOST -U USER -d DATABASE -f mariadb_init/02-data.sql

# MySQL/MariaDB
mysql -h HOST -u USER -p DATABASE < mariadb_init/01-schema.sql
mysql -h HOST -u USER -p DATABASE < mariadb_init/02-data.sql
```

---

## 5. Cập nhật Google OAuth2 Redirect URI

Sau khi deploy, cần cập nhật redirect URI trong Google Cloud Console:

1. Vào [Google Cloud Console](https://console.cloud.google.com/apis/credentials)
2. Chọn OAuth 2.0 Client ID của bạn
3. Thêm Authorized redirect URIs:
   - Render: `https://your-app.onrender.com/login/oauth2/code/google`
   - Cloud Run: `https://your-app-xxxxx.run.app/login/oauth2/code/google`
   - App Engine: `https://YOUR_PROJECT_ID.appspot.com/login/oauth2/code/google`

---

## 6. So sánh các platform

| Feature | Render | Cloud Run | App Engine |
|---------|--------|-----------|------------|
| Free tier | ✅ (sleep sau 15p) | ✅ (2M requests/month) | ✅ (28h/day) |
| Cold start | ~30-60s | ~5-10s | ~5-10s |
| Auto-scaling | ✅ | ✅ | ✅ |
| Custom domain | ✅ | ✅ | ✅ |
| SSL | ✅ Free | ✅ Free | ✅ Free |
| Database | PostgreSQL | Cloud SQL | Cloud SQL |
| Dễ setup | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ |
| Chi phí | $7+/month | Pay-per-use | Pay-per-use |

**Khuyến nghị:**
- **Học tập/Demo**: Render Free
- **Production nhỏ**: Cloud Run
- **Production lớn**: App Engine hoặc GKE
