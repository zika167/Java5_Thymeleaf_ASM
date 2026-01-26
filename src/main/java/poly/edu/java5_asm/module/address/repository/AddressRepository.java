package poly.edu.java5_asm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.java5_asm.entity.Address;
import poly.edu.java5_asm.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // Tìm tất cả địa chỉ của user
    List<Address> findByUserOrderByIsDefaultDescCreatedAtDesc(User user);

    // Tìm địa chỉ mặc định của user
    Optional<Address> findByUserAndIsDefaultTrue(User user);

    // Đếm số địa chỉ của user
    Long countByUser(User user);

    // Tìm địa chỉ theo ID và user (để kiểm tra quyền sở hữu)
    Optional<Address> findByIdAndUser(Long id, User user);

    // Xóa tất cả địa chỉ của user
    void deleteByUser(User user);
}
