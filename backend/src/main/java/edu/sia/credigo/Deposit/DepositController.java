package edu.sia.credigo.Deposit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {
    @Autowired
    private DepositService depositService;

    @GetMapping("/getAllDeposits")
    public List<DepositEntity> getAllDeposits() {
        return depositService.getAllDeposits();
    }

    @GetMapping("/user/{userId}")
    public List<DepositEntity> getDepositsByUser(@PathVariable Long userId) {
        return depositService.getDepositsByUserId(userId);
    }

    @GetMapping("/getDepositById/{id}")
    public ResponseEntity<DepositEntity> getDepositById(@PathVariable Long id) {
        Optional<DepositEntity> deposit = depositService.getDepositById(id);
        return deposit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createDeposit")
    public DepositEntity createDeposit(@RequestParam Long userId,
                                       @RequestParam BigDecimal amount,
                                       @RequestParam String paymentOption) {
        return depositService.createDeposit(userId, amount, paymentOption);
    }
}
