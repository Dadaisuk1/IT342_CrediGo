package edu.sia.credigo.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sia.credigo.ProductCategory.CategoryEntity;
import edu.sia.credigo.ProductCategory.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public ProductEntity createProduct(ProductEntity product) {
        if (product.getSalePrice() != null && product.getSalePrice().compareTo(product.getPrice()) >= 0) {
            throw new RuntimeException("Sale price must be lower than the original price");
        }
        
        if (productRepository.existsByProductname(product.getProductname())) {
            throw new RuntimeException("Product with this name already exists");
        }
        CategoryEntity defaultCategory = categoryRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("Default category not found"));
        product.setCategory(defaultCategory);

        return productRepository.save(product);
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductEntity> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<ProductEntity> getProductsByCategory(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        return productRepository.findByCategory(category);
    }

    public List<ProductEntity> searchProducts(String productname) {
        return productRepository.findByProductnameContainingIgnoreCase(productname);
    }

    @Transactional
    public ProductEntity updateProduct(Long id, ProductEntity productDetails) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductname(productDetails.getProductname());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        product.setImageUrl(productDetails.getImageUrl());
        product.setIsActive(productDetails.getIsActive());
        product.setSalePrice(productDetails.getSalePrice());
        
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Transactional
    public ProductEntity deactivateProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsActive(false);
        return productRepository.save(product);
    }
}
