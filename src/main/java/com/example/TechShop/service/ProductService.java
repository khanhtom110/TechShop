package com.example.TechShop.service;

import com.example.TechShop.dto.request.ProductRequest;
import com.example.TechShop.dto.response.ProductResponse;
import com.example.TechShop.entity.Brand;
import com.example.TechShop.entity.Category;
import com.example.TechShop.entity.Product;
import com.example.TechShop.exception.extended.AppException;
import com.example.TechShop.exception.extended.DuplicateResourceException;
import com.example.TechShop.exception.extended.ResourceNotFoundException;
import com.example.TechShop.repository.BrandRepository;
import com.example.TechShop.repository.CategoryRepository;
import com.example.TechShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.categoryId()));

        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", request.brandId()));

        if (productRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Product", "name", request.name());
        }

        if (request.salePrice().compareTo(request.basePrice()) > 0) {
            throw new AppException(400, "Giá sale phải nhỏ hơn giá gốc");
        }

        //Chua bo sung productImages, productVariants

        Product product = Product.builder()
                .category(category)
                .brand(brand)
                .name(request.name())
                .basePrice(request.basePrice())
                .salePrice(request.salePrice())
                .specifications(request.specifications())
                .description(request.description())
                .build();

        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllProduct(Pageable pageable, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return productRepository.findAll(pageable).map(ProductResponse::from);
        }
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable)
                .map(ProductResponse::from);
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.categoryId()));

        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", request.brandId()));

        if (!product.getName().equals(request.name()) && productRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Product", "name", request.name());
        }

        if (product.getSalePrice().compareTo(request.salePrice()) != 0 ||
                product.getBasePrice().compareTo(request.basePrice()) != 0) {
            if (request.salePrice().compareTo(request.basePrice()) > 0) {
                throw new AppException(400, "Giá sale phải nhỏ hơn giá gốc");
            }
        }

        product.setCategory(category);
        product.setBrand(brand);
        product.setName(request.name());
        product.setBasePrice(request.basePrice());
        product.setSalePrice(request.salePrice());
        product.setSpecifications(request.specifications());
        product.setDescription(request.description());

        return ProductResponse.from(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setStatus(false);
    }

    @Transactional
    public ProductResponse toggleProductStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setStatus(!product.getStatus());
        return ProductResponse.from(product);
    }
}
