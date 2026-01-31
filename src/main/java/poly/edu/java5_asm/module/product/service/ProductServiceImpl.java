package poly.edu.java5_asm.module.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.common.constant.CacheNames;
import poly.edu.java5_asm.common.constant.ErrorMessages;
import poly.edu.java5_asm.common.exception.ProductNotFoundException;
import poly.edu.java5_asm.module.product.dto.request.ProductSearchRequest;
import poly.edu.java5_asm.module.brand.dto.response.BrandResponse;
import poly.edu.java5_asm.module.category.dto.response.CategoryResponse;
import poly.edu.java5_asm.module.product.dto.response.ProductListResponse;
import poly.edu.java5_asm.module.product.dto.response.ProductResponse;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.category.entity.Category;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.brand.repository.BrandRepository;
import poly.edu.java5_asm.module.category.repository.CategoryRepository;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.common.util.ProductMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation của ProductService
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductListResponse searchAndFilterProducts(ProductSearchRequest request) {
        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(request.getSortDirection())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                request.getSortBy()
        );
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Product> productPage = productRepository.searchAndFilter(
                request.getKeyword(),
                request.getCategoryId(),
                request.getBrandId(),
                request.getMinPrice(),
                request.getMaxPrice(),
                pageable
        );

        return productMapper.toProductListResponse(productPage);
    }

    @Override
    @Cacheable(value = CacheNames.PRODUCT_BY_ID, key = "#id")
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND));

        return productMapper.toResponse(product);
    }

    @Override
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

    @Override
    @Cacheable(value = CacheNames.FEATURED_PRODUCTS, key = "'page_' + #page + '_size_' + #size")
    public ProductListResponse getFeaturedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByIsFeaturedTrueAndIsActiveTrue(pageable);
        return productMapper.toProductListResponse(productPage);
    }

    @Override
    public ProductListResponse getLatestProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findLatestProducts(pageable);
        return productMapper.toProductListResponse(productPage);
    }

    @Override
    public ProductListResponse getBestSellingProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findBestSellingProducts(pageable);
        return productMapper.toProductListResponse(productPage);
    }

    @Override
    @Cacheable(value = CacheNames.CATEGORIES)
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        
        Map<Long, Long> productCountMap = productRepository.countProductsByAllCategories()
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));

        return categories.stream()
                .map(category -> {
                    Long productCount = productCountMap.getOrDefault(category.getId(), 0L);
                    return productMapper.toCategoryResponse(category, productCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CacheNames.BRANDS)
    public List<BrandResponse> getAllBrands() {
        List<Brand> brands = brandRepository.findByIsActiveTrueOrderByNameAsc();
        
        Map<Long, Long> productCountMap = productRepository.countProductsByAllBrands()
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));

        return brands.stream()
                .map(brand -> {
                    Long productCount = productCountMap.getOrDefault(brand.getId(), 0L);
                    return productMapper.toBrandResponse(brand, productCount);
                })
                .collect(Collectors.toList());
    }
}
