package poly.edu.java5_asm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.request.ProductSearchRequest;
import poly.edu.java5_asm.dto.response.BrandResponse;
import poly.edu.java5_asm.dto.response.CategoryResponse;
import poly.edu.java5_asm.dto.response.ProductListResponse;
import poly.edu.java5_asm.dto.response.ProductResponse;
import poly.edu.java5_asm.entity.Brand;
import poly.edu.java5_asm.entity.Category;
import poly.edu.java5_asm.entity.Product;
import poly.edu.java5_asm.repository.BrandRepository;
import poly.edu.java5_asm.repository.CategoryRepository;
import poly.edu.java5_asm.repository.ProductRepository;
import poly.edu.java5_asm.util.ProductMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý logic nghiệp vụ cho Product
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;

    /**
     * Tìm kiếm và lọc sản phẩm
     */
    public ProductListResponse searchAndFilterProducts(ProductSearchRequest request) {
        // Tạo Pageable với sắp xếp
        Sort sort = Sort.by(
            "DESC".equalsIgnoreCase(request.getSortDirection()) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC,
            request.getSortBy()
        );
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // Gọi repository tìm kiếm
        Page<Product> productPage = productRepository.searchAndFilter(
            request.getKeyword(),
            request.getCategoryId(),
            request.getBrandId(),
            request.getMinPrice(),
            request.getMaxPrice(),
            pageable
        );

        // Chuyển đổi sang DTO
        return productMapper.toProductListResponse(productPage);
    }

    /**
     * Lấy chi tiết sản phẩm theo ID
     */
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
        
        return productMapper.toResponse(product);
    }

    /**
     * Lấy tất cả sản phẩm có phân trang
     */
    public ProductListResponse getAllProducts(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(
            "DESC".equalsIgnoreCase(sortDirection) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC,
            sortBy
        );
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Product> productPage = productRepository.findByIsActiveTrue(pageable);
        return productMapper.toProductListResponse(productPage);
    }

    /**
     * Lấy sản phẩm nổi bật
     */
    public ProductListResponse getFeaturedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByIsFeaturedTrueAndIsActiveTrue(pageable);
        return productMapper.toProductListResponse(productPage);
    }

    /**
     * Lấy sản phẩm mới nhất
     */
    public ProductListResponse getLatestProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findLatestProducts(pageable);
        return productMapper.toProductListResponse(productPage);
    }

    /**
     * Lấy sản phẩm bán chạy
     */
    public ProductListResponse getBestSellingProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findBestSellingProducts(pageable);
        return productMapper.toProductListResponse(productPage);
    }

    /**
     * Lấy danh sách categories
     */
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        
        return categories.stream()
            .map(category -> {
                Long productCount = productRepository.countByCategoryId(category.getId());
                return productMapper.toCategoryResponse(category, productCount);
            })
            .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách brands
     */
    public List<BrandResponse> getAllBrands() {
        List<Brand> brands = brandRepository.findByIsActiveTrueOrderByNameAsc();
        
        return brands.stream()
            .map(brand -> {
                Long productCount = productRepository.countByBrandId(brand.getId());
                return productMapper.toBrandResponse(brand, productCount);
            })
            .collect(Collectors.toList());
    }
}
