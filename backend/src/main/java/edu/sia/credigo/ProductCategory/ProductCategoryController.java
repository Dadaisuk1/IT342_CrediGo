package edu.sia.credigo.ProductCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Product Category", description = "Product Category management APIs")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/createCategory")
    public ResponseEntity<ProductCategoryEntity> createCategory(@RequestBody ProductCategoryEntity category) {
        return ResponseEntity.ok(productCategoryService.createCategory(category));
    }

    @Operation(summary = "Get all categories")
    @GetMapping("/getAllCategories")
    public ResponseEntity<List<ProductCategoryEntity>> getAllCategories() {
        return ResponseEntity.ok(productCategoryService.getAllCategories());
    }

    @Operation(summary = "Get all active categories")
    @GetMapping("/getActiveCategories")
    public ResponseEntity<List<ProductCategoryEntity>> getActiveCategories() {
        return ResponseEntity.ok(productCategoryService.getActiveCategories());
    }

    @Operation(summary = "Get all inactive categories")
    @GetMapping("/getInactiveCategories")
    public ResponseEntity<List<ProductCategoryEntity>> getInactiveCategories() {
        return ResponseEntity.ok(productCategoryService.getInactiveCategories());
    }

    @Operation(summary = "Get category by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("getCategoryById/{id}")
    public ResponseEntity<ProductCategoryEntity> getCategoryById(@PathVariable Long id) {
        return productCategoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<ProductCategoryEntity> updateCategory(
            @PathVariable Long id,
            @RequestBody ProductCategoryEntity categoryDetails) {
        return ResponseEntity.ok(productCategoryService.updateCategory(id, categoryDetails));
    }

    @Operation(summary = "Delete category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        productCategoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deactivate category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/deactivateCategory/{id}")
    public ResponseEntity<ProductCategoryEntity> deactivateCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productCategoryService.deactivateCategory(id));
    }
}