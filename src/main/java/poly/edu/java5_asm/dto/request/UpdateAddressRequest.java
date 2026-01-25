package poly.edu.java5_asm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAddressRequest {
    
    @NotBlank(message = "Tên người nhận không được để trống")
    private String recipientName;
    
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải từ 10-11 chữ số")
    private String phone;
    
    @NotBlank(message = "Địa chỉ không được để trống")
    private String addressLine1;
    
    private String addressLine2;
    
    @NotBlank(message = "Thành phố không được để trống")
    private String city;
    
    private String state;
    
    private String postalCode;
    
    private String country;
    
    private Boolean isDefault;
}
kage poly.edu.java5_asm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    
    private Long id;
    
    private String recipientName;
    
    private String phone;
    
    private String addressLine1;
    
    private String addressLine2;
    
    private String city;
    
    private String state;
    
    private String postalCode;
    
    private String country;
    
    private Boolean isDefault;
    
    private LocalDateTime createdAt;
}
