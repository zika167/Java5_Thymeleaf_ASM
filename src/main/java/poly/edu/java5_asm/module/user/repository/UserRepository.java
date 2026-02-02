package poly.edu.java5_asm.module.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.module.user.entity.User;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository interface để thao tác với bảng users trong database.
 * <p>
 * Kế thừa JpaRepository để có sẵn các method CRUD cơ bản:
 * - save(), findById(), findAll(), delete(), count()...
 * <p>
 * Spring Data JPA sẽ tự động implement interface này tại runtime
 * dựa trên tên method (Query Method).
 *
 * @param User: Entity class được quản lý
 * @param Long: Kiểu dữ liệu của primary key (id)
 * @Repository: Đánh dấu đây là Spring Repository bean
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Tìm user theo username.
     * <p>
     * Spring Data JPA tự động generate query:
     * SELECT * FROM users WHERE username = ?
     *
     * @param username Tên đăng nhập cần tìm
     * @return Optional chứa User nếu tìm thấy, empty nếu không
     * <p>
     * Sử dụng trong: CustomUserDetailsService để load user khi đăng nhập
     */
    Optional<User> findByUsername(String username);

    /**
     * Tìm user theo email.
     * <p>
     * Spring Data JPA tự động generate query:
     * SELECT * FROM users WHERE email = ?
     *
     * @param email Địa chỉ email cần tìm
     * @return Optional chứa User nếu tìm thấy, empty nếu không
     * <p>
     * Sử dụng trong:
     * - CustomUserDetailsService: cho phép đăng nhập bằng email
     * - AuthService: kiểm tra email đã tồn tại khi đăng ký
     */
    Optional<User> findByEmail(String email);

    /**
     * Kiểm tra username đã tồn tại trong database chưa.
     * <p>
     * Spring Data JPA tự động generate query:
     * SELECT COUNT(*) > 0 FROM users WHERE username = ?
     *
     * @param username Tên đăng nhập cần kiểm tra
     * @return true nếu username đã tồn tại, false nếu chưa
     * <p>
     * Sử dụng trong: AuthService để validate khi đăng ký tài khoản mới
     */
    boolean existsByUsername(String username);

    /**
     * Kiểm tra email đã tồn tại trong database chưa.
     * <p>
     * Spring Data JPA tự động generate query:
     * SELECT COUNT(*) > 0 FROM users WHERE email = ?
     *
     * @param email Địa chỉ email cần kiểm tra
     * @return true nếu email đã tồn tại, false nếu chưa
     * <p>
     * Sử dụng trong: AuthService để validate khi đăng ký tài khoản mới
     */
    boolean existsByEmail(String email);

    /**
     * Đếm số người dùng đang hoạt động
     */
    Long countByIsActiveTrue();

    /**
     * Đếm số người đăng ký trong một ngày cụ thể
     */
    Long countByRegisteredDate(LocalDate date);

    /**
     * Đếm số người đăng ký trong khoảng thời gian
     */
    Long countByRegisteredDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Tìm user theo provider và providerId (cho OAuth2)
     *
     * @param provider Tên provider (google, facebook, etc.)
     * @param providerId ID từ provider
     * @return Optional chứa User nếu tìm thấy
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    /**
     * Tìm users theo role
     */
    Page<User> findByRole(User.Role role, Pageable pageable);

    /**
     * Tìm users theo username hoặc email (search)
     */
    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email, Pageable pageable);

    /**
     * Tìm users theo role và search
     */
    Page<User> findByRoleAndUsernameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
            User.Role role1, String username, User.Role role2, String email, Pageable pageable);
}
