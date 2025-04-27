package edu.sia.credigo.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long productid;
    private String productname;
    private String imageUrl;
    private Double price;
    private Double salePrice;
    private String categoryname; // optional for wishlist
}
