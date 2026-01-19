package poly.edu.java5_asm.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
