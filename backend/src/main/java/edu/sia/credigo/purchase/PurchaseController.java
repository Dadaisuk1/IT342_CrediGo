package edu.sia.credigo.purchase;

import edu.sia.credigo.Product.ProductEntity;
import edu.sia.credigo.Product.ProductRepository;
import edu.sia.credigo.Transaction.TransactionEntity;
import edu.sia.credigo.Transaction.TransactionRepository;
import edu.sia.credigo.User.UserEntity;
import edu.sia.credigo.User.UserRepository;
import edu.sia.credigo.Mail.MailEntity;
import edu.sia.credigo.Mail.MailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MailRepository mailRepository;

    @PostMapping("/{productId}")
    public ResponseEntity<?> purchaseProduct(@PathVariable Long productId, Authentication authentication) {
        String username = authentication.getName();
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        Optional<ProductEntity> productOpt = productRepository.findById(productId);

        if (userOpt.isEmpty() || productOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User or Product not found.");
        }

        UserEntity user = userOpt.get();
        ProductEntity product = productOpt.get();

        BigDecimal price = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();

        if (user.getWallet() == null || user.getWallet() < price.longValue()) {
            return ResponseEntity.badRequest().body("Not enough wallet balance.");
        }

        // Deduct wallet
        user.setWallet(user.getWallet() - price.longValue());
        userRepository.save(user);

        // Save transaction
        TransactionEntity transaction = TransactionEntity.builder()
                .user(user)
                .product(product)
                .amount(price)
                .status("Completed")
                .paymentMethod("Wallet Payment")
                .description("Purchased: " + product.getProductname())
                .build();
        transactionRepository.save(transaction);

        // Generate dummy Steam Wallet Code
        String steamCode = generateSteamCode();

        // Save mail
        MailEntity mail = MailEntity.builder()
                .user(user)
                .subject("Purchase Successful - Steam Wallet Code")
                .body("Thank you for purchasing " + product.getProductname() + "!\n\nHere is your Steam Wallet Code: " + steamCode)
                .build();
        mailRepository.save(mail);

        return ResponseEntity.ok("Purchase successful! Steam Wallet Code sent to your mail.");
    }

    private String generateSteamCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 4; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        code.append("-");
        for (int i = 0; i < 4; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        code.append("-");
        for (int i = 0; i < 4; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }
}
