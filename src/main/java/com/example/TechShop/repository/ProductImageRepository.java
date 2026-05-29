package com.example.TechShop.repository;

import com.example.TechShop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId);

    long countByProductId(Long productId);
}
