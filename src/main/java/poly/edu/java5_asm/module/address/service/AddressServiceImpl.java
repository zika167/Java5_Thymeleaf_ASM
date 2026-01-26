package poly.edu.java5_asm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.request.CreateAddressRequest;
import poly.edu.java5_asm.dto.response.AddressResponse;
import poly.edu.java5_asm.entity.Address;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.AddressRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý logic quản lý địa chỉ giao hàng của user
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl {

    private final AddressRepository addressRepository;

    /**
     * Tạo địa chỉ mới
     * - Tối đa 5 địa chỉ per user
     * - Địa chỉ đầu tiên tự động set làm mặc định
     * - Nếu set isDefault=true, unset các địa chỉ khác
     */
    @Transactional
    public AddressResponse createAddress(User user, CreateAddressRequest request) {
        // Kiểm tra số lượng địa chỉ
        Long addressCount = addressRepository.countByUser(user);
        if (addressCount >= 5) {
            throw new RuntimeException("Bạn đã đạt giới hạn 5 địa chỉ. Vui lòng xóa một địa chỉ trước khi thêm mới");
        }

        // Nếu đây là địa chỉ đầu tiên, tự động set làm mặc định
        Boolean isDefault = request.getIsDefault();
        if (addressCount == 0) {
            isDefault = true;
        }

        // Nếu set isDefault=true, unset các địa chỉ khác
        if (isDefault != null && isDefault) {
            addressRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(existingDefault -> {
                        existingDefault.setIsDefault(false);
                        addressRepository.save(existingDefault);
                    });
        }

        // Tạo địa chỉ mới
        Address address = Address.builder()
                .user(user)
                .recipientName(request.getRecipientName())
                .phone(request.getPhone())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry() != null ? request.getCountry() : "Vietnam")
                .isDefault(isDefault)
                .build();

        address = addressRepository.save(address);
        log.info("Tạo địa chỉ mới cho user {}: {}", user.getId(), address.getId());

        return convertToResponse(address);
    }

    /**
     * Cập nhật địa chỉ
     */
    @Transactional
    public AddressResponse updateAddress(User user, Long addressId, CreateAddressRequest request) {
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại hoặc bạn không có quyền cập nhật"));

        // Nếu set isDefault=true, unset các địa chỉ khác
        if (request.getIsDefault() != null && request.getIsDefault()) {
            addressRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(existingDefault -> {
                        if (!existingDefault.getId().equals(addressId)) {
                            existingDefault.setIsDefault(false);
                            addressRepository.save(existingDefault);
                        }
                    });
            address.setIsDefault(true);
        }

        // Cập nhật thông tin
        address.setRecipientName(request.getRecipientName());
        address.setPhone(request.getPhone());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }

        address = addressRepository.save(address);
        log.info("Cập nhật địa chỉ {} của user {}", addressId, user.getId());

        return convertToResponse(address);
    }

    /**
     * Xóa địa chỉ
     * - Nếu xóa địa chỉ mặc định, set địa chỉ đầu tiên làm mặc định
     */
    @Transactional
    public void deleteAddress(User user, Long addressId) {
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại hoặc bạn không có quyền xóa"));

        boolean wasDefault = address.getIsDefault() != null && address.getIsDefault();
        addressRepository.delete(address);
        log.info("Xóa địa chỉ {} của user {}", addressId, user.getId());

        // Nếu xóa địa chỉ mặc định, set địa chỉ đầu tiên làm mặc định
        if (wasDefault) {
            List<Address> remainingAddresses = addressRepository.findByUserOrderByIsDefaultDescCreatedAtDesc(user);
            if (!remainingAddresses.isEmpty()) {
                Address newDefault = remainingAddresses.get(0);
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
                log.info("Set địa chỉ {} làm mặc định cho user {}", newDefault.getId(), user.getId());
            }
        }
    }

    /**
     * Set địa chỉ làm mặc định
     * - Unset các địa chỉ khác
     */
    @Transactional
    public AddressResponse setDefaultAddress(User user, Long addressId) {
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại hoặc bạn không có quyền cập nhật"));

        // Unset các địa chỉ khác
        addressRepository.findByUserAndIsDefaultTrue(user)
                .ifPresent(existingDefault -> {
                    if (!existingDefault.getId().equals(addressId)) {
                        existingDefault.setIsDefault(false);
                        addressRepository.save(existingDefault);
                    }
                });

        // Set địa chỉ này làm mặc định
        address.setIsDefault(true);
        address = addressRepository.save(address);
        log.info("Set địa chỉ {} làm mặc định cho user {}", addressId, user.getId());

        return convertToResponse(address);
    }

    /**
     * Lấy tất cả địa chỉ của user
     */
    @Transactional(readOnly = true)
    public List<AddressResponse> getUserAddresses(User user) {
        List<Address> addresses = addressRepository.findByUserOrderByIsDefaultDescCreatedAtDesc(user);
        return addresses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy địa chỉ mặc định của user
     */
    @Transactional(readOnly = true)
    public AddressResponse getDefaultAddress(User user) {
        Address address = addressRepository.findByUserAndIsDefaultTrue(user)
                .orElseThrow(() -> new RuntimeException("Bạn chưa có địa chỉ mặc định"));
        return convertToResponse(address);
    }

    /**
     * Lấy địa chỉ theo ID
     */
    @Transactional(readOnly = true)
    public AddressResponse getAddress(User user, Long addressId) {
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại hoặc bạn không có quyền xem"));
        return convertToResponse(address);
    }

    /**
     * Convert Address entity thành AddressResponse
     */
    private AddressResponse convertToResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .recipientName(address.getRecipientName())
                .phone(address.getPhone())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreatedAt())
                .build();
    }
}
