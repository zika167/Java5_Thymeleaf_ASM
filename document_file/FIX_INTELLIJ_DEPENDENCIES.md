# Fix IntelliJ Dependencies Issue

## Problem
IntelliJ không nhận dependencies mới từ Maven (OAuth2, JWT, Dotenv)

## Solution - Thử theo thứ tự:

### Option 1: Reload Maven Project (Nhanh nhất)
1. Mở Maven panel: `View → Tool Windows → Maven`
2. Click icon "Reload All Maven Projects" (⟳) ở góc trên bên trái
3. Đợi Maven download dependencies
4. Build lại project: `Build → Rebuild Project`

### Option 2: Invalidate Caches (Nếu Option 1 không work)
1. `File → Invalidate Caches...`
2. Check tất cả options:
   - ✅ Clear file system cache and Local History
   - ✅ Clear downloaded shared indexes
   - ✅ Clear VCS Log caches and indexes
   - ✅ Invalidate and Restart
3. Click "Invalidate and Restart"
4. Sau khi restart, reload Maven project (Option 1)

### Option 3: Reimport Project (Nếu Option 2 không work)
1. Close project: `File → Close Project`
2. Xóa folder `.idea` trong project:
   ```bash
   rm -rf .idea
   ```
3. Reopen project: `File → Open` → chọn folder project
4. IntelliJ sẽ tự động import lại Maven project
5. Đợi indexing hoàn thành

### Option 4: Manual Maven Commands (Nếu tất cả fail)
```bash
# Clean và download lại dependencies
./mvnw clean install -U -DskipTests

# Sau đó trong IntelliJ:
# File → Invalidate Caches → Invalidate and Restart
```

## Verify Dependencies Downloaded

Check trong Maven panel xem các dependencies này đã có chưa:
- ✅ spring-boot-starter-oauth2-client
- ✅ jjwt-api (0.11.5)
- ✅ jjwt-impl (0.11.5)
- ✅ jjwt-jackson (0.11.5)
- ✅ dotenv-java (3.0.0)

## Expected Result

Sau khi fix, các import này sẽ không còn lỗi:
```java
import org.springframework.security.oauth2.core.user.OAuth2User;
import io.jsonwebtoken.*;
import io.github.cdimascio.dotenv.Dotenv;
```

## Note

Maven command line đã build thành công (BUILD SUCCESS), vấn đề chỉ là IntelliJ chưa sync với Maven.
