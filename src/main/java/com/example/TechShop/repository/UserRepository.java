package com.example.TechShop.repository;

import com.example.TechShop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

//    @Query("select u from User u where lower(u.name) like concat('%',lower(:keyword),'%') ")
    Page<User> findByNameContainingIgnoreCase(String keyword,Pageable pageable);
}
