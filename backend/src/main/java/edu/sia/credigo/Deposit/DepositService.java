package edu.sia.credigo.deposit;

import edu.sia.credigo.User.UserEntity;
import edu.sia.credigo.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class DepositService {
    @Autowired
    private DepositRepository depositRepository;
    @Autowired
    private UserRepository userRepository;

    public List<DepositEntity> getAllDeposits() {
        return depositRepository.findAll();
    }

    public Optional<DepositEntity> getDepositById(Long id) {
        return depositRepository.findById(id);
    }

    public List<DepositEntity> getDepositsByUserId(Long userId) {
        return depositRepository.findAll().stream()
                .filter(d -> d.getUser().getUserId().equals(userId))
                .toList();
    }

    public DepositEntity createDeposit(Long userId, BigDecimal amount, String paymentOption) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        DepositEntity deposit = new DepositEntity(user, amount, Instant.now(), paymentOption);
        return depositRepository.save(deposit);
    }
}
