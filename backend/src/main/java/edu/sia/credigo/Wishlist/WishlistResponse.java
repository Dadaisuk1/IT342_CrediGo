package edu.sia.credigo.Wishlist;

import edu.sia.credigo.Product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistResponse {
    private Long wishlistId;
    private ProductResponse product;
}
