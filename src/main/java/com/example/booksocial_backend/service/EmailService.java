package com.example.booksocial_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public void sendFollowConfirmation(String toEmail, String authorName) {
        if (mailSender == null || fromEmail == null || fromEmail.isBlank()) return;
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setFrom(fromEmail);
            msg.setSubject("¡Ahora sigues a " + authorName + " en BookSocial!");
            msg.setText(
                "¡Hola!\n\n" +
                "Has empezado a seguir a " + authorName + " en BookSocial.\n" +
                "Te notificaremos por email cada vez que publique una nueva obra.\n\n" +
                "— El equipo de BookSocial"
            );
            mailSender.send(msg);
            log.info("Email de seguimiento enviado a {}", toEmail);
        } catch (Exception e) {
            log.warn("No se pudo enviar email de seguimiento a {}: {}", toEmail, e.getMessage());
        }
    }

    public void sendNewWorkNotification(String toEmail, String authorName, String workTitle) {
        if (mailSender == null || fromEmail == null || fromEmail.isBlank()) return;
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setFrom(fromEmail);
            msg.setSubject("Nueva obra de " + authorName + " — BookSocial");
            msg.setText(
                "¡Hola!\n\n" +
                authorName + " acaba de publicar una nueva obra: \"" + workTitle + "\".\n" +
                "Entra en BookSocial para descubrirla.\n\n" +
                "— El equipo de BookSocial"
            );
            mailSender.send(msg);
            log.info("Notificación de nueva obra enviada a {}", toEmail);
        } catch (Exception e) {
            log.warn("No se pudo enviar notificación de nueva obra a {}: {}", toEmail, e.getMessage());
        }
    }
}
