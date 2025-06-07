package com.oo2.grupo9.services;

public interface IEmailService {
    void enviarEmailDeBienvenida(String destinatario, String nombreUsuario, String urlLogin);
}