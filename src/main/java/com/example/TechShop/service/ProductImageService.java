package com.example.TechShop.service;

import com.example.TechShop.dto.request.ProductImageRequest;
import com.example.TechShop.entity.Product;
import com.example.TechShop.entity.ProductImage;
import com.example.TechShop.exception.extended.AppException;
import com.example.TechShop.exception.extended.ResourceNotFoundException;
import com.example.TechShop.repository.ProductImageRepository;
import com.example.TechShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    //Dem xem co qua so luong anh hay khong
    @Transactional
    public void createProductImage(ProductImageRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.productId()));

        long count = productImageRepository.countByProductId(product.getId());
        if (count >= 8) {
            throw new AppException(400, "Mỗi sản phẩm có tối đa 8 ảnh");
        }

        if (request.isPrimary()) {
            productImageRepository.findByProductIdAndIsPrimaryTrue(product.getId())
                    .ifPresent(oldImagePrimary ->
                            oldImagePrimary.setIsPrimary(false));
        }

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(request.imageUrl())
                .displayIndex(request.displayIndex())
                .isPrimary(request.isPrimary())
                .build();

        product.addImage(image);
    }

    @Transactional
    public void setPrimaryImage(Long productId, Long newImageId) {
        productImageRepository.findByProductIdAndIsPrimaryTrue(productId)
                .ifPresent(oldImagePrimary ->
                        oldImagePrimary.setIsPrimary(false));
        ProductImage image = productImageRepository.findById(newImageId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", newImageId));
        image.setIsPrimary(true);
    }

    //Xoa anh
    //Truong hop dac biet: Xoa anh dai dien -> throw ra AppException
    @Transactional
    public void deleteProductImage(Long id) {
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));

        if(image.getIsPrimary()){
            throw new AppException(400,"Không thể xóa ảnh đại diện. Vui lòng chọn ảnh đại diện khác trước khi xóa!");
        }

        Product product = image.getProduct();
        if (product != null) {
            product.removeImage(image);
        }

        productImageRepository.delete(image);
    }

}
