package edu.sia.credigo.buy;

import edu.sia.credigo.Mail.MailEntity;
import edu.sia.credigo.Mail.MailRepository;
import edu.sia.credigo.Product.ProductEntity;
import edu.sia.credigo.Product.ProductRepository;
import edu.sia.credigo.Transaction.TransactionEntity;
import edu.sia.credigo.Transaction.TransactionRepository;
import edu.sia.credigo.User.UserEntity;
import edu.sia.credigo.User.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/buy")
public class BuyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MailRepository mailRepository;

    @PostMapping("/{productId}")
    public ResponseEntity<Map<String,String>> buyProduct(@PathVariable Long productId, Authentication authentication) {
        String username = authentication.getName();
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        Optional<ProductEntity> productOpt = productRepository.findById(productId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));

        }
        if (productOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Product not found"));

        }

        UserEntity user = userOpt.get();
        ProductEntity product = productOpt.get();

        BigDecimal price = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();

        if (user.getWallet() == null || user.getWallet() < price.longValue()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Not enough balance. Please top-up."));

        }

        // Deduct wallet
        user.setWallet(user.getWallet() - price.longValue());
        userRepository.save(user);

        // Create transaction
        TransactionEntity transaction = TransactionEntity.builder()
                .user(user)
                .product(product)
                .amount(price)
                .status("Completed")
                .paymentMethod("Wallet Balance")
                .description("Purchased product: " + product.getProductname())
                .build();
        transactionRepository.save(transaction);

        // Create mail
        MailEntity mail = MailEntity.builder()
                .user(user)
                .subject("Purchase Confirmed: " + product.getProductname())
                .body("Thank you for your purchase! Your Steam Wallet Code: " + generateSteamCode())
                .build();
        mailRepository.save(mail);

        return ResponseEntity.ok(Map.of("message", "Purchase successful!"));

    }

    // Dummy code generator
    private String generateSteamCode() {
        return "STEAM-" + (int)(Math.random() * 1000000);
    }
}
