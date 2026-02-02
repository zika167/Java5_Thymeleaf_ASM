package poly.edu.java5_asm.module.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;

    private Long productId;

    private Long userId;

    private String userName;

    private String userAvatar;

    private Integer rating;

    private String title;

    private String comment;

    private Boolean isVerifiedPurchase;

    private LocalDateTime createdAt;
}
