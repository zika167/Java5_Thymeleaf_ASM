package poly.edu.java5_asm.module.product.service;

import poly.edu.java5_asm.module.brand.dto.response.BrandResponse;
import poly.edu.java5_asm.module.category.dto.response.CategoryResponse;
import poly.edu.java5_asm.module.product.dto.request.ProductSearchRequest;
import poly.edu.java5_asm.module.product.dto.response.ProductListResponse;
import poly.edu.java5_asm.module.product.dto.response.ProductResponse;

import java.util.List;

/**
 * Interface cho Product Service
 * 
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║                 TẠI SAO CẦN INTERFACE SERVICE?                   ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║ 1. LOOSE COUPLING (Giảm phụ thuộc):                              ║
 * ║    - Controller chỉ biết interface, không biết implementation    ║
 * ║    - Có thể swap implementation mà không sửa controller          ║
 * ║                                                                  ║
 * ║ 2. TESTABILITY (Dễ test):                                        ║
 * ║    - Mock interface dễ dàng trong unit test                      ║
 * ║    - Không cần database thật khi test controller                 ║
 * ║                                                                  ║
 * ║ 3. FLEXIBILITY (Linh hoạt):                                      ║
 * ║    - Có thể có nhiều implementation: ProductServiceImpl,         ║
 * ║      ProductServiceCacheImpl, ProductServiceMockImpl             ║
 * ║                                                                  ║
 * ║ 4. DOCUMENTATION (Tài liệu):                                     ║
 * ║    - Interface như contract, định nghĩa rõ service làm gì        ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public interface ProductService {

    /**
     * Tìm kiếm và lọc sản phẩm
     * @param request Điều kiện tìm kiếm
     * @return Danh sách sản phẩm phân trang
     */
    ProductListResponse searchAndFilterProducts(ProductSearchRequest request);

    /**
     * Lấy chi tiết sản phẩm theo ID
     * @param id ID sản phẩm
     * @return Thông tin sản phẩm
     */
    ProductResponse getProductById(Long id);

    /**
     * Lấy tất cả sản phẩm có phân trang
     */
    ProductListResponse getAllProducts(int page, int size, String sortBy, String sortDirection);

    /**
     * Lấy sản phẩm nổi bật
     */
    ProductListResponse getFeaturedProducts(int page, int size);

    /**
     * Lấy sản phẩm mới nhất
     */
    ProductListResponse getLatestProducts(int page, int size);

    /**
     * Lấy sản phẩm bán chạy
     */
    ProductListResponse getBestSellingProducts(int page, int size);

    /**
     * Lấy danh sách categories
     */
    List<CategoryResponse> getAllCategories();

    /**
     * Lấy danh sách brands
     */
    List<BrandResponse> getAllBrands();
}
