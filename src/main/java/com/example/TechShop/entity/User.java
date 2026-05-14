package com.example.TechShop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, updatable = false, length = 120, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 120)
    private String password;

    @Column(name = "full_name", nullable = false, length = 250)
    private String name;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "status")
    @Builder.Default
    private Boolean status = Boolean.TRUE;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Cart cart;

}
