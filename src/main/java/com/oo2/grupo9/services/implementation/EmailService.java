package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.helpers.MailConfigHelper;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Slf4j
public class EmailService implements IEmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private HttpServletRequest request;

    
    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, HttpServletRequest request) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.request = request;
    }

    //Este metodo deberiamos usar en el controller que guarda al usuario en la base de datos
    @Override
    public void prepareAndSendWelcomeEmail(String email, String nombreUsuario) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("nombreUsuario", nombreUsuario);
        //se arma la url
        String scheme = request.getScheme(); // http
        String serverName = request.getServerName(); // localhost
        int serverPort = request.getServerPort(); // 8080
        String contextPath = request.getContextPath(); // Usualmente vacío
        String urlBase = scheme + "://" + serverName + ":" + serverPort + contextPath + "/";
        String loginURL = urlBase + ViewRouteHelper.USUARIO_LOGIN; 

        message.put("loginURL", loginURL);

        this.sendEmail(email, nombreUsuario, MailConfigHelper.EMAIL_BIENVENIDA,"¡Bienvenido a nuestro servicio!", message);
    }

    @Override
    public void sendEmail(String email, String nombreUsuario, String vistaNombre, String subject, Map<String, Object> variablesDeVista)
            throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            
            Context context = new Context();
            context.setVariables(variablesDeVista);

            String htmlContent = templateEngine.process(vistaNombre, context);
            mimeMessageHelper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MessagingException(e.getMessage()); 
        }
    }
}
