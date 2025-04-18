package edu.sia.credigo.Mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mails")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/createMail")
    public ResponseEntity<MailEntity> createMail(@RequestBody MailEntity mail) {
        MailEntity newMail = mailService.createMail(mail);
        return ResponseEntity.ok(newMail);
    }

    @GetMapping("/getAllMail")
    public ResponseEntity<List<MailEntity>> getAllMails() {
        List<MailEntity> mails = mailService.getAllMails();
        return ResponseEntity.ok(mails);
    }

    @GetMapping("/getMailById/{id}")
    public ResponseEntity<MailEntity> getMailById(@PathVariable Long id) {
        return mailService.getMailById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getUserMails/{userId}")
    public ResponseEntity<List<MailEntity>> getMailsByUserId(@PathVariable Long userId) {
        List<MailEntity> mails = mailService.getMailsByUserId(userId);
        return ResponseEntity.ok(mails);
    }


    @PutMapping("/updateMail/{id}")
    public ResponseEntity<MailEntity> updateMail(
            @PathVariable Long id,
            @RequestBody MailEntity mail) {
        MailEntity updatedMail = mailService.updateMail(id, mail);
        if (updatedMail != null) {
            return ResponseEntity.ok(updatedMail);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteMail/{id}")
    public ResponseEntity<Void> deleteMail(@PathVariable Long id) {
        if (mailService.deleteMail(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
