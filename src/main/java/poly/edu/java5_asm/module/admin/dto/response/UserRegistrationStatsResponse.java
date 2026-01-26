package poly.edu.java5_asm.dto.response;

import lombok.*;

import java.time.LocalDate;

/**
 * DTO Response cho thống kê đăng ký người dùng
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationStatsResponse {

    private LocalDate date;           // Ngày
    private Long registrationCount;   // Số người đăng ký trong ngày
}
