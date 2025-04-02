package edu.sia.credigo.ProductCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping("/createCategory")
    public ResponseEntity<ProductCategoryEntity> createCategory(@RequestBody ProductCategoryEntity category) {
        return ResponseEntity.ok(productCategoryService.createCategory(category));
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<ProductCategoryEntity>> getAllCategories() {
        return ResponseEntity.ok(productCategoryService.getAllCategories());
    }

    @GetMapping("/getActiveCategories")
    public ResponseEntity<List<ProductCategoryEntity>> getActiveCategories() {
        return ResponseEntity.ok(productCategoryService.getActiveCategories());
    }

    @GetMapping("/getInactiveCategories")
    public ResponseEntity<List<ProductCategoryEntity>> getInactiveCategories() {
        return ResponseEntity.ok(productCategoryService.getInactiveCategories());
    }

    @GetMapping("getCategoryById/{id}")
    public ResponseEntity<ProductCategoryEntity> getCategoryById(@PathVariable Long id) {
        return productCategoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<ProductCategoryEntity> updateCategory(
            @PathVariable Long id,
            @RequestBody ProductCategoryEntity categoryDetails) {
        return ResponseEntity.ok(productCategoryService.updateCategory(id, categoryDetails));
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        productCategoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deactivateCategory/{id}")
    public ResponseEntity<ProductCategoryEntity> deactivateCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productCategoryService.deactivateCategory(id));
    }
}
