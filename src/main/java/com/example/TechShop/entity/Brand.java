package com.example.TechShop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 255)
    private String name;

    @Column(name = "logo_url",length = 255)
    private String logoUrl;

    @OneToMany(mappedBy = "brand",fetch = FetchType.LAZY)
    private List<Product> products;
}
