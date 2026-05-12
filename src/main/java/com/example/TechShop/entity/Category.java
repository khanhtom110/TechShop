package com.example.TechShop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory",fetch = FetchType.LAZY)
    private List<Category> subCategories;

    @Column(nullable = false,length = 255)
    private String name;

    @Column(nullable = false)
    @Builder.Default
    private Boolean status=Boolean.TRUE;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    private List<Product> products;
}
