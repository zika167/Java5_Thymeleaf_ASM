package poly.edu.java5_asm.module.auth.service;

import poly.edu.java5_asm.module.auth.dto.request.RegisterRequest;
import poly.edu.java5_asm.module.user.entity.User;

/**
 * Interface cho Auth Service
 */
public interface AuthService {

    User register(RegisterRequest request);

    void updateLastLogin(String username);
}
