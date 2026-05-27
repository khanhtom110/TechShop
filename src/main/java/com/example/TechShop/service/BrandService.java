package com.example.TechShop.service;

import com.example.TechShop.dto.request.BrandRequest;
import com.example.TechShop.dto.response.BrandResponse;
import com.example.TechShop.entity.Brand;
import com.example.TechShop.exception.extended.DuplicateResourceException;
import com.example.TechShop.exception.extended.ResourceNotFoundException;
import com.example.TechShop.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public BrandResponse createBrand(BrandRequest request){
        if (brandRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Brand","name",request.name());
        }
        Brand brand=Brand.builder()
                .name(request.name())
                .logoUrl(request.logoUrl())
                .description(request.description())
                .build();

        return BrandResponse.from(brandRepository.save(brand));
    }

    @Transactional(readOnly = true)
    public Page<BrandResponse> findAllBrand(Pageable pageable,String keyword){
        if (keyword == null || keyword.isBlank()) {
            return brandRepository.findAll(pageable).map(BrandResponse::from);
        }
        return brandRepository.findByNameContainingIgnoreCase(keyword,pageable).map(BrandResponse::from);
    }

    @Transactional(readOnly = true)
    public BrandResponse findByBrandId(Long id){
        Brand brand=brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand","id",id));
        return BrandResponse.from(brand);
    }

//    @Transactional(readOnly = true)
//    public ProductResponse

    @Transactional
    public BrandResponse updateBrand(Long id, BrandRequest request){
        Brand brand=brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand","id",id));

        if (!brand.getName().equals(request.name()) && brandRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Brand","name",request.name());
        }

        brand.setName(request.name());
        brand.setLogoUrl(request.logoUrl());
        brand.setDescription(request.description());

        Brand savedBrand=brandRepository.save(brand);
        return BrandResponse.from(savedBrand);
    }

    @Transactional
    public void deleteBrand(Long id){
        Brand brand=brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand","id",id));
        brand.setStatus(false);
    }

    @Transactional
    public BrandResponse toggleBrandStatus(Long id){
        Brand brand=brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand","id",id));
        brand.setStatus(!brand.getStatus());
        return BrandResponse.from(brand);
    }
}
