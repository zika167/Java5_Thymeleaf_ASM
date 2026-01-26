package poly.edu.java5_asm.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.UserRepository;

/**
 * Service để load thông tin user từ database cho Spring Security.
 * <p>
 * Implement UserDetailsService interface - đây là cách Spring Security
 * biết cách lấy thông tin user để xác thực.
 * <p>
 * Service này được Spring Security tự động sử dụng khi:
 * - User submit form đăng nhập
 * - Cần verify thông tin user trong các request
 *
 * @Service: Đánh dấu là Spring Service bean
 * @RequiredArgsConstructor: Lombok tự động tạo constructor với các field final
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository để truy vấn user từ database
     */
    private final UserRepository userRepository;

    /**
     * Load thông tin user từ database dựa trên username hoặc email.
     * <p>
     * Method này được Spring Security gọi trong quá trình xác thực:
     * 1. User nhập username/email và password vào form đăng nhập
     * 2. Spring Security gọi loadUserByUsername() với giá trị user nhập
     * 3. Method này tìm user trong database
     * 4. Trả về UserDetails để Spring Security so sánh password
     * <p>
     * Hỗ trợ đăng nhập bằng cả username và email:
     * - Tìm theo username trước
     * - Nếu không thấy, tìm theo email
     * - Nếu vẫn không thấy, throw exception
     *
     * @param username Giá trị user nhập vào ô username (có thể là username hoặc email)
     * @return UserDetails object chứa thông tin user để xác thực
     * @throws UsernameNotFoundException Nếu không tìm thấy user với username/email đã cho
     *                                   <p>
     *                                   Lưu ý: Exception này sẽ được Spring Security catch và hiển thị thông báo
     *                                   "Bad credentials" trên trang đăng nhập (không tiết lộ user có tồn tại hay không)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user theo username, nếu không thấy thì tìm theo email
        // Sử dụng Optional.or() để chain 2 query một cách elegant
        User user = userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username))  // Fallback: tìm theo email
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Wrap User entity trong CustomUserDetails để Spring Security sử dụng
        return new CustomUserDetails(user);
    }
}
