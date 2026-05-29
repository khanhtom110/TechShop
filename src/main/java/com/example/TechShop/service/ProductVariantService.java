package com.example.TechShop.service;

import com.example.TechShop.dto.request.ProductVariantRequest;
import com.example.TechShop.entity.Product;
import com.example.TechShop.entity.ProductVariant;
import com.example.TechShop.exception.extended.AppException;
import com.example.TechShop.exception.extended.ResourceNotFoundException;
import com.example.TechShop.repository.ProductRepository;
import com.example.TechShop.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createProductVariant(ProductVariantRequest request){
        Product product=productRepository.findById(request.productId())
                .orElseThrow(()->new ResourceNotFoundException("Product","id",request.productId()));

        //check trung Bien the
        List<ProductVariant> existingVariant=productVariantRepository.findByProductIdAndStatusTrue(product.getId());
        boolean isDuplicate=existingVariant.stream()
                .anyMatch(variant ->
                        variant.getAttributes().equals(request.attributes()));
        if(isDuplicate){
            throw new AppException(400, "Biến thể với cấu hình thuộc tính này đã tồn tại cho sản phẩm!");
        }

        ProductVariant variant=ProductVariant.builder()
                .product(product)
                .attributes(request.attributes())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .build();

        product.addVariant(variant);
    }

    @Transactional
    public void deleteProductVariant(Long id){
        ProductVariant variant=productVariantRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("ProductVariant","id",id));
        variant.setStatus(false);
    }
}
