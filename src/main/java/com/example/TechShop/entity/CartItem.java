package com.example.TechShop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    //Nut bam tren man hinh
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    //Khong luu vao database
    @Transient
    public BigDecimal getSubTotal() {
        if (this.productVariant != null && this.productVariant.getPrice() != null && this.quantity != null) {
            // Lay gia nhan so luon
            return this.productVariant.getPrice().multiply(new BigDecimal(this.quantity));
        }
        return BigDecimal.ZERO;
    }
}
