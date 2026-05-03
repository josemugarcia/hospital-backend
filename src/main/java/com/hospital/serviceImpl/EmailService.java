package com.hospital.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Lista de dominios válidos (puedes agregar más)
    private static final List<String> DOMINIOS_VALIDOS = Arrays.asList(
        "gmail.com", "hotmail.com", "yahoo.com", "outlook.com", 
        "icloud.com", "protonmail.com", "mail.com"
    );

    public void sendEmail(String to, String subject, String body) {
        // Verificar si el correo es válido
        if (!esCorreoValido(to)) {
            System.out.println("❌ Correo no enviado a " + to + " - Dominio no válido para pruebas");
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("✅ Correo enviado exitosamente a: " + to);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo a " + to + ": " + e.getMessage());
        }
    }

    private boolean esCorreoValido(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        // Extraer el dominio después del @
        String[] partes = email.split("@");
        if (partes.length != 2) {
            return false;
        }
        
        String dominio = partes[1].toLowerCase();
        
        // Verificar si el dominio está en la lista de válidos
        // Para pruebas, solo permitir gmail.com
        return dominio.equals("gmail.com");
    }
}