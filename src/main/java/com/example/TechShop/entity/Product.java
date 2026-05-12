package com.example.TechShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "product",indexes = {
        @Index(name = "idx_category",columnList = "category_id"),
        @Index(name = "idx_brand",columnList = "brand_id"),
        @Index(name = "idx_price",columnList = "base_price")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(nullable = false,length = 255)
    private String name;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductImage> productImages;

    @Column(name = "base_price",precision = 38,scale = 2)
    private BigDecimal basePrice;

    @Column(name = "sale_price",precision = 38,scale = 2)
    private BigDecimal salePrice;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "specifications",columnDefinition = "json")
    private Map<String,Object> specifications;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductVariant> productVariants;

    @Column(nullable = false)
    @Builder.Default
    private Boolean status=Boolean.TRUE;

    @Column(length = 1000)
    private String description;
}
