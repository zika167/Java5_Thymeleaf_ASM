package poly.edu.java5_asm.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Cấu hình OpenAPI/Swagger cho API Documentation
 * Truy cập: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server().url(baseUrl).description("Current Server"),
                        new Server().url("http://localhost:8080").description("Local Development")
                ))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Nhập JWT token (không cần prefix 'Bearer ')")
                        )
                        .addSecuritySchemes("Cookie", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JWT_TOKEN")
                                .description("JWT token trong HTTP-Only Cookie")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("JWT"));
    }

    private Info apiInfo() {
        return new Info()
                .title("Fat-C Grocery Store API")
                .version("1.0.0")
                .description("""
                        ## API Documentation cho Fat-C Grocery Store
                        
                        ### Tính năng chính:
                        - 🛒 **Cart**: Quản lý giỏ hàng
                        - 📦 **Order**: Đặt hàng và theo dõi đơn hàng
                        - 🛍️ **Product**: Danh sách sản phẩm
                        - ⭐ **Review**: Đánh giá sản phẩm
                        - ❤️ **Wishlist**: Danh sách yêu thích
                        - 👤 **User**: Quản lý tài khoản
                        - 🔐 **Auth**: Đăng nhập/Đăng ký
                        
                        ### Authentication:
                        - JWT Token qua Cookie (tự động sau khi login)
                        - Hoặc Bearer Token trong header
                        
                        ### Response Codes:
                        - `200` - Thành công
                        - `201` - Tạo mới thành công
                        - `400` - Bad Request (validation error)
                        - `401` - Unauthorized (chưa đăng nhập)
                        - `403` - Forbidden (không có quyền)
                        - `404` - Not Found
                        - `500` - Server Error
                        """)
                .contact(new Contact()
                        .name("Fat-C Grocery Store")
                        .email("support@fatc-grocery.com")
                        .url("https://github.com/zika167/Java5_Thymeleaf_ASM"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }
}
