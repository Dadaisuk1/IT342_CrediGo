package edu.sia.credigo.Mail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import edu.sia.credigo.User.UserEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="tblMail")
public class MailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mailid;
    private String subject;
    private String body;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userid", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @JsonProperty("userid")
    public Long getUserId() {
        return user != null ? user.getUserid() : null;
    }
    
    @CreationTimestamp
    private LocalDateTime createdDate;
}
