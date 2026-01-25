package poly.edu.java5_asm.service;

import poly.edu.java5_asm.dto.request.CreateAddressRequest;
import poly.edu.java5_asm.dto.response.AddressResponse;
import poly.edu.java5_asm.entity.User;

import java.util.List;

/**
 * Service interface quản lý địa chỉ giao hàng của user
 */
public interface AddressService {
    
    /**
     * Tạo địa chỉ mới
     * - Tối đa 5 địa chỉ per user
     * - Địa chỉ đầu tiên tự động set làm mặc định
     * - Nếu set isDefault=true, unset các địa chỉ khác
     */
    AddressResponse createAddress(User user, CreateAddressRequest request);
    
    /**
     * Cập nhật địa chỉ
     */
    AddressResponse updateAddress(User user, Long addressId, CreateAddressRequest request);
    
    /**
     * Xóa địa chỉ
     * - Nếu xóa địa chỉ mặc định, set địa chỉ đầu tiên làm mặc định
     */
    void deleteAddress(User user, Long addressId);
    
    /**
     * Set địa chỉ làm mặc định
     * - Unset các địa chỉ khác
     */
    AddressResponse setDefaultAddress(User user, Long addressId);
    
    /**
     * Lấy tất cả địa chỉ của user
     */
    List<AddressResponse> getUserAddresses(User user);
    
    /**
     * Lấy địa chỉ mặc định của user
     */
    AddressResponse getDefaultAddress(User user);
    
    /**
     * Lấy địa chỉ theo ID
     */
    AddressResponse getAddress(User user, Long addressId);
}
