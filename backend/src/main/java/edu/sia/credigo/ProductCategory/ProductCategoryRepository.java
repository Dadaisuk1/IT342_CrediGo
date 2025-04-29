package edu.sia.credigo.ProductCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
    boolean existsByCategoryname(String categoryname);
    List<ProductCategoryEntity> findByIsActiveTrue();
    List<ProductCategoryEntity> findByIsActiveFalse();
}