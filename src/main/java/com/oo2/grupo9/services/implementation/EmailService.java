package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.services.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Async
    @Override
    public void enviarEmailDeBienvenida(String destinatario, String nombreUsuario,  String urlLogin) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("santi.fiorda04@gmail.com");
        message.setTo(destinatario); 
        message.setSubject("¡Bienvenido a Gestión de Tickets!");
        String texto = String.format("Hola %s,\n\n¡Gracias por registrarte en nuestro sistema de gestión de tickets!\n\nTu cuenta ha sido creada exitosamente.\n"+ urlLogin + "\n\nSaludos,\nEl equipo del Grupo 9.", nombreUsuario);
        message.setText(texto);
        
        try {
            mailSender.send(message);
            System.out.println("Email de bienvenida enviado a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar el email de bienvenida a " + destinatario + ": " + e.getMessage());
        }
    }
}
