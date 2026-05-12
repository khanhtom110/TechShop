package com.example.TechShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "product_variant",indexes = {
        @Index(name = "idx_variant_product",columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes",columnDefinition = "json",nullable = false)
    private Map<String,String> attributes;

    @Column(precision = 38,scale = 2,nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity",nullable = false)
    private Integer stockQuantity;
}
