package com.oo2.grupo9;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import javax.crypto.SecretKey;

public class GenerarClave {
    public static void main(String[] args) {
        SecretKey key = Jwts.SIG.HS256.key().build();
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Tu nueva clave secreta es:");
        System.out.println(secretString);
    }
}
