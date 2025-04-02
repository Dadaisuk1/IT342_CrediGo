package edu.sia.credigo.ProductCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public ProductCategoryEntity createCategory(ProductCategoryEntity category) {
        if (productCategoryRepository.existsByCategoryname(category.getCategoryname())) {
            throw new RuntimeException("Category with this name already exists");
        }
        return productCategoryRepository.save(category);
    }

    public List<ProductCategoryEntity> getAllCategories() {
        return productCategoryRepository.findAll();
    }

    public Optional<ProductCategoryEntity> getCategoryById(Long id) {
        return productCategoryRepository.findById(id);
    }

    public List<ProductCategoryEntity> getActiveCategories() {
        return productCategoryRepository.findByIsActiveTrue();
    }

    public List<ProductCategoryEntity> getInactiveCategories() {
        return productCategoryRepository.findByIsActiveFalse();
    }

    @Transactional
    public ProductCategoryEntity updateCategory(Long id, ProductCategoryEntity categoryDetails) {
        ProductCategoryEntity category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setCategoryname(categoryDetails.getCategoryname());
        category.setDescription(categoryDetails.getDescription());
        category.setIsActive(categoryDetails.getIsActive());
        category.setImage(categoryDetails.getImage());

        return productCategoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        ProductCategoryEntity category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        productCategoryRepository.delete(category);
    }

    @Transactional
    public ProductCategoryEntity deactivateCategory(Long id) {
        ProductCategoryEntity category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setIsActive(false);
        return productCategoryRepository.save(category);
    }
}
