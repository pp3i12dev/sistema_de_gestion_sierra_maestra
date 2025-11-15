package com.sca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String htmlBody) {
        try {
            // Crear mensaje MIME
            MimeMessage message = mailSender.createMimeMessage();

            // Configurar helper para mensaje HTML
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Configurar remitente, destinatario, asunto y cuerpo
            helper.setFrom("sierramaestralaplata@gmail.com"); // debe coincidir con spring.mail.username
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = cuerpo HTML

            // Enviar el mensaje
            mailSender.send(message);

        } catch (MessagingException e) {
            // Mostrar error en consola y lanzar excepción
            e.printStackTrace();
            throw new RuntimeException("Error enviando correo: " + e.getMessage(), e);
        }
    }
}
