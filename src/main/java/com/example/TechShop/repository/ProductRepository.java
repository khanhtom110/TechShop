package com.example.TechShop.repository;

import com.example.TechShop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);

    Page<Product> findByNameContainingIgnoreCase(String keyword,
                                                 Pageable pageable);
}
