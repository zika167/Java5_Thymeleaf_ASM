package poly.edu.java5_asm.module.user.service;

import poly.edu.java5_asm.module.user.dto.request.ProfileUpdateRequest;
import poly.edu.java5_asm.module.user.entity.User;

/**
 * Interface cho User Service
 */
public interface UserService {

    User findByUsername(String username);

    User findById(Long id);

    User updateProfile(Long userId, ProfileUpdateRequest request);

    User updateAvatar(Long userId, String avatarUrl);
}
