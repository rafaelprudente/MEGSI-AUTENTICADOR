package pt.uminho.megsi.authenticator.domain.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pt.uminho.megsi.authenticator.application.dto.EmailDto;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "REGISTRATION.MESSAGE.1", groupId = "REGISTRATION.MESSAGE.GROUP.ID", containerFactory = "emailKafkaListenerContainerFactory")
    public void receiveRegistrationMessage(EmailDto message) {
        sendSimpleHtmlMail(message);
    }

    public void sendSimpleHtmlMail(EmailDto details) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(details.getSender());
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(details.getBody(), true);

            javaMailSender.send(mimeMessage);

            log.info("Mail Sent Successfully.");
        } catch (Exception e) {
            log.error("Error while sending mail.", e);
        }
    }

    public void sendSimpleMail(EmailDto details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(details.getSender());
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);

            log.info("Mail Sent Successfully.");
        } catch (Exception e) {
            log.error("Error while sending mail.", e);
        }
    }

    public void sendMailWithAttachment(EmailDto details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(details.getSender());
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getBody());
            mimeMessageHelper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            javaMailSender.send(mimeMessage);

            log.info("Mail Sent Successfully.");
        } catch (MessagingException e) {
            log.error("Error while sending mail.", e);
        }
    }
}
