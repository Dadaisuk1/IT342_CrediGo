package edu.sia.credigo.deposit;

import edu.sia.credigo.User.UserEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "deposits")
public class DepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depositId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Instant datetime;

    @Column(nullable = false)
    private String paymentOption; // "gcash", "maya", "visa", "mastercard"

    public DepositEntity() {}

    public DepositEntity(UserEntity user, BigDecimal amount, Instant datetime, String paymentOption) {
        this.user = user;
        this.amount = amount;
        this.datetime = datetime;
        this.paymentOption = paymentOption;
    }

    public Long getDepositId() { return depositId; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Instant getDatetime() { return datetime; }
    public void setDatetime(Instant datetime) { this.datetime = datetime; }
    public String getPaymentOption() { return paymentOption; }
    public void setPaymentOption(String paymentOption) { this.paymentOption = paymentOption; }
}
