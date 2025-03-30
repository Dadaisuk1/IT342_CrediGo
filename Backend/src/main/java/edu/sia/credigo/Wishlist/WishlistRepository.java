package edu.sia.credigo.Wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {
    List<WishlistEntity> findByUser_Userid(Long userid);
    Optional<WishlistEntity> findByUser_UseridAndProduct_Productid(Long userid, Long productid);
    boolean existsByUser_UseridAndProduct_Productid(Long userid, Long productid);
}