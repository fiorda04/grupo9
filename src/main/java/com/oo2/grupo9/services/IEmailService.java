package com.oo2.grupo9.services;

import jakarta.mail.MessagingException;
import java.util.Map;

public interface IEmailService {

    void prepareAndSendWelcomeEmail(String email, String nombreUsuario) throws Exception;

    void sendEmail(String email, String nombreUsuario, String vistaNombre, String subject, Map<String, Object> variablesDeVista)
            throws MessagingException;
}