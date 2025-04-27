package edu.sia.credigo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.sia.credigo.Transaction.TransactionEntity;
import edu.sia.credigo.Transaction.TransactionRepository;

import org.springframework.security.core.Authentication;



import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {


    
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;



    @PostMapping("/topup")
    public ResponseEntity<String> topUpWallet(@RequestParam BigDecimal amount, Authentication authentication) {
        String username = authentication.getName();
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        UserEntity user = userOpt.get();
        if (user.getWallet() == null) {
            user.setWallet(0L);
        }

        // Top-up wallet
        user.setWallet(user.getWallet() + amount.longValue());
        userRepository.save(user);

        // Save transaction
        TransactionEntity transaction = TransactionEntity.builder()
                .user(user)
                .amount(amount)
                .status("Completed")
                .paymentMethod("Dummy GCash")
                .description("Wallet Top-Up")
                .build();
        transactionRepository.save(transaction);

        return ResponseEntity.ok("Wallet topped up successfully!");
    }
    @GetMapping("/wallet")
    public ResponseEntity<Long> getWalletBalance(Authentication authentication) {
        String username = authentication.getName();
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Long wallet = userOpt.get().getWallet();
        return ResponseEntity.ok(wallet != null ? wallet : 0L);
        }
    

    @PostMapping("/createUser")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("getAllUsers")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}