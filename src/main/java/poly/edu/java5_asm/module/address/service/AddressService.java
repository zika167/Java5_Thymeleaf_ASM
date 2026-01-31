package poly.edu.java5_asm.module.address.service;

import poly.edu.java5_asm.module.address.dto.request.CreateAddressRequest;
import poly.edu.java5_asm.module.address.dto.response.AddressResponse;
import poly.edu.java5_asm.module.user.entity.User;

import java.util.List;

/**
 * Interface cho Address Service
 */
public interface AddressService {

    AddressResponse createAddress(User user, CreateAddressRequest request);

    AddressResponse updateAddress(User user, Long addressId, CreateAddressRequest request);

    void deleteAddress(User user, Long addressId);

    AddressResponse setDefaultAddress(User user, Long addressId);

    List<AddressResponse> getUserAddresses(User user);

    AddressResponse getDefaultAddress(User user);

    AddressResponse getAddress(User user, Long addressId);
}
