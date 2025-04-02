package edu.sia.credigo.Mail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<MailEntity, Long> {
    List<MailEntity> findByUser_Userid(Long userid);

}
