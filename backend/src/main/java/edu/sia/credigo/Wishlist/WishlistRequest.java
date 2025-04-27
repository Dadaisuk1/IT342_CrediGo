package edu.sia.credigo.Wishlist;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistRequest {
    private Long userid;
    private Long productid;
}
