package edu.sia.credigo.Wishlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    public WishlistEntity addToWishlist(WishlistEntity wishlist) {
        // Check if the item is already in the wishlist
        if (wishlistRepository.existsByUser_UseridAndProduct_Productid(
                wishlist.getUser().getId(),
                wishlist.getProduct().getProductid())) {
            throw new RuntimeException("Item already exists in wishlist");
        }
        return wishlistRepository.save(wishlist);
    }

    public List<WishlistEntity> getAllWishlistItems() {
        return wishlistRepository.findAll();
    }

    public List<WishlistEntity> getWishlistByUserId(Long userid) {
        return wishlistRepository.findByUser_Userid(userid);
    }

    public Optional<WishlistEntity> getWishlistItemById(Long id) {
        return wishlistRepository.findById(id);
    }

    public Optional<WishlistEntity> getWishlistItemByUserAndProduct(Long userid, Long productid) {
        return wishlistRepository.findByUser_UseridAndProduct_Productid(userid, productid);
    }

    public boolean removeFromWishlist(Long id) {
        if (wishlistRepository.existsById(id)) {
            wishlistRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean removeFromWishlistByUserAndProduct(Long userid, Long productid) {
        Optional<WishlistEntity> wishlistItem = wishlistRepository.findByUser_UseridAndProduct_Productid(userid,
                productid);
        if (wishlistItem.isPresent()) {
            wishlistRepository.delete(wishlistItem.get());
            return true;
        }
        return false;
    }
}
