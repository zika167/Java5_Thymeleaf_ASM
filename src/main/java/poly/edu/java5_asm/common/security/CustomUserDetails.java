package poly.edu.java5_asm.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import poly.edu.java5_asm.entity.User;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation của UserDetails interface.
 * Wrap entity User để Spring Security có thể sử dụng trong quá trình xác thực.
 * 
 * UserDetails là interface cốt lõi của Spring Security, cung cấp thông tin
 * cần thiết để xây dựng Authentication object.
 */
@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    /**
     * Entity User được wrap bên trong để truy cập thông tin người dùng
     */
    private final User user;

    /**
     * Trả về ID của user.
     * Helper method để dễ dàng truy cập user ID.
     * 
     * @return ID của user
     */
    public Long getUserId() {
        return user.getId();
    }

    /**
     * Trả về danh sách quyền (authorities) của user.
     * Prefix "ROLE_" được thêm vào để Spring Security nhận diện đây là role.
     * Ví dụ: User.Role.ADMIN -> "ROLE_ADMIN"
     * 
     * @return Collection các GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Trả về password đã được mã hóa của user.
     * Spring Security sẽ so sánh với password người dùng nhập vào.
     * 
     * @return Password đã mã hóa
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Trả về username dùng để đăng nhập.
     * 
     * @return Username của user
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Kiểm tra tài khoản có hết hạn không.
     * Trả về true = tài khoản còn hiệu lực.
     * 
     * @return true nếu tài khoản chưa hết hạn
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Kiểm tra tài khoản có bị khóa không.
     * Dựa vào trường isActive của User entity.
     * 
     * @return true nếu tài khoản không bị khóa
     */
    @Override
    public boolean isAccountNonLocked() {
        return user.getIsActive();
    }

    /**
     * Kiểm tra credentials (password) có hết hạn không.
     * Trả về true = credentials còn hiệu lực.
     * 
     * @return true nếu credentials chưa hết hạn
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Kiểm tra tài khoản có được kích hoạt không.
     * Dựa vào trường isActive của User entity.
     * 
     * @return true nếu tài khoản đã được kích hoạt
     */
    @Override
    public boolean isEnabled() {
        return user.getIsActive();
    }
}
