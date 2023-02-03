package com.hinesh.shopgenie.product.repository;

import com.hinesh.shopgenie.product.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}