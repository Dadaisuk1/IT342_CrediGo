package edu.sia.credigo.Wishlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sia.credigo.Product.ProductEntity;
import edu.sia.credigo.Product.ProductRepository;
import edu.sia.credigo.User.UserEntity;
import edu.sia.credigo.User.UserRepository;
import edu.sia.credigo.Product.ProductResponse;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    

    @Transactional
    public WishlistEntity addToWishlist(Long userid, Long productid) {
        if (wishlistRepository.existsByUser_UseridAndProduct_Productid(userid, productid)) {
            throw new RuntimeException("Item already exists in wishlist");
        }

        UserEntity user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductEntity product = productRepository.findById(productid)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        WishlistEntity wishlist = WishlistEntity.builder()
                .user(user)
                .product(product)
                .build();

        return wishlistRepository.save(wishlist);
    }

    public List<WishlistEntity> getAllWishlistItems() {
        return wishlistRepository.findAll();
    }

    public List<WishlistResponse> getWishlistByUserId(Long userid) {
        List<WishlistEntity> wishlistEntities = wishlistRepository.findByUser_Userid(userid);
    
        return wishlistEntities.stream()
                .<WishlistResponse>map(entity -> WishlistResponse.builder()
                        .wishlistId(entity.getWishlistId())
                        .product(ProductResponse.builder()
                            .productid(entity.getProduct().getProductid())
                            .productname(entity.getProduct().getProductname())
                            .imageUrl(entity.getProduct().getImageUrl())
                            .price(entity.getProduct().getPrice().doubleValue())
                            .salePrice(entity.getProduct().getSalePrice().doubleValue())
                            .categoryname(entity.getProduct().getCategory() != null ? entity.getProduct().getCategory().getCategoryname() : null)
                            .build()
                        )
                        .build())
                .toList();
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
        Optional<WishlistEntity> wishlistItem = wishlistRepository.findByUser_UseridAndProduct_Productid(userid, productid);
        if (wishlistItem.isPresent()) {
            wishlistRepository.delete(wishlistItem.get());
            return true;
        }
        return false;
    }
}
