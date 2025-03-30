package edu.sia.credigo.Mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MailService {

    @Autowired
    private MailRepository mailRepository;

    public MailEntity createMail(MailEntity mail) {
        return mailRepository.save(mail);
    }

    public List<MailEntity> getAllMails() {
        return mailRepository.findAll();
    }

    public Optional<MailEntity> getMailById(Long id) {
        return mailRepository.findById(id);
    }

    public List<MailEntity> getMailsByUserId(Long id) {
        return mailRepository.findByUser_Userid(id);
    }

    public MailEntity updateMail(Long id, MailEntity updatedMail) {
        Optional<MailEntity> existingMail = mailRepository.findById(id);
        if (existingMail.isPresent()) {
            MailEntity mail = existingMail.get();
            mail.setSubject(updatedMail.getSubject());
            mail.setBody(updatedMail.getBody());
            return mailRepository.save(mail);
        }
        return null;
    }

    public boolean deleteMail(Long id) {
        if (mailRepository.existsById(id)) {
            mailRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
