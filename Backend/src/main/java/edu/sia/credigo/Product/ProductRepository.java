package edu.sia.credigo.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategory(String category);
    List<ProductEntity> findByIsActiveTrue();
    List<ProductEntity> findByProductnameContainingIgnoreCase(String productname);
    boolean existsByProductname(String productname);
}
