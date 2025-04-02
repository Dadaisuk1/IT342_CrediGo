package edu.sia.credigo.Wishlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<WishlistEntity> addToWishlist(@RequestBody WishlistEntity wishlist) {
        try {
            WishlistEntity newWishlistItem = wishlistService.addToWishlist(wishlist);
            return ResponseEntity.ok(newWishlistItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<WishlistEntity>> getAllWishlistItems() {
        List<WishlistEntity> wishlistItems = wishlistService.getAllWishlistItems();
        return ResponseEntity.ok(wishlistItems);
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<WishlistEntity>> getWishlistByUserId(@PathVariable Long userid) {
        List<WishlistEntity> wishlistItems = wishlistService.getWishlistByUserId(userid);
        return ResponseEntity.ok(wishlistItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishlistEntity> getWishlistItemById(@PathVariable Long id) {
        return wishlistService.getWishlistItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userid}/product/{productid}")
    public ResponseEntity<WishlistEntity> getWishlistItemByUserAndProduct(
            @PathVariable Long userid,
            @PathVariable Long productid) {
        return wishlistService.getWishlistItemByUserAndProduct(userid, productid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long id) {
        if (wishlistService.removeFromWishlist(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/{userid}/product/{productid}")
    public ResponseEntity<Void> removeFromWishlistByUserAndProduct(
            @PathVariable Long userid,
            @PathVariable Long productid) {
        if (wishlistService.removeFromWishlistByUserAndProduct(userid, productid)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
