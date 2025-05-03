package edu.sia.credigo.Deposit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<DepositEntity, Long> {
    // Optionally add custom queries here
}
