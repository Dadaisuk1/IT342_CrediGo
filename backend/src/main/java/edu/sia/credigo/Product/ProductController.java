package edu.sia.credigo.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
 
    @Autowired
    private ProductService productService;
    


     // ADMIN ONLY - Manage All Products
     @PreAuthorize("hasAuthority('ROLE_ADMIN')")
     @GetMapping("/admin/getAllProducts")
     public ResponseEntity<List<ProductEntity>> getAllProducts() {
         return ResponseEntity.ok(productService.getAllProducts());
     }


     // PUBLIC - Customers View Active Products
    @GetMapping("/getActiveProducts")
    public ResponseEntity<List<ProductEntity>> getActiveProducts() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
     } 


    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/createProduct")
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product) {
        System.out.println("✅ Inside createProduct: authorized admin");

        System.out.println("🔐 Spring Security Context Auth: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("🔐 Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println("🔐 Principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok(productService.createProduct(product));
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("getAllProducts")
    public ResponseEntity<List<ProductEntity>> getAllProducts(
            @RequestParam(required = false) Boolean activeOnly) {
        System.out.println("✅ Inside getAllProducts - Access granted");

        if (Boolean.TRUE.equals(activeOnly)) {
            return ResponseEntity.ok(productService.getAllActiveProducts());
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductEntity>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProductEntity>> searchProducts(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProducts(name));
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductEntity> updateProduct(
            @PathVariable Long id, 
            @RequestBody ProductEntity productDetails) {
        return ResponseEntity.ok(productService.updateProduct(id, productDetails));
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<ProductEntity> deactivateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deactivateProduct(id));
    }


}
